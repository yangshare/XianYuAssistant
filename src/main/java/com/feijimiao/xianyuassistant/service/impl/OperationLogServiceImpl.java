package com.feijimiao.xianyuassistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feijimiao.xianyuassistant.entity.XianyuOperationLog;
import com.feijimiao.xianyuassistant.mapper.XianyuOperationLogMapper;
import com.feijimiao.xianyuassistant.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 操作记录服务实现
 */
@Slf4j
@Service
public class OperationLogServiceImpl implements OperationLogService {

    // ==================== 常量定义 ====================

    /** 操作状态：成功 */
    private static final int OPERATION_STATUS_SUCCESS = 1;
    /** 操作状态：失败 */
    private static final int OPERATION_STATUS_FAILED = 0;

    /** 每批次处理数量 */
    private static final int BATCH_SIZE = 1000;
    /** 毫秒转天数的因子 */
    private static final long MS_PER_DAY = 24L * 60 * 60 * 1000;

    /** 日期格式化器 */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ==================== 常量定义结束 ====================

    @Autowired
    private XianyuOperationLogMapper operationLogMapper;

    @Autowired
    private ObjectMapper objectMapper;

    /** 日志归档目录 */
    @Value("${operation.log.archive.path:./logs/archive}")
    private String archivePath;
    
    @Async
    @Override
    public void log(XianyuOperationLog operationLog) {
        try {
            if (operationLog.getCreateTime() == null) {
                operationLog.setCreateTime(System.currentTimeMillis());
            }
            operationLogMapper.insert(operationLog);
        } catch (Exception e) {
            log.error("记录操作日志失败", e);
        }
    }
    
    @Async
    @Override
    public void log(Long accountId, String operationType, String operationDesc, Integer status) {
        XianyuOperationLog operationLog = new XianyuOperationLog();
        operationLog.setXianyuAccountId(accountId);
        operationLog.setOperationType(operationType);
        operationLog.setOperationDesc(operationDesc);
        operationLog.setOperationStatus(status);
        operationLog.setCreateTime(System.currentTimeMillis());
        log(operationLog);
    }
    
    @Async
    @Override
    public void log(Long accountId, String operationType, String operationModule,
                    String operationDesc, Integer status, String targetType, String targetId,
                    String requestParams, String responseResult, String errorMessage, Integer durationMs) {
        XianyuOperationLog operationLog = new XianyuOperationLog();
        operationLog.setXianyuAccountId(accountId);
        operationLog.setOperationType(operationType);
        operationLog.setOperationModule(operationModule);
        operationLog.setOperationDesc(operationDesc);
        operationLog.setOperationStatus(status);
        operationLog.setTargetType(targetType);
        operationLog.setTargetId(targetId);
        operationLog.setRequestParams(requestParams);
        operationLog.setResponseResult(responseResult);
        operationLog.setErrorMessage(errorMessage);
        operationLog.setDurationMs(durationMs);
        operationLog.setCreateTime(System.currentTimeMillis());
        log(operationLog);
    }
    
    @Override
    public Map<String, Object> queryLogs(Long accountId, String operationType, String operationModule,
                                         Integer operationStatus, Integer page, Integer pageSize) {
        try {
            // 计算偏移量
            int offset = (page - 1) * pageSize;
            
            // 查询数据
            List<XianyuOperationLog> logs = operationLogMapper.selectByPage(
                    accountId, operationType, operationModule, operationStatus, pageSize, offset);
            
            // 查询总数
            Integer total = operationLogMapper.countByCondition(
                    accountId, operationType, operationModule, operationStatus);
            
            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("logs", logs);
            result.put("total", total);
            result.put("page", page);
            result.put("pageSize", pageSize);
            result.put("totalPages", (int) Math.ceil((double) total / pageSize));
            
            return result;
            
        } catch (Exception e) {
            log.error("查询操作记录失败", e);
            throw new RuntimeException("查询操作记录失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public int deleteOldLogs(int days) {
        try {
            long cutoffTime = System.currentTimeMillis() - (days * MS_PER_DAY);
            int deleted = operationLogMapper.delete(
                    new LambdaQueryWrapper<XianyuOperationLog>()
                            .lt(XianyuOperationLog::getCreateTime, cutoffTime)
            );
            log.info("删除{}天前的操作记录，共删除{}条", days, deleted);
            return deleted;
        } catch (Exception e) {
            log.error("删除旧操作记录失败", e);
            return 0;
        }
    }

    /**
     * 归档旧日志到文件
     * 将指定天数之前的日志导出到文件，然后从数据库删除
     *
     * @param days 归档多少天前的日志
     * @return 归档的日志数量
     */
    @Override
    public int archiveOldLogs(int days) {
        try {
            long cutoffTime = System.currentTimeMillis() - (days * MS_PER_DAY);
            String archiveDate = LocalDateTime.now().format(DATE_FORMATTER);
            String archiveFileName = "operation_logs_" + archiveDate + ".json";

            // 创建归档目录（Files.createDirectories 会自动处理已存在的情况）
            Path archiveDir = Paths.get(archivePath);
            Files.createDirectories(archiveDir);

            Path archiveFile = archiveDir.resolve(archiveFileName);

            // 查询需要归档的日志
            List<XianyuOperationLog> logsToArchive = operationLogMapper.selectList(
                    new LambdaQueryWrapper<XianyuOperationLog>()
                            .lt(XianyuOperationLog::getCreateTime, cutoffTime)
                            .last("LIMIT " + BATCH_SIZE)
            );

            if (logsToArchive.isEmpty()) {
                log.info("没有需要归档的日志");
                return 0;
            }

            // 追加写入文件
            try (FileWriter writer = new FileWriter(archiveFile.toFile(), true)) {
                for (XianyuOperationLog logEntry : logsToArchive) {
                    String json = objectMapper.writeValueAsString(logEntry);
                    writer.write(json);
                    writer.write("\n");
                }
            }

            // 批量删除已归档的日志
            List<Long> idsToDelete = logsToArchive.stream()
                    .map(XianyuOperationLog::getId)
                    .collect(java.util.stream.Collectors.toList());

            int archivedCount = 0;
            if (!idsToDelete.isEmpty()) {
                operationLogMapper.deleteBatchIds(idsToDelete);
                archivedCount = idsToDelete.size();
            }

            log.info("成功归档{}条日志到文件: {}", archivedCount, archiveFile);
            return archivedCount;

        } catch (IOException e) {
            log.error("归档日志文件操作失败", e);
            throw new RuntimeException("归档日志失败: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("归档旧操作记录失败", e);
            throw new RuntimeException("归档日志失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取归档文件列表
     *
     * @return 归档文件信息列表
     */
    @Override
    public List<Map<String, Object>> listArchiveFiles() {
        try {
            Path archiveDir = Paths.get(archivePath);
            if (!Files.exists(archiveDir)) {
                return List.of();
            }

            // 使用 try-with-resources 确保流正确关闭
            try (Stream<Path> stream = Files.list(archiveDir)) {
                return stream
                        .filter(Files::isRegularFile)
                        .map(path -> {
                            Map<String, Object> fileInfo = new HashMap<>();
                            fileInfo.put("fileName", path.getFileName().toString());
                            try {
                                fileInfo.put("size", Files.size(path));
                                fileInfo.put("lastModified", Files.getLastModifiedTime(path).toMillis());
                            } catch (IOException e) {
                                log.warn("获取文件信息失败: {}", path, e);
                            }
                            return fileInfo;
                        })
                        .toList();
            }
        } catch (IOException e) {
            log.error("获取归档文件列表失败", e);
            throw new RuntimeException("获取归档文件列表失败: " + e.getMessage(), e);
        }
    }
}
