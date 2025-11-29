package com.sqlaudit.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class ExecutionPlan {
    private String databaseType;
    private List<PlanNode> nodes;
    private String rawPlan;
    
    // Getters and Setters (Lombok should generate these, but adding explicitly just in case)
    public String getDatabaseType() {
        return databaseType;
    }
    
    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }
    
    public List<PlanNode> getNodes() {
        return nodes;
    }
    
    public void setNodes(List<PlanNode> nodes) {
        this.nodes = nodes;
    }
    
    public String getRawPlan() {
        return rawPlan;
    }
    
    public void setRawPlan(String rawPlan) {
        this.rawPlan = rawPlan;
    }
}