-- ============================================================
-- 修复历史数据中错误的买家名称
-- 版本: V1.4.0
-- 说明: 将 content_type != 14 的消息的 sender_user_name 字段清空
--       同时清空对应订单记录中的 buyer_user_name 字段
-- ============================================================

-- 1. 清空非 contentType=14 消息的买家名称
UPDATE xianyu_chat_message
SET sender_user_name = NULL
WHERE content_type IS NOT NULL
  AND content_type != 14
  AND sender_user_name IS NOT NULL;

-- 2. 清空订单记录表中对应的买家名称
--    通过 pnm_id 关联消息表，清空 content_type != 14 的订单记录的买家名称
UPDATE xianyu_goods_auto_delivery_record r
INNER JOIN xianyu_chat_message m ON r.pnm_id = m.pnm_id
SET r.buyer_user_name = NULL
WHERE m.content_type IS NOT NULL
  AND m.content_type != 14
  AND r.buyer_user_name IS NOT NULL;
