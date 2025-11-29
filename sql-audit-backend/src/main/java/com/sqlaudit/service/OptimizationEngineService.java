package com.sqlaudit.service;

import com.sqlaudit.dto.request.OptimizeRequest;
import com.sqlaudit.dto.response.OptimizationResponse;

public interface OptimizationEngineService {
    OptimizationResponse optimizeSql(OptimizeRequest request);
}