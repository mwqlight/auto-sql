package com.sqlaudit.service;

import com.sqlaudit.dto.request.ExecuteRequest;
import com.sqlaudit.dto.response.QueryResult;

public interface SqlExecutorService {
    QueryResult executeSql(ExecuteRequest request);
}