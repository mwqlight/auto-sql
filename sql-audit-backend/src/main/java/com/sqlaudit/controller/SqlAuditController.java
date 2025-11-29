package com.sqlaudit.controller;

import com.sqlaudit.dto.request.*;
import com.sqlaudit.dto.response.*;
import com.sqlaudit.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // 允许跨域访问
public class SqlAuditController {

    @Autowired
    private DataSourceService dataSourceService;
    
    @Autowired
    private SqlExecutorService sqlExecutorService;
    
    @Autowired
    private SqlAuditorService sqlAuditorService;
    
    @Autowired
    private OptimizationEngineService optimizationEngineService;
    
    @Autowired
    private ExecutionPlanService executionPlanService;

    // 1. 数据源管理
    @PostMapping("/datasources/test")
    public ApiResponse<DataSourceTestResult> testConnection(@RequestBody DataSourceConfig config) {
        // 将DTO转换为实体对象
        com.sqlaudit.entity.DataSourceConfig entityConfig = new com.sqlaudit.entity.DataSourceConfig();
        entityConfig.setType(config.getType());
        entityConfig.setHost(config.getHost());
        entityConfig.setPort(config.getPort());
        entityConfig.setDatabase(config.getDatabase());
        entityConfig.setUsername(config.getUsername());
        entityConfig.setPassword(config.getPassword());
        
        DataSourceTestResult result = new DataSourceTestResult();
        result.setSuccess(dataSourceService.testConnection(entityConfig));
        result.setMessage(result.isSuccess() ? "连接成功" : "连接失败");
        return ApiResponse.success(result);
    }
    
    // 2. SQL执行
    @PostMapping("/execute")
    public ApiResponse<QueryResult> executeSql(@RequestBody ExecuteRequest request) {
        QueryResult result = sqlExecutorService.executeSql(request);
        return ApiResponse.success(result);
    }
    
    // 3. SQL审计
    @PostMapping("/audit")
    public ApiResponse<AuditReport> auditSql(@RequestBody ExecuteRequest request) {
        AuditReport report = sqlAuditorService.auditSql(request);
        return ApiResponse.success(report);
    }
    
    // 4. 优化建议
    @PostMapping("/optimize")
    public ApiResponse<OptimizationResponse> optimizeSql(@RequestBody OptimizeRequest request) {
        OptimizationResponse response = optimizationEngineService.optimizeSql(request);
        return ApiResponse.success(response);
    }
    
    // 5. 执行计划
    @PostMapping("/explain")
    public ApiResponse<ExecutionPlan> getExecutionPlan(@RequestBody ExplainRequest request) {
        ExecutionPlan plan = executionPlanService.getExecutionPlan(request);
        return ApiResponse.success(plan);
    }
}