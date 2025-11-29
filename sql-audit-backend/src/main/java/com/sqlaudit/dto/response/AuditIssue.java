package com.sqlaudit.dto.response;

import lombok.Data;

@Data
public class AuditIssue {
    private String type; // PERFORMANCE, SECURITY, STANDARD
    private String severity; // LOW, MEDIUM, HIGH, CRITICAL
    private String description;
    private String suggestion;
    private int lineNumber;
    private String columnName;
    
    // Getters and Setters (Lombok should generate these, but adding explicitly just in case)
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getSeverity() {
        return severity;
    }
    
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSuggestion() {
        return suggestion;
    }
    
    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
    
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    public String getColumnName() {
        return columnName;
    }
    
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}