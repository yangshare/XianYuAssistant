# Docker 镜像自动构建发布方案

## 1. 概述

### 1.1 目标
实现提交代码到 GitHub 后，每次新增 tag 自动触发 CI/CD 流程，构建 Docker 镜像并推送到 Docker Hub 平台。

### 1.2 技术栈
- **CI/CD**: GitHub Actions
- **镜像仓库**: Docker Hub (`yangshare/xianyu-assistant`)
- **数据库**: 外部 MySQL（需配置连接信息）

---

## 2. GitHub Actions 工作流设计

### 2.1 文件结构
```
.github/
└── workflows/
    └── docker-release.yml    # Docker 镜像构建发布
```

### 2.2 触发条件
- 当推送 `v*` 格式的 tag 时触发（如 `v1.0.0`、`v1.2.3-beta`）

### 2.3 构建流程
```
Tag Push → 检出代码 → 登录 Docker Hub → 构建镜像 → 推送镜像
```

---

## 3. Docker Hub 配置

### 3.1 创建 Access Token
1. 登录 [Docker Hub](https://hub.docker.com/)
2. 进入 Account Settings → Security
3. 点击 "New Access Token"
4. 配置：
   - **名称**: `github-actions-xianyu`
   - **权限**: `Read & Write`
5. 复制生成的 Token（只显示一次）

### 3.2 配置 GitHub Secrets
在 GitHub 仓库中配置以下 Secrets：

| Secret 名称 | 值 | 说明 |
|-------------|-----|------|
| `DOCKER_USERNAME` | `yangshare` | Docker Hub 用户名 |
| `DOCKER_PASSWORD` | Access Token | 上一步生成的 Token |

**配置路径**: 仓库 → Settings → Secrets and variables → Actions → New repository secret

---

## 4. 镜像标签策略

| Tag 示例 | 生成的镜像标签 |
|----------|---------------|
| `v1.0.0` | `1.0.0`, `1.0`, `latest` |
| `v1.1.0` | `1.1.0`, `1.1`, `latest` |
| `v1.0.0-beta` | `1.0.0-beta` (无 latest) |
| `v2.0.0-rc.1` | `2.0.0-rc.1` (无 latest) |

**完整镜像地址**: `docker.io/yangshare/xianyu-assistant:<tag>`

---

## 5. 使用说明

### 5.1 发布新版本

双击运行 `release.bat`，输入版本号即可（例如 `1.0.0`）。

脚本会自动完成：
1. 检查工作目录是否干净
2. 验证版本号格式
3. 检查 tag 是否已存在
4. 创建 Git Tag (`v1.0.0`)
5. 推送 Tag 到 GitHub
6. 触发 GitHub Actions 自动构建 Docker 镜像并推送到 Docker Hub

推送 tag 后，可在 GitHub Actions 页面查看构建进度。

### 5.2 环境变量配置

镜像运行时**必须**配置以下 MySQL 连接信息：

| 变量名 | 说明 | 示例 |
|--------|------|------|
| `SPRING_DATASOURCE_URL` | MySQL 连接地址 | `jdbc:mysql://192.168.1.100:3306/xianyu_assistant?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true` |
| `SPRING_DATASOURCE_USERNAME` | 数据库用户名 | `root` |
| `SPRING_DATASOURCE_PASSWORD` | 数据库密码 | `your_password` |
| `JAVA_OPTS` | JVM 参数（可选） | `-Xms256m -Xmx512m` |
| `TZ` | 时区（可选） | `Asia/Shanghai` |

### 5.3 拉取并运行镜像
```bash
# 拉取镜像
docker pull yangshare/xianyu-assistant:latest

# 运行容器（必须配置 MySQL 连接）
docker run -d \
  --name xianyu-assistant \
  -p 12400:12400 \
  -v ./logs:/app/logs \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://your-mysql-host:3306/xianyu_assistant?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true" \
  -e SPRING_DATASOURCE_USERNAME="root" \
  -e SPRING_DATASOURCE_PASSWORD="your_password" \
  -e JAVA_OPTS="-Xms256m -Xmx512m" \
  yangshare/xianyu-assistant:latest
```

### 5.4 使用 docker-compose

项目根目录已提供 `docker-compose.yml` 文件，使用前需配置数据库密码：

1. 创建 `.env` 文件（或直接修改 docker-compose.yml）：
```bash
# 创建 .env 文件
cat > .env << EOF
SPRING_DATASOURCE_PASSWORD=your_actual_password
EOF
```

2. 启动服务：
```bash
docker-compose up -d
```

详细配置请参考项目根目录的 `docker-compose.yml` 文件。

---

## 6. 已确认配置

- [x] **Docker Hub 仓库**: `yangshare/xianyu-assistant`
- [x] **多架构支持**: 仅 `linux/amd64`
- [x] **端口统一**: 12400
- [x] **数据库**: 外部 MySQL，通过环境变量配置

---

## 7. 实施清单

- [x] 创建 `.github/workflows/docker-release.yml` 文件
- [x] 更新 `docker-compose.yml`（端口、MySQL 配置）
- [x] 创建 `release.bat` 发布脚本
- [x] 在 Docker Hub 创建 Access Token
- [x] 在 GitHub 配置 Secrets (`DOCKER_USERNAME`, `DOCKER_PASSWORD`)
- [ ] 测试：运行 release.bat 验证流程
