# 某鱼自动化助手

<div align="center">

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen.svg)
![Vue](https://img.shields.io/badge/Vue-3.5-green.svg)

一个简洁高效的某鱼店铺自动化管理工具

[快速开始](#-快速开始) · [功能特性](#-功能特性) · [部署方式](#-部署方式) · [常见问题](#-常见问题)

</div>

---

## ✨ 功能特性

- 🔐 **多账号管理** - 支持同时管理多个某鱼账号
- 🚀 **自动发货** - 买家付款后自动发送发货内容
- 💬 **自动回复** - 关键词匹配，智能回复买家消息
- 🔗 **实时消息** - WebSocket 连接，即时接收消息
- 📦 **商品/订单管理** - 同步商品信息，查看订单列表
- 🔄 **Token 自动刷新** - 随机间隔策略，保持登录状态

## 🚀 快速开始

### 环境要求

- Java 21+

### 启动应用

```bash
# 下载 JAR 包后直接运行
java -jar xianyu-assistant.jar
```

打开浏览器访问 `http://localhost:12400`

### 使用流程

1. **添加账号** → 扫码登录某鱼账号
2. **启动连接** → 建立 WebSocket 连接
3. **同步商品** → 刷新商品列表
4. **配置自动化** → 开启自动发货/回复

## 📦 部署方式

<details>
<summary><b>JAR 包部署（推荐）</b></summary>

从 [Releases](https://github.com/IAMLZY2018/-XianYuAssistant/releases) 下载最新版本：

```bash
java -jar xianyu-assistant.jar
```

后台运行：
```bash
# Linux/Mac
nohup java -jar xianyu-assistant.jar &

# Windows
start /b java -jar xianyu-assistant.jar
```

</details>

<details>
<summary><b>Docker 部署</b></summary>

```bash
git clone https://gitee.com/lzy2018cn/xian-yu-assistant.git
cd xian-yu-assistant
docker compose up -d
```

访问 `http://localhost:12400`

> 📖 **详细部署指南**: 查看 [Docker部署详细指南](docs/Docker部署详细指南.md) 了解完整的部署流程、配置说明和故障排查。

</details>

<details>
<summary><b>源码构建</b></summary>

环境要求：Java 21+、Node.js 20.19+

```bash
git clone https://gitee.com/lzy2018cn/xian-yu-assistant.git
cd xian-yu-assistant

# 构建前端
cd vue-code && npm install && npm run build && cd ..

# 启动后端
./mvnw spring-boot:run
```

</details>

## 📸 界面预览

| 消息管理 | 自动发货配置 |
|:---:|:---:|
| ![消息管理](docs/images/1.png) | ![自动发货](docs/images/2.png) |

| 账号管理 | 商品管理 |
|:---:|:---:|
| ![账号管理](docs/images/3.png) | ![商品管理](docs/images/4.png) |

## 🛠️ 技术栈

| 后端 | 前端 |
|---|---|
| Java 21 | Vue 3.5 |
| Spring Boot 3.5 | TypeScript |
| MyBatis-Plus | Element Plus |
| SQLite | Vite 7 |
| WebSocket | Pinia |

## ❓ 常见问题

<details>
<summary><b>WebSocket 连接失败？</b></summary>

1. 检查 Cookie 是否有效
2. 访问 https://www.goofish.com/im 完成滑块验证
3. 手动更新 Cookie 和 Token

> 📖 **深入了解**: 查看 [某鱼WebSocket消息处理完整流程](docs/某鱼WebSocket消息处理完整流程.md) 了解 WebSocket 连接、消息处理和自动发货的完整技术细节。

</details>

<details>
<summary><b>如何获取 Cookie 和 Token？</b></summary>

点击连接管理页面中对应区域的"如何获取？"按钮查看图文教程。

</details>

<details>
<summary><b>Token 过期了怎么办？</b></summary>

系统会自动刷新（1.5-2.5小时间隔），也可手动更新。

</details>

## 📄 许可证

[MIT License](LICENSE)

## 📚 深入阅读

想要深入了解系统技术细节？查看以下文档：

| 文档 | 描述 |
|:---|:---|
| [Docker部署详细指南](docs/Docker部署详细指南.md) | Docker 完整部署流程、配置说明、故障排查和生产环境建议 |
| [某鱼WebSocket消息处理完整流程](docs/某鱼WebSocket消息处理完整流程.md) | WebSocket 连接建立、消息解析、事件驱动和自动发货的技术实现 |

## 🤝 致谢

- 参考项目：https://github.com/zhinianboke/xianyu-auto-reply

---

<div align="center">

**如果这个项目对你有帮助，请给个 ⭐️ Star**

[Gitee](https://gitee.com/lzy2018cn/xian-yu-assistant) · [GitHub](https://github.com/IAMLZY2018/-XianYuAssistant)

</div>
