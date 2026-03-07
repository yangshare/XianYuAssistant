<template>
  <div class="auto-reply-page">
    <div class="page-header">
      <h1 class="page-title">自动回复</h1>
    </div>

    <el-card class="content-card">
      <div class="empty-state">
        <span class="empty-icon">🔧</span>
        <p class="empty-text">自动回复功能开发中...</p>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
// Auto reply page
</script>

<style scoped>
.auto-reply-page {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
  margin: 0;
}

.content-card {
  min-height: 400px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 20px;
}

.empty-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 16px;
  color: var(--text-secondary);
  margin: 0;
}
</style>
