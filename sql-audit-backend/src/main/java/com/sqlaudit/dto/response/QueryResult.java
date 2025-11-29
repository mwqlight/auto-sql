package com.sqlaudit.dto.response;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class QueryResult {
    private List<Map<String, Object>> data;
    private int rowCount;
    private long executionTime; // in milliseconds
    private String errorMessage;
    private boolean success;
    
    // Getters and Setters (Lombok should generate these, but adding explicitly just in case)
    public List<Map<String, Object>> getData() {
        return data;
    }
    
    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
    
    public int getRowCount() {
        return rowCount;
    }
    
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
    
    public long getExecutionTime() {
        return executionTime;
    }
    
    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}