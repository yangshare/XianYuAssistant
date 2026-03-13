package com.feijimiao.xianyuassistant.controller;

import com.feijimiao.xianyuassistant.common.ResultObject;
import com.feijimiao.xianyuassistant.service.OperationLogService;
import com.feijimiao.xianyuassistant.utils.AdminAuthUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 操作记录控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/operation-log")
@CrossOrigin(origins = "*")
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 查询操作记录
     * 需要管理员权限
     */
    @PostMapping("/query")
    public ResultObject<Map<String, Object>> queryLogs(@RequestBody QueryLogsReqDTO reqDTO,
                                                       @RequestHeader java.util.Map<String, String> headers) {
        try {
            log.info("查询操作记录: accountId={}, type={}, module={}, status={}, page={}, pageSize={}",
                    reqDTO.getAccountId(), reqDTO.getOperationType(), reqDTO.getOperationModule(),
                    reqDTO.getOperationStatus(), reqDTO.getPage(), reqDTO.getPageSize());

            // 权限验证
            if (!AdminAuthUtils.verifyPermission(headers)) {
                log.warn("查询操作记录权限验证失败");
                return ResultObject.failed("权限不足，需要管理员权限");
            }

            if (reqDTO.getAccountId() == null) {
                return ResultObject.failed("账号ID不能为空");
            }
            
            // 设置默认值
            if (reqDTO.getPage() == null || reqDTO.getPage() < 1) {
                reqDTO.setPage(1);
            }
            if (reqDTO.getPageSize() == null || reqDTO.getPageSize() < 1) {
                reqDTO.setPageSize(20);
            }
            
            Map<String, Object> result = operationLogService.queryLogs(
                    reqDTO.getAccountId(),
                    reqDTO.getOperationType(),
                    reqDTO.getOperationModule(),
                    reqDTO.getOperationStatus(),
                    reqDTO.getPage(),
                    reqDTO.getPageSize()
            );
            
            return ResultObject.success(result);
            
        } catch (Exception e) {
            log.error("查询操作记录失败", e);
            return ResultObject.failed("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 删除旧日志
     * 需要管理员权限
     */
    @PostMapping("/deleteOld")
    public ResultObject<Integer> deleteOldLogs(@RequestBody DeleteOldLogsReqDTO reqDTO,
                                                @RequestHeader java.util.Map<String, String> headers) {
        try {
            log.info("删除旧操作记录: days={}", reqDTO.getDays());

            // 权限验证
            if (!AdminAuthUtils.verifyPermission(headers)) {
                log.warn("删除旧操作记录权限验证失败");
                return ResultObject.failed("权限不足，需要管理员权限");
            }

            if (reqDTO.getDays() == null || reqDTO.getDays() < 1) {
                return ResultObject.failed("天数必须大于0");
            }
            
            int deleted = operationLogService.deleteOldLogs(reqDTO.getDays());
            
            return ResultObject.success(deleted);
            
        } catch (Exception e) {
            log.error("删除旧操作记录失败", e);
            return ResultObject.failed("删除失败: " + e.getMessage());
        }
    }
    
    /**
     * 查询操作记录请求DTO
     */
    @Data
    public static class QueryLogsReqDTO {
        private Long accountId;           // 账号ID（必填）
        private String operationType;     // 操作类型（可选）
        private String operationModule;   // 操作模块（可选）
        private Integer operationStatus;  // 操作状态（可选）
        private Integer page;             // 页码（默认1）
        private Integer pageSize;         // 每页数量（默认20）
    }
    
    /**
     * 删除旧日志请求DTO
     */
    @Data
    public static class DeleteOldLogsReqDTO {
        private Integer days;  // 删除多少天之前的日志
    }
}
