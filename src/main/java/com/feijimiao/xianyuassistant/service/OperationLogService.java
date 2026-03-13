package com.feijimiao.xianyuassistant.service;

import com.feijimiao.xianyuassistant.entity.XianyuOperationLog;

import java.util.List;
import java.util.Map;

/**
 * 操作记录服务
 */
public interface OperationLogService {
    
    /**
     * 记录操作日志
     */
    void log(XianyuOperationLog operationLog);
    
    /**
     * 记录操作日志（简化版）
     */
    void log(Long accountId, String operationType, String operationDesc, Integer status);
    
    /**
     * 记录操作日志（完整版）
     */
    void log(Long accountId, String operationType, String operationModule, 
             String operationDesc, Integer status, String targetType, String targetId,
             String requestParams, String responseResult, String errorMessage, Integer durationMs);
    
    /**
     * 分页查询操作记录
     */
    Map<String, Object> queryLogs(Long accountId, String operationType, String operationModule,
                                   Integer operationStatus, Integer page, Integer pageSize);
    
    /**
     * 删除指定天数之前的日志
     */
    int deleteOldLogs(int days);

    /**
     * 归档指定天数之前的日志到文件
     *
     * @param days 归档多少天前的日志
     * @return 归档的日志数量
     */
    int archiveOldLogs(int days);

    /**
     * 获取归档文件列表
     *
     * @return 归档文件信息列表
     */
    List<Map<String, Object>> listArchiveFiles();
}
