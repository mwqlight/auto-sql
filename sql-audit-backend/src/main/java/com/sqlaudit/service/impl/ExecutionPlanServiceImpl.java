package com.sqlaudit.service.impl;

import com.sqlaudit.dto.request.ExplainRequest;
import com.sqlaudit.dto.response.ExecutionPlan;
import com.sqlaudit.dto.response.PlanNode;
import com.sqlaudit.entity.DataSourceConfig;
import com.sqlaudit.service.ExecutionPlanService;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class ExecutionPlanServiceImpl implements ExecutionPlanService {

    @Override
    public ExecutionPlan getExecutionPlan(ExplainRequest request) {
        ExecutionPlan plan = new ExecutionPlan();
        plan.setDatabaseType(request.getDataSourceConfig().getType());
        
        try {
            // 将dto.request.DataSourceConfig转换为entity.DataSourceConfig
            com.sqlaudit.entity.DataSourceConfig entityConfig = convertToEntityConfig(request.getDataSourceConfig());
            
            String url = buildConnectionUrl(entityConfig);
            Properties props = new Properties();
            props.setProperty("user", entityConfig.getUsername());
            props.setProperty("password", entityConfig.getPassword());
            
            if (request.getDataSourceConfig().isSslEnabled()) {
                props.setProperty("ssl", "true");
            }
            
            Connection connection = DriverManager.getConnection(url, props);
            
            // 根据不同数据库类型构造EXPLAIN语句
            String explainSql = constructExplainSql(request.getSql(), entityConfig.getType());
            
            PreparedStatement preparedStatement = connection.prepareStatement(explainSql);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            List<PlanNode> nodes = new ArrayList<>();
            
            // 解析执行计划结果
            while (resultSet.next()) {
                PlanNode node = new PlanNode();
                // 这里简化处理，实际应该根据不同数据库的执行计划格式解析
                node.setId(resultSet.getString(1));
                node.setOperation("OPERATION"); // 简化处理
                node.setCost("0"); // 简化处理
                node.setRows("0"); // 简化处理
                nodes.add(node);
            }
            
            plan.setNodes(nodes);
            
            // 保存原始执行计划输出
            plan.setRawPlan("执行计划数据"); // 简化处理
            
            resultSet.close();
            preparedStatement.close();
            connection.close();
            
        } catch (SQLException e) {
            // 错误处理
            plan.setRawPlan("获取执行计划失败: " + e.getMessage());
        }
        
        return plan;
    }
    
    private com.sqlaudit.entity.DataSourceConfig convertToEntityConfig(com.sqlaudit.dto.request.DataSourceConfig dtoConfig) {
        com.sqlaudit.entity.DataSourceConfig entityConfig = new com.sqlaudit.entity.DataSourceConfig();
        entityConfig.setType(dtoConfig.getType());
        entityConfig.setHost(dtoConfig.getHost());
        entityConfig.setPort(dtoConfig.getPort());
        entityConfig.setDatabase(dtoConfig.getDatabase());
        entityConfig.setUsername(dtoConfig.getUsername());
        entityConfig.setPassword(dtoConfig.getPassword());
        return entityConfig;
    }
    
    private String buildConnectionUrl(DataSourceConfig config) {
        switch (config.getType().toLowerCase()) {
            case "mysql":
                return String.format("jdbc:mysql://%s:%d/%s", 
                    config.getHost(), config.getPort(), config.getDatabase());
            case "postgresql":
                return String.format("jdbc:postgresql://%s:%d/%s", 
                    config.getHost(), config.getPort(), config.getDatabase());
            case "oracle":
                return String.format("jdbc:oracle:thin:@%s:%d:%s", 
                    config.getHost(), config.getPort(), config.getDatabase());
            case "sqlserver":
                return String.format("jdbc:sqlserver://%s:%d;databaseName=%s", 
                    config.getHost(), config.getPort(), config.getDatabase());
            default:
                throw new IllegalArgumentException("不支持的数据库类型: " + config.getType());
        }
    }
    
    private String constructExplainSql(String sql, String databaseType) {
        switch (databaseType.toLowerCase()) {
            case "mysql":
                return "EXPLAIN " + sql;
            case "postgresql":
                return "EXPLAIN (FORMAT JSON) " + sql;
            case "oracle":
                return "EXPLAIN PLAN FOR " + sql;
            case "sqlserver":
                return "SET SHOWPLAN_XML ON " + sql;
            default:
                return "EXPLAIN " + sql;
        }
    }
}