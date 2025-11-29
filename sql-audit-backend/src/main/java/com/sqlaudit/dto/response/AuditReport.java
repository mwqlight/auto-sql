package com.sqlaudit.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class AuditReport {
    private PerformanceScore performance;
    private SecurityScore security;
    private StandardScore standard;
    private List<AuditIssue> issues;
    private String riskLevel;
    private List<IndexSuggestion> indexSuggestions;
    
    // Getters and Setters (Lombok should generate these, but adding explicitly just in case)
    public PerformanceScore getPerformance() {
        return performance;
    }
    
    public void setPerformance(PerformanceScore performance) {
        this.performance = performance;
    }
    
    public SecurityScore getSecurity() {
        return security;
    }
    
    public void setSecurity(SecurityScore security) {
        this.security = security;
    }
    
    public StandardScore getStandard() {
        return standard;
    }
    
    public void setStandard(StandardScore standard) {
        this.standard = standard;
    }
    
    public List<AuditIssue> getIssues() {
        return issues;
    }
    
    public void setIssues(List<AuditIssue> issues) {
        this.issues = issues;
    }
    
    public String getRiskLevel() {
        return riskLevel;
    }
    
    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
    
    public List<IndexSuggestion> getIndexSuggestions() {
        return indexSuggestions;
    }
    
    public void setIndexSuggestions(List<IndexSuggestion> indexSuggestions) {
        this.indexSuggestions = indexSuggestions;
    }
}