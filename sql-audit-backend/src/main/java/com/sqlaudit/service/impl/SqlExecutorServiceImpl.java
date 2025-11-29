package com.sqlaudit.service.impl;

import com.sqlaudit.dto.request.ExecuteRequest;
import com.sqlaudit.dto.response.QueryResult;
import com.sqlaudit.entity.DataSourceConfig;
import com.sqlaudit.service.SqlExecutorService;
import com.sqlaudit.service.DataSourceService;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Service
public class SqlExecutorServiceImpl implements SqlExecutorService {

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public QueryResult executeSql(ExecuteRequest request) {
        QueryResult result = new QueryResult();
        long startTime = System.currentTimeMillis();
        
        try {
            // 获取数据源配置
            DataSourceConfig dataSourceConfig = dataSourceService.getDataSourceById(request.getDataSourceId());
            if (dataSourceConfig == null) {
                result.setSuccess(false);
                result.setErrorMessage("数据源配置不存在");
                return result;
            }
            
            // 解析SQL以确定类型
            Statement statement = CCJSqlParserUtil.parse(request.getSql());
            boolean isSelect = statement instanceof Select;
            
            // 对于非SELECT语句进行安全检查
            if (!isSelect) {
                // 检查是否为危险操作
                String upperSql = request.getSql().trim().toUpperCase();
                if (upperSql.startsWith("DROP") || upperSql.startsWith("TRUNCATE")) {
                    result.setSuccess(false);
                    result.setErrorMessage("禁止执行DROP/TRUNCATE操作");
                    return result;
                }
            }
            
            String url = buildConnectionUrl(dataSourceConfig);
            Properties props = new Properties();
            props.setProperty("user", dataSourceConfig.getUsername());
            props.setProperty("password", dataSourceConfig.getPassword());
            
            Connection connection = DriverManager.getConnection(url, props);
            
            // 设置查询超时
            connection.setNetworkTimeout(null, request.getTimeout());
            
            PreparedStatement preparedStatement = connection.prepareStatement(request.getSql());
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // 获取列信息
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            List<Map<String, Object>> data = new ArrayList<>();
            int rowCount = 0;
            
            // 限制返回行数
            while (resultSet.next() && rowCount < request.getMaxRows()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    row.put(columnName, value);
                }
                data.add(row);
                rowCount++;
            }
            
            resultSet.close();
            preparedStatement.close();
            connection.close();
            
            result.setData(data);
            result.setRowCount(rowCount);
            result.setSuccess(true);
            result.setExecutionTime(System.currentTimeMillis() - startTime);
            
        } catch (JSQLParserException e) {
            result.setSuccess(false);
            result.setErrorMessage("SQL语法错误: " + e.getMessage());
        } catch (SQLException e) {
            result.setSuccess(false);
            result.setErrorMessage("数据库执行错误: " + e.getMessage());
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage("执行异常: " + e.getMessage());
        }
        
        return result;
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
}