<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

// 面包屑数据
const breadcrumbs = computed(() => {
  const pathMap: Record<string, string> = {
    '/dashboard': '仪表板',
    '/accounts': '某鱼账号',
    '/connection': '连接管理',
    '/goods': '商品管理',
    '/orders': '订单管理',
    '/messages': '消息管理',
    '/auto-delivery': '自动发货',
    '/auto-reply': '自动回复',
    '/records': '操作记录',
    '/operation-log': '操作日志'
  }
  return pathMap[route.path] || '未知页面'
})
</script>

<template>
  <header class="app-header">
    <div class="header-left">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ breadcrumbs }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="header-center">
      <!-- 可选：搜索框 -->
    </div>

    <div class="header-right">
      <span class="version">v1.0.0</span>
    </div>
  </header>
</template>

<style scoped>
.app-header {
  height: 48px;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--bg-elevated);
  border-bottom: 1px solid var(--border-light);
}

.header-left {
  display: flex;
  align-items: center;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.version {
  font-size: 12px;
  color: var(--text-tertiary);
}
</style>
