-- ============================================================
-- XianYuAssistant 数据库完整结构（仅供参考）
-- 实际使用请执行版本化脚本 V1.x.x__*.sql
-- ============================================================

-- 版本管理表
CREATE TABLE IF NOT EXISTS schema_version (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    version VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200),
    script_name VARCHAR(100),
    execute_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 某鱼账号表
CREATE TABLE IF NOT EXISTS xianyu_account (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    account_note VARCHAR(100),
    unb VARCHAR(100),
    status TINYINT DEFAULT 1,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 某鱼Cookie表
CREATE TABLE IF NOT EXISTS xianyu_cookie (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    xianyu_account_id BIGINT NOT NULL,
    cookie_text TEXT,
    m_h5_tk VARCHAR(500),
    cookie_status TINYINT DEFAULT 1,
    expire_time DATETIME,
    websocket_token TEXT,
    token_expire_time INTEGER,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
);

-- 某鱼商品信息表
CREATE TABLE IF NOT EXISTS xianyu_goods (
    id BIGINT PRIMARY KEY,
    xy_good_id VARCHAR(100) NOT NULL,
    xianyu_account_id BIGINT,
    title VARCHAR(500),
    cover_pic TEXT,
    info_pic TEXT,
    detail_info TEXT,
    detail_url TEXT,
    sold_price VARCHAR(50),
    status TINYINT DEFAULT 0,
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
);

-- 某鱼聊天消息表
CREATE TABLE IF NOT EXISTS xianyu_chat_message (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    xianyu_account_id BIGINT NOT NULL,
    lwp VARCHAR(50),
    pnm_id VARCHAR(100) NOT NULL,
    s_id VARCHAR(100),
    content_type INTEGER,
    msg_content TEXT,
    sender_user_name VARCHAR(200),
    sender_user_id VARCHAR(100),
    sender_app_v VARCHAR(50),
    sender_os_type VARCHAR(20),
    reminder_url TEXT,
    xy_goods_id VARCHAR(100),
    complete_msg TEXT NOT NULL,
    message_time BIGINT,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
);

-- 商品配置表
CREATE TABLE IF NOT EXISTS xianyu_goods_config (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    xianyu_account_id BIGINT NOT NULL,
    xianyu_goods_id BIGINT,
    xy_goods_id VARCHAR(100) NOT NULL,
    xianyu_auto_delivery_on TINYINT DEFAULT 0,
    xianyu_auto_reply_on TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
);

-- 商品自动发货配置表
CREATE TABLE IF NOT EXISTS xianyu_goods_auto_delivery_config (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    xianyu_account_id BIGINT NOT NULL,
    xianyu_goods_id BIGINT,
    xy_goods_id VARCHAR(100) NOT NULL,
    type TINYINT DEFAULT 1,
    auto_delivery_content TEXT,
    auto_confirm_shipment TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
);

-- 商品自动发货记录表
CREATE TABLE IF NOT EXISTS xianyu_goods_auto_delivery_record (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    xianyu_account_id BIGINT NOT NULL,
    xianyu_goods_id BIGINT,
    xy_goods_id VARCHAR(100) NOT NULL,
    pnm_id VARCHAR(100) NOT NULL,
    buyer_user_id VARCHAR(100),
    buyer_user_name VARCHAR(100),
    content TEXT,
    state TINYINT DEFAULT 0,
    order_id VARCHAR(100),
    order_state TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
);

-- 商品自动回复配置表
CREATE TABLE IF NOT EXISTS xianyu_goods_auto_reply_config (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    xianyu_account_id BIGINT NOT NULL,
    xianyu_goods_id BIGINT,
    xy_goods_id VARCHAR(100) NOT NULL,
    keyword TEXT,
    reply_content TEXT,
    match_type TINYINT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
);

-- 商品自动回复记录表
CREATE TABLE IF NOT EXISTS xianyu_goods_auto_reply_record (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    xianyu_account_id BIGINT NOT NULL,
    xianyu_goods_id BIGINT,
    xy_goods_id VARCHAR(100) NOT NULL,
    buyer_message TEXT,
    reply_content TEXT,
    matched_keyword VARCHAR(200),
    state TINYINT DEFAULT 0,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
);

-- 操作日志表
CREATE TABLE IF NOT EXISTS xianyu_operation_log (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    xianyu_account_id INTEGER NOT NULL,
    operation_type VARCHAR(50) NOT NULL,
    operation_module VARCHAR(50),
    operation_desc VARCHAR(500),
    operation_status INTEGER DEFAULT 1,
    target_type VARCHAR(50),
    target_id VARCHAR(200),
    request_params TEXT,
    response_result TEXT,
    error_message TEXT,
    ip_address VARCHAR(50),
    user_agent VARCHAR(500),
    duration_ms INTEGER,
    create_time INTEGER NOT NULL,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
);
