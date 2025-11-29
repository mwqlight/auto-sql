package com.sqlaudit.service;

import com.sqlaudit.dto.request.ExplainRequest;
import com.sqlaudit.dto.response.ExecutionPlan;

public interface ExecutionPlanService {
    ExecutionPlan getExecutionPlan(ExplainRequest request);
}