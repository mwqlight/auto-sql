package com.sqlaudit.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class OptimizationSuggestion {
    private String originalSql;
    private String optimizedSql;
    private String category; // PERFORMANCE, SECURITY, STANDARD
    private double expectedImprovement; // 预期提升百分比
    private String explanation;
    private List<String> applySteps; // 应用步骤
    
    // Getters and Setters (Lombok should generate these, but adding explicitly just in case)
    public String getOriginalSql() {
        return originalSql;
    }
    
    public void setOriginalSql(String originalSql) {
        this.originalSql = originalSql;
    }
    
    public String getOptimizedSql() {
        return optimizedSql;
    }
    
    public void setOptimizedSql(String optimizedSql) {
        this.optimizedSql = optimizedSql;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public double getExpectedImprovement() {
        return expectedImprovement;
    }
    
    public void setExpectedImprovement(double expectedImprovement) {
        this.expectedImprovement = expectedImprovement;
    }
    
    public String getExplanation() {
        return explanation;
    }
    
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    
    public List<String> getApplySteps() {
        return applySteps;
    }
    
    public void setApplySteps(List<String> applySteps) {
        this.applySteps = applySteps;
    }
}