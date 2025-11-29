package com.sqlaudit.service.impl;

import com.sqlaudit.dto.request.OptimizeRequest;
import com.sqlaudit.dto.response.OptimizationResponse;
import com.sqlaudit.dto.response.OptimizationSuggestion;
import com.sqlaudit.service.OptimizationEngineService;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OptimizationEngineImpl implements OptimizationEngineService {

    @Override
    public OptimizationResponse optimizeSql(OptimizeRequest request) {
        OptimizationResponse response = new OptimizationResponse();
        List<OptimizationSuggestion> suggestions = new ArrayList<>();
        
        try {
            Statement statement = CCJSqlParserUtil.parse(request.getSql());
            String optimizedSql = request.getSql(); // 默认保持原样
            
            // 子查询优化建议
            if (request.getSql().toUpperCase().contains("SELECT") && 
                request.getSql().toUpperCase().contains("FROM") && 
                request.getSql().toUpperCase().contains("(SELECT")) {
                
                OptimizationSuggestion suggestion = new OptimizationSuggestion();
                suggestion.setOriginalSql(request.getSql());
                suggestion.setOptimizedSql(optimizedSql); // 实际应用中需要真正的优化
                suggestion.setCategory("PERFORMANCE");
                suggestion.setExpectedImprovement(20.0); // 预估20%性能提升
                suggestion.setExplanation("检测到子查询，可以考虑使用JOIN优化");
                suggestion.setApplySteps(List.of(
                    "识别内层查询和外层查询的关系",
                    "将子查询转换为JOIN操作",
                    "确保连接条件正确"
                ));
                suggestions.add(suggestion);
            }
            
            // NOT IN优化建议
            if (request.getSql().toUpperCase().contains("NOT IN")) {
                OptimizationSuggestion suggestion = new OptimizationSuggestion();
                suggestion.setOriginalSql(request.getSql());
                suggestion.setOptimizedSql(optimizedSql); // 实际应用中需要真正的优化
                suggestion.setCategory("PERFORMANCE");
                suggestion.setExpectedImprovement(30.0); // 预估30%性能提升
                suggestion.setExplanation("NOT IN操作可能导致全表扫描，建议使用NOT EXISTS优化");
                suggestion.setApplySteps(List.of(
                    "将NOT IN替换为NOT EXISTS",
                    "确保子查询中处理NULL值的情况"
                ));
                suggestions.add(suggestion);
            }
            
            // OR条件优化建议
            if (request.getSql().toUpperCase().contains(" OR ")) {
                OptimizationSuggestion suggestion = new OptimizationSuggestion();
                suggestion.setOriginalSql(request.getSql());
                suggestion.setOptimizedSql(optimizedSql); // 实际应用中需要真正的优化
                suggestion.setCategory("PERFORMANCE");
                suggestion.setExpectedImprovement(15.0); // 预估15%性能提升
                suggestion.setExplanation("OR条件可能无法有效利用索引，建议使用UNION ALL优化");
                suggestion.setApplySteps(List.of(
                    "将OR条件拆分为多个独立查询",
                    "使用UNION ALL连接查询结果",
                    "确保各查询条件互斥避免重复数据"
                ));
                suggestions.add(suggestion);
            }
            
            response.setSuggestions(suggestions);
            response.setOptimizedSql(optimizedSql);
            
        } catch (JSQLParserException e) {
            // 如果解析失败，返回空建议
            response.setSuggestions(new ArrayList<>());
            response.setOptimizedSql(request.getSql());
        }
        
        return response;
    }
}