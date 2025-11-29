package com.sqlaudit.service.impl;

import com.sqlaudit.dto.request.ExecuteRequest;
import com.sqlaudit.dto.response.*;
import com.sqlaudit.service.SqlAuditorService;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.insert.Insert;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SqlAuditorServiceImpl implements SqlAuditorService {

    @Override
    public AuditReport auditSql(ExecuteRequest request) {
        AuditReport report = new AuditReport();
        List<AuditIssue> issues = new ArrayList<>();
        
        try {
            Statement statement = CCJSqlParserUtil.parse(request.getSql());
            
            // 性能审计
            performPerformanceAudit(statement, issues);
            
            // 安全审计
            performSecurityAudit(statement, issues);
            
            // 规范审计
            performStandardAudit(statement, issues);
            
            // 生成评分
            report.setPerformance(generatePerformanceScore(issues));
            report.setSecurity(generateSecurityScore(issues));
            report.setStandard(generateStandardScore(issues));
            
            report.setIssues(issues);
            report.setRiskLevel(determineRiskLevel(issues));
            
            // 生成索引建议
            report.setIndexSuggestions(generateIndexSuggestions(statement));
            
        } catch (JSQLParserException e) {
            AuditIssue issue = new AuditIssue();
            issue.setType("SYNTAX");
            issue.setSeverity("HIGH");
            issue.setDescription("SQL语法错误: " + e.getMessage());
            issue.setSuggestion("请检查SQL语法");
            issues.add(issue);
            
            report.setIssues(issues);
            report.setRiskLevel("HIGH");
        }
        
        return report;
    }
    
    private void performPerformanceAudit(Statement statement, List<AuditIssue> issues) {
        // 检查SELECT *
        if (statement instanceof Select) {
            String sql = statement.toString().toUpperCase();
            if (sql.contains("SELECT *")) {
                AuditIssue issue = new AuditIssue();
                issue.setType("PERFORMANCE");
                issue.setSeverity("MEDIUM");
                issue.setDescription("使用了SELECT *，可能导致不必要的数据传输");
                issue.setSuggestion("明确指定需要的列名");
                issues.add(issue);
            }
        }
        
        // 检查缺少WHERE条件的DELETE/UPDATE
        if (statement instanceof Delete) {
            Delete delete = (Delete) statement;
            if (delete.getWhere() == null) {
                AuditIssue issue = new AuditIssue();
                issue.setType("PERFORMANCE");
                issue.setSeverity("HIGH");
                issue.setDescription("DELETE语句缺少WHERE条件，将删除所有数据");
                issue.setSuggestion("添加适当的WHERE条件");
                issues.add(issue);
            }
        } else if (statement instanceof Update) {
            Update update = (Update) statement;
            if (update.getWhere() == null) {
                AuditIssue issue = new AuditIssue();
                issue.setType("PERFORMANCE");
                issue.setSeverity("HIGH");
                issue.setDescription("UPDATE语句缺少WHERE条件，将更新所有数据");
                issue.setSuggestion("添加适当的WHERE条件");
                issues.add(issue);
            }
        }
    }
    
    private void performSecurityAudit(Statement statement, List<AuditIssue> issues) {
        String sql = statement.toString().toUpperCase();
        
        // 检查SQL注入风险（简单的字符串拼接检测）
        if (sql.contains("||") && !(statement instanceof Select)) {
            AuditIssue issue = new AuditIssue();
            issue.setType("SECURITY");
            issue.setSeverity("HIGH");
            issue.setDescription("可能存在SQL注入风险");
            issue.setSuggestion("使用参数化查询");
            issues.add(issue);
        }
        
        // 检查高危操作
        if (sql.startsWith("DROP") || sql.startsWith("TRUNCATE")) {
            AuditIssue issue = new AuditIssue();
            issue.setType("SECURITY");
            issue.setSeverity("CRITICAL");
            issue.setDescription("检测到高危操作");
            issue.setSuggestion("谨慎执行此类操作");
            issues.add(issue);
        }
    }
    
    private void performStandardAudit(Statement statement, List<AuditIssue> issues) {
        String sql = statement.toString();
        
        // 检查表别名使用
        if (sql.toUpperCase().contains("FROM") && !sql.toUpperCase().contains(" AS ")) {
            // 简单检查，实际应该更复杂
            AuditIssue issue = new AuditIssue();
            issue.setType("STANDARD");
            issue.setSeverity("LOW");
            issue.setDescription("建议为表添加别名");
            issue.setSuggestion("使用AS关键字为表指定别名");
            issues.add(issue);
        }
    }
    
    private PerformanceScore generatePerformanceScore(List<AuditIssue> issues) {
        PerformanceScore score = new PerformanceScore();
        int issueCount = 0;
        int criticalCount = 0;
        
        for (AuditIssue issue : issues) {
            if ("PERFORMANCE".equals(issue.getType())) {
                issueCount++;
                if ("HIGH".equals(issue.getSeverity()) || "CRITICAL".equals(issue.getSeverity())) {
                    criticalCount++;
                }
            }
        }
        
        // 简单评分算法
        int performanceScore = Math.max(0, 100 - (issueCount * 5) - (criticalCount * 20));
        score.setScore(performanceScore);
        score.setLevel(performanceScore >= 80 ? "GOOD" : performanceScore >= 60 ? "FAIR" : "POOR");
        score.setDescription("性能评分基于发现的问题数量");
        
        return score;
    }
    
    private SecurityScore generateSecurityScore(List<AuditIssue> issues) {
        SecurityScore score = new SecurityScore();
        int issueCount = 0;
        int criticalCount = 0;
        
        for (AuditIssue issue : issues) {
            if ("SECURITY".equals(issue.getType())) {
                issueCount++;
                if ("CRITICAL".equals(issue.getSeverity())) {
                    criticalCount++;
                }
            }
        }
        
        // 简单评分算法
        int securityScore = Math.max(0, 100 - (issueCount * 10) - (criticalCount * 30));
        score.setScore(securityScore);
        score.setLevel(securityScore >= 80 ? "GOOD" : securityScore >= 60 ? "FAIR" : "POOR");
        score.setDescription("安全评分基于发现的安全问题数量");
        
        return score;
    }
    
    private StandardScore generateStandardScore(List<AuditIssue> issues) {
        StandardScore score = new StandardScore();
        int issueCount = 0;
        
        for (AuditIssue issue : issues) {
            if ("STANDARD".equals(issue.getType())) {
                issueCount++;
            }
        }
        
        // 简单评分算法
        int standardScore = Math.max(0, 100 - (issueCount * 5));
        score.setScore(standardScore);
        score.setLevel(standardScore >= 80 ? "GOOD" : standardScore >= 60 ? "FAIR" : "POOR");
        score.setDescription("规范评分基于发现的规范问题数量");
        
        return score;
    }
    
    private String determineRiskLevel(List<AuditIssue> issues) {
        int criticalCount = 0;
        int highCount = 0;
        int mediumCount = 0;
        
        for (AuditIssue issue : issues) {
            switch (issue.getSeverity()) {
                case "CRITICAL":
                    criticalCount++;
                    break;
                case "HIGH":
                    highCount++;
                    break;
                case "MEDIUM":
                    mediumCount++;
                    break;
            }
        }
        
        if (criticalCount > 0) {
            return "CRITICAL";
        } else if (highCount > 2) {
            return "HIGH";
        } else if (highCount > 0 || mediumCount > 5) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }
    
    private List<IndexSuggestion> generateIndexSuggestions(Statement statement) {
        List<IndexSuggestion> suggestions = new ArrayList<>();
        
        // 简单的索引建议生成
        // 在实际应用中，这需要更复杂的分析
        IndexSuggestion suggestion = new IndexSuggestion();
        suggestion.setTableName("example_table");
        suggestion.setColumns(new String[]{"column1", "column2"});
        suggestion.setIndexName("idx_example_column1_column2");
        suggestion.setCreateStatement("CREATE INDEX idx_example_column1_column2 ON example_table(column1, column2);");
        suggestion.setReason("复合索引可以提高查询性能");
        suggestions.add(suggestion);
        
        return suggestions;
    }
}