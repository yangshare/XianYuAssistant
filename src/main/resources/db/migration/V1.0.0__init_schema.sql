-- ============================================================
-- XianYuAssistant 数据库初始化脚本
-- 版本: V1.0.0
-- ============================================================

-- ------------------------------------------------------------
-- 1. 某鱼账号表
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS xianyu_account (
    id          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    account_note VARCHAR(100),
    unb         VARCHAR(100),
    status      TINYINT      DEFAULT 1,
    created_time DATETIME    DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_account_unb ON xianyu_account(unb);

-- ------------------------------------------------------------
-- 2. 某鱼Cookie表
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS xianyu_cookie (
    id                 INT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    xianyu_account_id  BIGINT   NOT NULL,
    cookie_text        TEXT,
    m_h5_tk            VARCHAR(500),
    cookie_status      TINYINT  DEFAULT 1,
    expire_time        DATETIME,
    websocket_token    TEXT,
    token_expire_time  BIGINT,
    created_time       DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_cookie_account_id    ON xianyu_cookie(xianyu_account_id);
CREATE INDEX idx_cookie_status        ON xianyu_cookie(cookie_status);
CREATE INDEX idx_token_expire_time    ON xianyu_cookie(token_expire_time);

-- ------------------------------------------------------------
-- 3. 某鱼商品信息表
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS xianyu_goods (
    id                BIGINT       NOT NULL PRIMARY KEY,
    xy_good_id        VARCHAR(100) NOT NULL,
    xianyu_account_id BIGINT,
    title             VARCHAR(500),
    cover_pic         TEXT,
    info_pic          TEXT,
    detail_info       TEXT,
    detail_url        TEXT,
    sold_price        VARCHAR(50),
    status            TINYINT      DEFAULT 0,
    created_time      DATETIME     DEFAULT CURRENT_TIMESTAMP,
    updated_time      DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE UNIQUE INDEX idx_goods_xy_good_id ON xianyu_goods(xy_good_id);
CREATE INDEX        idx_goods_status     ON xianyu_goods(status);
CREATE INDEX        idx_goods_account_id ON xianyu_goods(xianyu_account_id);

-- ------------------------------------------------------------
-- 4. 某鱼聊天消息表
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS xianyu_chat_message (
    id                 INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    xianyu_account_id  BIGINT       NOT NULL,
    lwp                VARCHAR(50),
    pnm_id             VARCHAR(100) NOT NULL,
    s_id               VARCHAR(100),
    content_type       INT,
    msg_content        TEXT,
    sender_user_name   VARCHAR(200),
    sender_user_id     VARCHAR(100),
    sender_app_v       VARCHAR(50),
    sender_os_type     VARCHAR(20),
    reminder_url       TEXT,
    xy_goods_id        VARCHAR(100),
    complete_msg       TEXT         NOT NULL,
    message_time       BIGINT,
    create_time        DATETIME     DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX        idx_chat_message_account_id     ON xianyu_chat_message(xianyu_account_id);
CREATE INDEX        idx_chat_message_pnm_id         ON xianyu_chat_message(pnm_id);
CREATE INDEX        idx_chat_message_s_id           ON xianyu_chat_message(s_id);
CREATE INDEX        idx_chat_message_sender_user_id ON xianyu_chat_message(sender_user_id);
CREATE INDEX        idx_chat_message_content_type   ON xianyu_chat_message(content_type);
CREATE INDEX        idx_chat_message_time           ON xianyu_chat_message(message_time);
CREATE INDEX        idx_chat_message_goods_id       ON xianyu_chat_message(xy_goods_id);
CREATE UNIQUE INDEX idx_chat_message_unique         ON xianyu_chat_message(xianyu_account_id, pnm_id);

-- ------------------------------------------------------------
-- 5. 商品配置表
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS xianyu_goods_config (
    id                        INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    xianyu_account_id         BIGINT       NOT NULL,
    xianyu_goods_id           BIGINT,
    xy_goods_id               VARCHAR(100) NOT NULL,
    xianyu_auto_delivery_on   TINYINT      DEFAULT 0,
    xianyu_auto_reply_on      TINYINT      DEFAULT 0,
    create_time               DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_time               DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX        idx_goods_config_account_id  ON xianyu_goods_config(xianyu_account_id);
CREATE INDEX        idx_goods_config_xy_goods_id ON xianyu_goods_config(xy_goods_id);
CREATE UNIQUE INDEX idx_goods_config_unique       ON xianyu_goods_config(xianyu_account_id, xy_goods_id);

-- ------------------------------------------------------------
-- 6. 商品自动发货配置表
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS xianyu_goods_auto_delivery_config (
    id                    INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    xianyu_account_id     BIGINT       NOT NULL,
    xianyu_goods_id       BIGINT,
    xy_goods_id           VARCHAR(100) NOT NULL,
    type                  TINYINT      DEFAULT 1,
    auto_delivery_content TEXT,
    create_time           DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_time           DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX        idx_auto_delivery_config_account_id  ON xianyu_goods_auto_delivery_config(xianyu_account_id);
CREATE INDEX        idx_auto_delivery_config_xy_goods_id ON xianyu_goods_auto_delivery_config(xy_goods_id);
CREATE UNIQUE INDEX idx_auto_delivery_config_unique       ON xianyu_goods_auto_delivery_config(xianyu_account_id, xy_goods_id);

-- ------------------------------------------------------------
-- 7. 商品自动发货记录表
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS xianyu_goods_auto_delivery_record (
    id                INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    xianyu_account_id BIGINT       NOT NULL,
    xianyu_goods_id   BIGINT,
    xy_goods_id       VARCHAR(100) NOT NULL,
    pnm_id            VARCHAR(100) NOT NULL,
    buyer_user_id     VARCHAR(100),
    buyer_user_name   VARCHAR(100),
    content           TEXT,
    state             TINYINT      DEFAULT 0,
    create_time       DATETIME     DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX        idx_auto_delivery_record_account_id  ON xianyu_goods_auto_delivery_record(xianyu_account_id);
CREATE INDEX        idx_auto_delivery_record_xy_goods_id ON xianyu_goods_auto_delivery_record(xy_goods_id);
CREATE INDEX        idx_auto_delivery_record_state       ON xianyu_goods_auto_delivery_record(state);
CREATE INDEX        idx_auto_delivery_record_create_time ON xianyu_goods_auto_delivery_record(create_time);
CREATE INDEX        idx_auto_delivery_record_pnm_id      ON xianyu_goods_auto_delivery_record(pnm_id);
CREATE UNIQUE INDEX idx_auto_delivery_record_unique      ON xianyu_goods_auto_delivery_record(xianyu_account_id, pnm_id);

-- ------------------------------------------------------------
-- 8. 商品自动回复配置表
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS xianyu_goods_auto_reply_config (
    id                INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    xianyu_account_id BIGINT       NOT NULL,
    xianyu_goods_id   BIGINT,
    xy_goods_id       VARCHAR(100) NOT NULL,
    keyword           TEXT,
    reply_content     TEXT,
    match_type        TINYINT      DEFAULT 1,
    create_time       DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_time       DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_auto_reply_config_account_id  ON xianyu_goods_auto_reply_config(xianyu_account_id);
CREATE INDEX idx_auto_reply_config_xy_goods_id ON xianyu_goods_auto_reply_config(xy_goods_id);

-- ------------------------------------------------------------
-- 9. 商品自动回复记录表
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS xianyu_goods_auto_reply_record (
    id                INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    xianyu_account_id BIGINT       NOT NULL,
    xianyu_goods_id   BIGINT,
    xy_goods_id       VARCHAR(100) NOT NULL,
    buyer_message     TEXT,
    reply_content     TEXT,
    matched_keyword   VARCHAR(200),
    state             TINYINT      DEFAULT 0,
    create_time       DATETIME     DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_auto_reply_record_account_id  ON xianyu_goods_auto_reply_record(xianyu_account_id);
CREATE INDEX idx_auto_reply_record_xy_goods_id ON xianyu_goods_auto_reply_record(xy_goods_id);
CREATE INDEX idx_auto_reply_record_state       ON xianyu_goods_auto_reply_record(state);
CREATE INDEX idx_auto_reply_record_create_time ON xianyu_goods_auto_reply_record(create_time);
