package com.sqlaudit.dto.request;

import lombok.Data;

@Data
public class ExecuteRequest {
    private Long dataSourceId;
    private String sql;
    private int maxRows = 1000;
    private int timeout = 30000;
    
    // Getters and Setters (Lombok should generate these, but adding explicitly just in case)
    public Long getDataSourceId() {
        return dataSourceId;
    }
    
    public void setDataSourceId(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
    
    public String getSql() {
        return sql;
    }
    
    public void setSql(String sql) {
        this.sql = sql;
    }
    
    public int getMaxRows() {
        return maxRows;
    }
    
    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}