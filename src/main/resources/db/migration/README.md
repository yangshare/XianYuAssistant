# 数据库迁移脚本

## 目录结构

```
db/migration/
├── README.md                              # 本文件
├── V1.0.0__init_schema.sql                # 初始化脚本（基础表结构）
├── V1.1.0__add_order_fields.sql           # 升级：添加订单字段
├── V1.2.0__add_auto_confirm_shipment.sql  # 升级：添加自动确认发货开关
└── V1.3.0__add_operation_log.sql          # 升级：添加操作日志表
```

## 工作原理

项目使用 **Flyway** 进行数据库版本管理。应用启动时，Flyway 会自动：
1. 检查 `schema_version` 表（Flyway 自动创建）
2. 对比已执行的版本和待执行的脚本
3. 按版本号顺序执行未执行的迁移脚本

## 命名规范

```
V{版本号}__{描述}.sql
```

- `V` - 大写，固定前缀
- `{版本号}` - 如 `1.0.0`、`1.1.0`，用双下划线分隔
- `{描述}` - 简短描述，用下划线连接单词
- `.sql` - 文件扩展名

**示例：**
- `V1.0.0__init_schema.sql`
- `V1.1.0__add_user_table.sql`
- `V2.0.0__refactor_order_system.sql`

## 新增迁移脚本

1. 确定新版本号（基于上一版本递增）
2. 在 `db/migration/` 目录创建新文件
3. 编写 SQL 语句（确保幂等性）

```sql
-- 示例：V1.4.0__add_new_field.sql
-- 添加新字段（幂等方式）
ALTER TABLE some_table ADD COLUMN new_field VARCHAR(100);

-- 创建索引（幂等方式）
CREATE INDEX IF NOT EXISTS idx_some_table_new_field
ON some_table(new_field);
```

## 注意事项

1. **版本号不能重复** - Flyway 会校验版本唯一性
2. **已执行的脚本不能修改** - 会导致校验失败
3. **SQLite 限制**：
   - 不支持 `COMMENT ON` 语法
   - `ALTER TABLE ADD COLUMN` 不支持 `IF NOT EXISTS`
   - 触发器分隔符统一使用 `$$`

## 常用命令

```bash
# 查看 Flyway 状态（需要配置 Maven Flyway 插件）
mvn flyway:info

# 手动执行迁移
mvn flyway:migrate

# 清空数据库（危险！仅开发环境）
mvn flyway:clean
```
