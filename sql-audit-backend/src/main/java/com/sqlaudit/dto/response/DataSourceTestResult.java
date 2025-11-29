package com.sqlaudit.dto.response;

import lombok.Data;

@Data
public class DataSourceTestResult {
    private boolean success;
    private String message;
    private String databaseType;
    private String databaseVersion;
    
    // Getters and Setters (Lombok should generate these, but adding explicitly just in case)
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getDatabaseType() {
        return databaseType;
    }
    
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }
    
    public String getDatabaseVersion() {
        return databaseVersion;
    }
    
    public void setDatabaseVersion(String databaseVersion) {
        this.databaseVersion = databaseVersion;
    }
}