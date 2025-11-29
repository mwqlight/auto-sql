package com.sqlaudit.dto.response;

import lombok.Data;

@Data
public class IndexSuggestion {
    private String tableName;
    private String[] columns;
    private String indexName;
    private String createStatement;
    private String reason;
    
    // Getters and Setters (Lombok should generate these, but adding explicitly just in case)
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String[] getColumns() {
        return columns;
    }
    
    public void setColumns(String[] columns) {
        this.columns = columns;
    }
    
    public String getIndexName() {
        return indexName;
    }
    
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }
    
    public String getCreateStatement() {
        return createStatement;
    }
    
    public void setCreateStatement(String createStatement) {
        this.createStatement = createStatement;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
}