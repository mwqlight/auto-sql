package com.sqlaudit.dto.response;

import lombok.Data;

@Data
public class SecurityScore {
    private int score; // 0-100
    private String level; // LOW, MEDIUM, HIGH
    private String description;
    
    // Getters and Setters (Lombok should generate these, but adding explicitly just in case)
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public String getLevel() {
        return level;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}