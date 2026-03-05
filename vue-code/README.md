# 某鱼自动化管理系统 - Vue3 前端

某鱼自动化管理系统的 Vue3 + TypeScript 前端项目，从原有的静态 HTML/JS 项目迁移而来。

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架，使用 Composition API
- **TypeScript** - 完整类型安全
- **Vue Router** - 路由管理，支持懒加载
- **Pinia** - 状态管理
- **Element Plus** - UI 组件库
- **Axios** - HTTP 请求封装
- **Vite** - 快速构建工具

## 项目特点

### 1. 细粒度结构
每个页面都遵循一致的结构模式：
- 独立的目录
- 页面组件（index.vue）
- 样式文件（[page].css）
- 业务逻辑（use[Page]Manager.ts Composable）
- 子组件目录（components/）

### 2. 类型安全
- 完整的 TypeScript 类型定义
- API 响应类型化
- 组件 Props 和 Emits 类型化

### 3. 模块化设计
- API 按功能模块拆分
- 工具函数独立封装
- 组件高度复用

### 4. 现代化开发
- Vue 3 Composition API
- Composable 模式管理状态
- Vite 快速热更新
- 路由懒加载和 Tree Shaking

## 项目结构

```
vue-code/
├── src/
│   ├── api/                    # API 接口模块
│   │   ├── account.ts         # 账号相关 API
│   │   ├── goods.ts           # 商品相关 API
│   │   ├── websocket.ts       # WebSocket 相关 API
│   │   ├── message.ts         # 消息相关 API
│   │   └── qrlogin.ts         # 二维码登录 API
│   ├── types/                  # TypeScript 类型定义
│   │   └── index.ts           # 全局类型
│   ├── utils/                  # 工具函数
│   │   ├── index.ts           # 通用工具（格式化、提示等）
│   │   └── request.ts         # HTTP 请求封装
│   ├── router/                 # 路由配置
│   │   └── index.ts           # 路由定义
│   ├── views/                  # 页面组件
│   │   ├── dashboard/         # 仪表板（✅ 完整实现）
│   │   ├── accounts/          # 账号管理（✅ 基础结构）
│   │   ├── connection/        # 连接管理
│   │   ├── goods/             # 商品管理
│   │   ├── messages/          # 消息管理
│   │   ├── auto-delivery/     # 自动发货
│   │   ├── auto-reply/        # 自动回复
│   │   ├── records/           # 操作记录
│   │   └── qrlogin/           # 扫码登录
│   ├── App.vue                 # 根组件
│   └── main.ts                 # 入口文件
├── public/                     # 公共资源
├── vite.config.ts             # Vite 配置（含代理）
├── package.json               # 依赖配置
└── README.md                  # 本文档
```

## 快速开始

### 1. 安装依赖

```bash
cd vue-code
pnpm install
```

### 2. 配置开发代理

编辑 `vite.config.ts`，确保代理配置正确：

```typescript
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
```

### 3. 启动开发服务器

```bash
pnpm run dev
```

访问 `http://localhost:5173`

### 4. 构建生产版本

```bash
pnpm run build
```

构建产物在 `dist/` 目录

### 5. 部署到 Spring Boot

将构建后的文件复制到 Spring Boot 项目：

```bash
# Windows
xcopy /E /I /Y dist\* ..\src\main\resources\static\

# Linux/Mac
cp -r dist/* ../src/main/resources/static/
```

## 功能模块

- 📊 **仪表板** - 系统概览和统计信息
- 👤 **某鱼账号管理** - 账号的增删改查
- 🔗 **WebSocket 连接管理** - 管理 WebSocket 连接状态
- 📦 **商品管理** - 商品列表和配置
- 💬 **消息管理** - 聊天消息查看
- 🤖 **自动发货** - 配置和记录
- 💭 **自动回复** - 配置和记录
- 📝 **操作记录** - 查看历史操作
- 📱 **扫码登录** - 二维码登录添加账号

## 页面结构规范

每个页面遵循以下细粒度结构：

```
views/[page]/
├── index.vue              # 页面主组件（组合子组件）
├── [page].css            # 页面样式（布局、响应式）
├── use[Page]Manager.ts   # 业务逻辑（状态管理、API 调用）
└── components/           # 页面子组件
    ├── [Component].vue   # 可复用组件
    └── ...
```

### 组件模板

```vue
<script setup lang="ts">
import { ref } from 'vue'

interface Props {
  data: any[]
  loading?: boolean
}

interface Emits {
  (e: 'action', id: number): void
}

const props = withDefaults(defineProps<Props>(), {
  loading: false
})

const emit = defineEmits<Emits>()

const handleAction = (id: number) => {
  emit('action', id)
}
</script>

<template>
  <div class="component-name">
    <!-- 组件内容 -->
  </div>
</template>

<style scoped>
.component-name {
  /* 组件样式 */
}
</style>
```

## 开发指南

### 已完成的页面

- ✅ **Dashboard（仪表板）** - 完整实现
  - 统计信息展示
  - 欢迎信息
  - 快速开始指南

- ✅ **Accounts（账号管理）** - 基础结构完成
  - 页面框架搭建
  - 业务逻辑封装
  - 待完成：子组件（AccountTable、AddAccountDialog 等）

### 待完成的页面

参考原有代码：`src/main/resources/static/js/pages/[page]/`

1. **Connection（连接管理）**
2. **Goods（商品管理）**
3. **Messages（消息管理）**
4. **AutoDelivery（自动发货）**
5. **AutoReply（自动回复）**
6. **Records（操作记录）**
7. **QRLogin（扫码登录）**

### 开发步骤

1. 启动开发服务器：`npm run dev`
2. 查看原有 JS 代码了解业务逻辑
3. 创建页面目录和文件
4. 实现 Composable 业务逻辑
5. 创建子组件
6. 编写样式
7. 测试功能

## 技术栈对比

| 原项目 | Vue3 项目 |
|--------|-----------|
| 原生 JavaScript | TypeScript |
| 手动 DOM 操作 | Vue 响应式 |
| 全局变量 | Composable |
| 内联样式 | CSS 模块 |
| fetch API | Axios |
| 无类型检查 | 完整类型定义 |
| 手动路由 | Vue Router |
| 无组件化 | 组件化开发 |

## 开发命令

```bash
# 安装依赖
pnpm install

# 启动开发服务器
pnpm run dev

# 构建生产版本
pnpm run build

# 预览生产构建
pnpm run preview

# 类型检查
pnpm run type-check

# 代码检查
pnpm run lint
```

## 注意事项

1. **API 响应格式**: 后端返回 `code: 200` 或 `code: 0` 表示成功
2. **类型安全**: 充分利用 TypeScript 的类型检查
3. **组件复用**: 将重复的 UI 抽取为公共组件
4. **样式规范**: 使用 scoped 样式，遵循 BEM 命名
5. **错误处理**: 统一的错误提示机制

## 后端 API

- **开发环境**: `http://localhost:8080/api`
- **生产环境**: 通过 Vite 代理自动转发

## 下一步

1. 完成 Accounts 页面的所有子组件
2. 参考已完成的页面，创建其他页面
3. 测试所有功能
4. 优化样式和交互
5. 构建并部署

---

开发时参考已完成的 Dashboard 和 Accounts 页面作为模板，保持代码风格一致。
