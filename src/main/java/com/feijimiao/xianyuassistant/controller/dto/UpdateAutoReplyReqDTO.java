package com.feijimiao.xianyuassistant.controller.dto;

import lombok.Data;

/**
 * 更新自动回复状态请求DTO
 */
@Data
public class UpdateAutoReplyReqDTO {
    
    /**
     * 某鱼账号ID
     */
    private Long xianyuAccountId;
    
    /**
     * 某鱼商品ID
     */
    private String xyGoodsId;
    
    /**
     * 自动回复开关：1-开启，0-关闭
     */
    private Integer xianyuAutoReplyOn;
}