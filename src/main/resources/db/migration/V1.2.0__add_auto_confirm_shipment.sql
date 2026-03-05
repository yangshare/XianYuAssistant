-- ============================================================
-- 升级脚本: 为自动发货配置表添加自动确认发货开关
-- 版本: V1.2.0
-- 日期: 2025-12-23
-- ============================================================

-- 添加 auto_confirm_shipment 字段 (0-关闭，1-开启)
ALTER TABLE xianyu_goods_auto_delivery_config ADD COLUMN auto_confirm_shipment TINYINT DEFAULT 0;
