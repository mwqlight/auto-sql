package com.sqlaudit.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class OptimizationResponse {
    private List<OptimizationSuggestion> suggestions;
    private String optimizedSql;
    
    // Getters and Setters (Lombok should generate these, but adding explicitly just in case)
    public List<OptimizationSuggestion> getSuggestions() {
        return suggestions;
    }
    
    public void setSuggestions(List<OptimizationSuggestion> suggestions) {
        this.suggestions = suggestions;
    }
    
    public String getOptimizedSql() {
        return optimizedSql;
    }
    
    public void setOptimizedSql(String optimizedSql) {
        this.optimizedSql = optimizedSql;
    }
}