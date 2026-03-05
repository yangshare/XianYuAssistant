package com.feijimiao.xianyuassistant.controller.dto;

import lombok.Data;

/**
 * 删除商品请求DTO
 */
@Data
public class DeleteItemReqDTO {
    
    /**
     * 某鱼账号ID
     */
    private Long xianyuAccountId;
    
    /**
     * 某鱼商品ID
     */
    private String xyGoodsId;
}