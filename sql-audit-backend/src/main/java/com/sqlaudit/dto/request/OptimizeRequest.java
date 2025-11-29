package com.sqlaudit.dto.request;

import lombok.Data;
import com.sqlaudit.dto.request.DataSourceConfig;

@Data
public class OptimizeRequest {
    private DataSourceConfig dataSourceConfig;
    private String sql;
    
    // Getters and Setters (Lombok should generate these, but adding explicitly just in case)
    public DataSourceConfig getDataSourceConfig() {
        return dataSourceConfig;
    }
    
    public void setDataSourceConfig(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }
    
    public String getSql() {
        return sql;
    }
    
    public void setSql(String sql) {
        this.sql = sql;
    }
}