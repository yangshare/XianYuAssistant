package com.feijimiao.xianyuassistant.controller;

import com.feijimiao.xianyuassistant.common.ResultObject;
import com.feijimiao.xianyuassistant.entity.XianyuGoodsInfo;
import com.feijimiao.xianyuassistant.mapper.XianyuAccountMapper;
import com.feijimiao.xianyuassistant.mapper.XianyuGoodsInfoMapper;
import com.feijimiao.xianyuassistant.controller.dto.DashboardStatsRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

/**
 * 首页仪表板控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    // ==================== 常量定义 ====================

    /** 商品状态：在售 */
    private static final int GOODS_STATUS_SELLING = 0;
    /** 商品状态：已下架 */
    private static final int GOODS_STATUS_OFF_SHELF = 1;
    /** 商品状态：已售出 */
    private static final int GOODS_STATUS_SOLD = 2;

    // ==================== 常量定义结束 ====================

    @Autowired
    private XianyuAccountMapper accountMapper;
    
    @Autowired
    private XianyuGoodsInfoMapper goodsMapper;

    /**
     * 获取首页统计数据
     * 使用缓存，缓存时间5分钟，减少数据库压力
     */
    @PostMapping("/stats")
    @Cacheable(value = "dashboardStats", unless = "#result == null or #result.code != 200")
    public ResultObject<DashboardStatsRespDTO> getDashboardStats() {
        try {
            log.info("获取首页统计数据");
            
            // 获取账号总数
            int accountCount = accountMapper.selectCount(null).intValue();
            
            // 获取商品总数
            int itemCount = goodsMapper.selectCount(null).intValue();
            
            // 获取在售商品数
            int sellingItemCount = goodsMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<XianyuGoodsInfo>()
                    .eq("status", GOODS_STATUS_SELLING)
            ).intValue();

            // 获取已下架商品数
            int offShelfItemCount = goodsMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<XianyuGoodsInfo>()
                    .eq("status", GOODS_STATUS_OFF_SHELF)
            ).intValue();

            // 获取已售出商品数
            int soldItemCount = goodsMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<XianyuGoodsInfo>()
                    .eq("status", GOODS_STATUS_SOLD)
            ).intValue();
            
            // 构造响应数据
            DashboardStatsRespDTO respDTO = new DashboardStatsRespDTO();
            respDTO.setAccountCount(accountCount);
            respDTO.setItemCount(itemCount);
            respDTO.setSellingItemCount(sellingItemCount);
            respDTO.setOffShelfItemCount(offShelfItemCount);
            respDTO.setSoldItemCount(soldItemCount);
            
            return ResultObject.success(respDTO);
        } catch (Exception e) {
            log.error("获取首页统计数据失败", e);
            return ResultObject.failed("获取首页统计数据失败: " + e.getMessage());
        }
    }
}