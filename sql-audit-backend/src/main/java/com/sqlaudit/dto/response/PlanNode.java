package com.sqlaudit.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class PlanNode {
    private String id;
    private String operation;
    private String tableName;
    private String cost;
    private String rows;
    private List<PlanNode> children;
    
    // Getters and Setters (Lombok should generate these, but adding explicitly just in case)
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String operation) {
        this.operation = operation;
    }
    
    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    
    public String getCost() {
        return cost;
    }
    
    public void setCost(String cost) {
        this.cost = cost;
    }
    
    public String getRows() {
        return rows;
    }
    
    public void setRows(String rows) {
        this.rows = rows;
    }
    
    public List<PlanNode> getChildren() {
        return children;
    }
    
    public void setChildren(List<PlanNode> children) {
        this.children = children;
    }
}