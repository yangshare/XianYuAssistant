# 数据库脚本说明

## 目录结构

```
sql/
├── README.md                           # 本文件
├── V1.0.0__init_schema.sql             # 初始化脚本（基础表结构）
├── V1.1.0__add_order_fields.sql        # 升级：添加订单字段
├── V1.2.0__add_auto_confirm_shipment.sql  # 升级：添加自动确认发货开关
└── V1.3.0__add_operation_log.sql       # 升级：添加操作日志表
```

## 使用方式

### 方式一：新项目初始化
```bash
# 按版本号顺序执行所有脚本
sqlite3 database.db < V1.0.0__init_schema.sql
sqlite3 database.db < V1.1.0__add_order_fields.sql
sqlite3 database.db < V1.2.0__add_auto_confirm_shipment.sql
sqlite3 database.db < V1.3.0__add_operation_log.sql
```

### 方式二：应用自动执行（推荐）
在应用启动时，检查 `schema_version` 表，自动执行未执行的升级脚本：

```java
// 伪代码示例
public void checkAndUpgrade() {
    List<String> scripts = List.of(
        "V1.0.0", "V1.1.0", "V1.2.0", "V1.3.0"
    );
    for (String version : scripts) {
        if (!isVersionExecuted(version)) {
            executeScript(version);
        }
    }
}
```

## 版本管理机制

### schema_version 表
| 字段 | 类型 | 说明 |
|------|------|------|
| id | INTEGER | 主键 |
| version | VARCHAR(50) | 版本号，如 1.0.0 |
| description | VARCHAR(200) | 版本描述 |
| script_name | VARCHAR(100) | 脚本文件名 |
| execute_time | DATETIME | 执行时间 |

### 升级脚本规范

1. **命名规范**: `V{大版本}.{小版本}.{补丁}__{描述}.sql`
   - 例: `V1.1.0__add_order_fields.sql`

2. **幂等性**: 每个升级脚本必须包含版本检查
   ```sql
   INSERT INTO schema_version (version, description, script_name)
   SELECT '1.1.0', '描述', '文件名'
   WHERE NOT EXISTS (SELECT 1 FROM schema_version WHERE version = '1.1.0');
   ```

3. **索引创建**: 使用 `IF NOT EXISTS` 确保可重复执行
   ```sql
   CREATE INDEX IF NOT EXISTS idx_name ON table(column);
   ```

## 表结构概览

| 表名 | 说明 |
|------|------|
| schema_version | 版本管理表 |
| xianyu_account | 某鱼账号表 |
| xianyu_cookie | 某鱼Cookie表 |
| xianyu_goods | 某鱼商品信息表 |
| xianyu_chat_message | 某鱼聊天消息表 |
| xianyu_goods_config | 商品配置表 |
| xianyu_goods_auto_delivery_config | 商品自动发货配置表 |
| xianyu_goods_auto_delivery_record | 商品自动发货记录表 |
| xianyu_goods_auto_reply_config | 商品自动回复配置表 |
| xianyu_goods_auto_reply_record | 商品自动回复记录表 |
| xianyu_operation_log | 操作日志表 |

## 新增升级脚本步骤

1. 创建新文件：`V{下一版本号}__{描述}.sql`
2. 添加版本记录（幂等方式）
3. 编写 DDL 语句（确保幂等）
4. 更新本 README 的目录结构

```sql
-- 模板
-- ============================================================
-- 升级脚本: {描述}
-- 版本: V{版本号}
-- 日期: {日期}
-- ============================================================

INSERT INTO schema_version (version, description, script_name)
SELECT '{版本号}', '{描述}', '{文件名}'
WHERE NOT EXISTS (SELECT 1 FROM schema_version WHERE version = '{版本号}');

-- 你的 DDL 语句
```

## 注意事项

1. **SQLite 限制**
   - 不支持 `COMMENT ON` 语法，注释写在脚本中
   - `ALTER TABLE ADD COLUMN` 不支持 `IF NOT EXISTS`，需注意幂等
   - 触发器分隔符统一使用 `$$`

2. **升级顺序**
   - 必须按版本号顺序执行
   - 不可跳过中间版本

3. **回滚**
   - SQLite 不支持完整的 DDL 回滚
   - 建议升级前备份数据库
