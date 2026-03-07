import { ref, reactive, computed } from 'vue'
import { getDashboardStats } from '@/api/dashboard'
import { getAccountList } from '@/api/account'
import { getConnectionStatus } from '@/api/websocket'
import { getGoodsList } from '@/api/goods'
import type { Goods } from '@/types'

// 自动化配置状态常量
const AUTO_CONFIG_ON = 1

export function useDashboard() {
  const loading = ref(false)

  const stats = reactive({
    accountCount: 0,
    goodsCount: 0,
    onlineGoodsCount: 0
  })

  // 步骤完成状态
  const stepStatus = reactive({
    hasAccount: false,      // 步骤1: 是否有账号
    hasConnection: false,   // 步骤2: 是否有已连接的账号
    hasGoods: false,        // 步骤3: 是否有商品
    hasAutoConfig: false    // 步骤4: 是否有自动化配置
  })

  // 计算当前激活的步骤 (0-3 对应步骤1-4)
  const activeStep = computed(() => {
    if (!stepStatus.hasAccount) return 0
    if (!stepStatus.hasConnection) return 1
    if (!stepStatus.hasGoods) return 2
    if (!stepStatus.hasAutoConfig) return 3
    return 4 // 全部完成
  })

  const loadStatistics = async () => {
    loading.value = true
    try {
      const res = await getDashboardStats()
      if (res.code === 0 || res.code === 200) {
        if (res.data) {
          stats.accountCount = res.data.accountCount || 0
          stats.goodsCount = res.data.itemCount || 0
          stats.onlineGoodsCount = res.data.sellingItemCount || 0
        }
      }
    } catch (error) {
      console.error('加载统计数据失败:', error)
    } finally {
      loading.value = false
    }
  }

  // 加载步骤完成状态
  const loadStepStatus = async () => {
    try {
      // 1. 检查是否有账号
      const accountRes = await getAccountList()
      const accounts = accountRes.data?.accounts || []
      stepStatus.hasAccount = accounts.length > 0

      if (!stepStatus.hasAccount) {
        return // 没有账号，后续步骤都不用检查
      }

      const firstAccount = accounts[0]
      if (firstAccount?.id) {
        // 2-3. 并行检查连接状态和商品列表
        const [connectionRes, goodsRes] = await Promise.all([
          getConnectionStatus(firstAccount.id),
          getGoodsList({
            xianyuAccountId: firstAccount.id,
            pageNum: 1,
            pageSize: 1
          })
        ])

        stepStatus.hasConnection = connectionRes.data?.connected === true
        const goodsCount = goodsRes.data?.totalCount || 0
        stepStatus.hasGoods = goodsCount > 0

        // 4. 检查是否有自动化配置
        if (stepStatus.hasGoods && goodsRes.data?.itemsWithConfig?.length > 0) {
          const items = goodsRes.data.itemsWithConfig as Goods[]
          stepStatus.hasAutoConfig = items.some(item =>
            item.xianyuAutoDeliveryOn === AUTO_CONFIG_ON || item.xianyuAutoReplyOn === AUTO_CONFIG_ON
          )
        }
      }
    } catch (error) {
      console.error('加载步骤状态失败:', error)
    }
  }

  return {
    loading,
    stats,
    activeStep,
    loadStatistics,
    loadStepStatus
  }
}