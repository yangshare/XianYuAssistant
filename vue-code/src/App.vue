<script setup lang="ts">
import { RouterView } from 'vue-router'
import AppHeader from './components/AppHeader.vue'
</script>

<template>
  <div class="app-container">
    <!-- 顶部栏 -->
    <AppHeader />

    <el-container class="main-container">
      <!-- 侧边栏 -->
      <el-aside width="200px" class="sidebar">
        <div class="logo">
          <div class="logo-icon">闲</div>
          <div class="logo-text">自动化管理</div>
        </div>
        <el-menu
          :default-active="$route.path"
          router
          class="nav-menu"
        >
          <el-menu-item index="/dashboard">
            <span>📊 仪表板</span>
          </el-menu-item>

          <div class="menu-group-title">
            <span class="group-icon">-</span>
            <span class="group-text">账号管理</span>
          </div>

          <el-menu-item index="/accounts">
            <span>👤 某鱼账号</span>
          </el-menu-item>
          <el-menu-item index="/connection">
            <span>🔗 连接管理</span>
          </el-menu-item>

          <div class="menu-group-title">
            <span class="group-icon">-</span>
            <span class="group-text">业务管理</span>
          </div>

          <el-menu-item index="/goods">
            <span>📦 商品管理</span>
          </el-menu-item>
          <el-menu-item index="/orders">
            <span>📋 订单管理</span>
          </el-menu-item>
          <el-menu-item index="/messages">
            <span>💬 消息管理</span>
          </el-menu-item>

          <div class="menu-group-title">
            <span class="group-icon">-</span>
            <span class="group-text">自动化</span>
          </div>

          <el-menu-item index="/auto-delivery">
            <span>🤖 自动发货</span>
          </el-menu-item>
          <el-menu-item index="/auto-reply">
            <span>💭 自动回复</span>
          </el-menu-item>

          <div class="menu-group-title">
            <span class="group-icon">-</span>
            <span class="group-text">日志记录</span>
          </div>

          <el-menu-item index="/operation-log">
            <span>📜 操作日志</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-container>
        <el-main>
          <RouterView />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
.app-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--bg-base);
}

.main-container {
  flex: 1;
  overflow: hidden;
}

.sidebar {
  background: var(--bg-elevated);
  border-right: 1px solid var(--border-light);
}

.logo {
  display: flex;
  align-items: center;
  padding: 16px 20px;
  gap: 12px;
}

.logo-icon {
  width: 32px;
  height: 32px;
  background: var(--theme-primary);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 16px;
  font-weight: bold;
}

.logo-text {
  font-size: 15px;
  font-weight: 600;
  color: var(--text-primary);
}

.nav-menu {
  border-right: none;
  background: transparent;
}

:deep(.el-menu-item) {
  margin: 2px 12px;
  border-radius: var(--radius-md);
  color: var(--text-secondary);
  transition: all var(--transition-fast);
}

:deep(.el-menu-item:hover) {
  background: var(--bg-hover);
  color: var(--text-primary);
}

:deep(.el-menu-item.is-active) {
  background: var(--theme-primary) !important;
  color: white;
}

.el-main {
  padding: 24px;
  overflow-y: auto;
  background: var(--bg-base);
}

.menu-group-title {
  display: flex;
  align-items: center;
  padding: 16px 24px 6px;
  margin-top: 24px;
  gap: 6px;
  pointer-events: none;
  user-select: none;
}

.menu-group-title:first-child {
  margin-top: 12px;
}

.group-icon {
  font-size: 12px;
  opacity: 0.5;
}

.group-text {
  font-size: 10px;
  font-weight: 700;
  color: var(--text-tertiary);
  text-transform: uppercase;
  letter-spacing: 1px;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .el-aside {
    width: 60px !important;
  }

  .logo-text {
    display: none;
  }

  .el-main {
    padding: 16px 20px;
  }

  :deep(.el-menu-item span) {
    font-size: 12px;
  }
}

@media (max-width: 480px) {
  .main-container {
    flex-direction: column;
  }

  .el-aside {
    width: 100% !important;
    height: auto !important;
  }

  .logo {
    justify-content: center;
  }

  .el-main {
    padding: 10px;
  }
}
</style>
