-- ============================================================
-- 升级脚本: 添加操作日志表
-- 版本: V1.3.0
-- 日期: 2025-03-05
-- ============================================================

-- 操作记录表
CREATE TABLE IF NOT EXISTS xianyu_operation_log (
    id               INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    xianyu_account_id BIGINT      NOT NULL,                    -- 账号ID
    operation_type   VARCHAR(50)  NOT NULL,                    -- 操作类型
    operation_module VARCHAR(50),                              -- 操作模块
    operation_desc   VARCHAR(500),                             -- 操作描述
    operation_status INT          DEFAULT 1,                   -- 操作状态 1:成功 0:失败 2:部分成功
    target_type      VARCHAR(50),                              -- 目标类型
    target_id        VARCHAR(200),                             -- 目标ID
    request_params   TEXT,                                     -- 请求参数（JSON）
    response_result  TEXT,                                     -- 响应结果（JSON）
    error_message    TEXT,                                     -- 错误信息
    ip_address       VARCHAR(50),                              -- IP地址
    user_agent       VARCHAR(500),                             -- 浏览器UA
    duration_ms      INT,                                      -- 操作耗时（毫秒）
    create_time      BIGINT       NOT NULL,                    -- 创建时间（毫秒时间戳）
    FOREIGN KEY (xianyu_account_id) REFERENCES xianyu_account(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_operation_log_account_id  ON xianyu_operation_log(xianyu_account_id);
CREATE INDEX idx_operation_log_type        ON xianyu_operation_log(operation_type);
CREATE INDEX idx_operation_log_module      ON xianyu_operation_log(operation_module);
CREATE INDEX idx_operation_log_status      ON xianyu_operation_log(operation_status);
CREATE INDEX idx_operation_log_create_time ON xianyu_operation_log(create_time);
CREATE INDEX idx_operation_log_target      ON xianyu_operation_log(target_type, target_id);

-- 操作类型说明:
-- LOGIN: 扫码登录
-- LOGOUT: 退出登录
-- WEBSOCKET_CONNECT: WebSocket连接
-- WEBSOCKET_DISCONNECT: WebSocket断开
-- SEND_MESSAGE: 发送消息
-- RECEIVE_MESSAGE: 接收消息
-- AUTO_DELIVERY: 自动发货
-- AUTO_REPLY: 自动回复
-- CONFIRM_SHIPMENT: 确认收货
-- TOKEN_REFRESH: Token刷新
-- COOKIE_UPDATE: Cookie更新
-- GOODS_SYNC: 商品同步
-- MESSAGE_SYNC: 消息同步
