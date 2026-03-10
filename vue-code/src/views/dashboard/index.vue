<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useDashboard } from './useDashboard';

const router = useRouter();
const { loading, stats, activeStep, loadStatistics, loadStepStatus } = useDashboard();

// 用户手动选择的步骤（用于卡片点击交互）
const selectedStep = ref(0);

// 组件挂载时并行加载数据
Promise.all([loadStatistics(), loadStepStatus()]);

// 快速开始步骤
const quickStartSteps = [
  {
    title: '1. 添加某鱼账号',
    icon: '👤',
    description: '通过扫码登录添加您的某鱼账号',
    route: '/accounts',
    details: [
      '点击左侧菜单"某鱼账号"',
      '点击"扫码登录"按钮',
      '使用某鱼APP扫描二维码',
      '等待登录成功，账号自动添加'
    ]
  },
  {
    title: '2. 启动WebSocket连接',
    icon: '🔗',
    description: '建立与某鱼服务器的实时连接',
    route: '/connection',
    details: [
      '进入"连接管理"页面',
      '选择要连接的账号',
      '点击"启动连接"按钮',
      '等待连接成功，开始监听消息'
    ]
  },
  {
    title: '3. 同步商品信息',
    icon: '📦',
    description: '获取您的某鱼商品列表',
    route: '/goods',
    details: [
      '进入"商品管理"页面',
      '选择已连接的账号',
      '点击"刷新商品"按钮',
      '等待商品同步完成'
    ]
  },
  {
    title: '4. 配置自动化功能',
    icon: '🤖',
    description: '设置自动发货和自动回复',
    route: '/auto-delivery',
    details: [
      '在商品列表中找到目标商品',
      '开启"自动发货"或"自动回复"',
      '配置发货内容或回复规则',
      '保存配置，自动化开始工作'
    ]
  }
];

// 时间轴类型
const getTimelineType = (index: number) => {
  if (selectedStep.value === index) return 'primary';
  if (index < activeStep.value) return 'success';
  return 'info';
};

// 时间轴颜色
const getTimelineColor = (index: number) => {
  if (selectedStep.value === index) return 'var(--theme-primary)';
  if (index < activeStep.value) return 'var(--color-success)';
  return 'var(--border-color)';
};

// 快捷菜单
const quickMenus = [
  { icon: '👤', title: '某鱼账号', route: '/accounts' },
  { icon: '🔗', title: '连接管理', route: '/connection' },
  { icon: '📦', title: '商品管理', route: '/goods' },
  { icon: '📋', title: '订单管理', route: '/orders' },
  { icon: '💬', title: '消息管理', route: '/messages' },
  { icon: '🤖', title: '自动发货', route: '/auto-delivery' },
  { icon: '💭', title: '自动回复', route: '/auto-reply' }
];

// 常见问题
const faqs = [
  {
    question: '如何获取Cookie？',
    answer: '在连接管理页面，点击Cookie部分的"❓"帮助按钮，查看详细的获取步骤和示例图片。'
  },
  {
    question: 'WebSocket连接失败怎么办？',
    answer: '1. 检查Cookie是否有效；2. 尝试刷新Token；3. 如果提示需要滑块验证，访问 https://www.goofish.com/im 完成验证后手动更新Cookie和Token。'
  },
  {
    question: '自动发货什么时候触发？',
    answer: '当买家付款后，系统会自动检测到"已付款待发货"消息，并根据配置自动发送发货信息。'
  },
  {
    question: 'Token过期了怎么办？',
    answer: '系统会自动刷新Token（1.5-2.5小时刷新一次），也可以在连接管理页面手动刷新。'
  }
];

// 跳转到指定页面
const navigateTo = (route: string) => {
  router.push(route);
};

// 选择步骤
const selectStep = (index: number) => {
  console.log('点击卡片 index:', index, '当前 selectedStep:', selectedStep.value);
  selectedStep.value = index;
  console.log('更新后 selectedStep:', selectedStep.value);
};
</script>

<template>
  <div class="dashboard-page">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row" v-loading="loading">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon account-icon">👤</div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.accountCount }}</div>
              <div class="stat-label">某鱼账号</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon goods-icon">📦</div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.goodsCount }}</div>
              <div class="stat-label">商品总数</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon online-icon">🔥</div>
            <div class="stat-info">
              <div class="stat-value">{{ stats.onlineGoodsCount }}</div>
              <div class="stat-label">在售商品</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快捷入口 -->
    <el-card class="quick-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">🚀 快捷入口</span>
          <span class="card-subtitle">快速访问常用功能</span>
        </div>
      </template>

      <div class="quick-menu-grid">
        <div
          v-for="(menu, index) in quickMenus"
          :key="index"
          class="quick-menu-item"
          @click="navigateTo(menu.route)"
        >
          <span class="quick-menu-icon">{{ menu.icon }}</span>
          <span class="quick-menu-text">{{ menu.title }}</span>
        </div>
      </div>
    </el-card>

    <!-- 快速开始指南 -->
    <el-card class="guide-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">🚀 快速指南</span>
          <span class="card-subtitle">4步完成系统配置，开启自动化之旅</span>
        </div>
      </template>

      <div class="timeline-container">
        <el-timeline>
          <el-timeline-item
            v-for="(step, index) in quickStartSteps"
            :key="index"
            :icon="step.icon"
            :type="getTimelineType(index)"
            :color="getTimelineColor(index)"
            :hollow="selectedStep !== index"
            :size="selectedStep === index ? 'large' : 'normal'"
            class="timeline-item"
          >
            <el-card
              :class="['timeline-card', { 'active': selectedStep === index }]"
              shadow="hover"
              @click="selectStep(index)"
            >
              <div class="timeline-card-header">
                <span class="timeline-step-icon">{{ step.icon }}</span>
                <span class="timeline-step-title">{{ step.title }}</span>
              </div>
              <div class="timeline-step-description">{{ step.description }}</div>

              <div v-if="selectedStep === index" class="timeline-details">
                <ul class="timeline-details-list">
                  <li v-for="(detail, idx) in step.details" :key="idx">
                    {{ detail }}
                  </li>
                </ul>
                <el-button
                  type="primary"
                  size="small"
                  class="timeline-button"
                  @click.stop="navigateTo(step.route)"
                >
                  前往操作 →
                </el-button>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-card>

    <!-- 常见问题 -->
    <el-card class="faq-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">❓ 常见问题</span>
          <span class="card-subtitle">快速解答您的疑问</span>
        </div>
      </template>

      <el-collapse accordion>
        <el-collapse-item
          v-for="(faq, index) in faqs"
          :key="index"
          :name="index"
        >
          <template #title>
            <span class="faq-question">{{ faq.question }}</span>
          </template>
          <div class="faq-answer">{{ faq.answer }}</div>
        </el-collapse-item>
      </el-collapse>
    </el-card>

    <!-- 底部提示 -->
    <el-card class="tips-card" shadow="never">
      <el-alert
        title="💡 温馨提示"
        type="info"
        :closable="false"
      >
        <ul class="tips-list">
          <li>系统会自动刷新Token，保持登录状态，无需频繁重新登录</li>
          <li>建议不要频繁启动/断开连接，避免触发人机验证</li>
          <li>自动发货和自动回复需要先启动WebSocket连接才能生效</li>
          <li>所有操作都会记录在"操作日志"中，方便追踪和排查问题</li>
        </ul>
      </el-alert>
    </el-card>
  </div>
</template>

<style scoped src="./dashboard.css"></style>
