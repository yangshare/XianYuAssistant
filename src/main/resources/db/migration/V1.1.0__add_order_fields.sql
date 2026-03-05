-- ============================================================
-- 升级脚本: 为自动发货记录表添加订单相关字段
-- 版本: V1.1.0
-- 日期: 2025-01-15
-- ============================================================

ALTER TABLE xianyu_goods_auto_delivery_record ADD COLUMN order_id    VARCHAR(100);
ALTER TABLE xianyu_goods_auto_delivery_record ADD COLUMN order_state TINYINT DEFAULT 0;

CREATE INDEX idx_auto_delivery_record_order_id    ON xianyu_goods_auto_delivery_record(order_id);
CREATE INDEX idx_auto_delivery_record_order_state ON xianyu_goods_auto_delivery_record(order_state);
