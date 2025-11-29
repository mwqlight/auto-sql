package com.sqlaudit.service;

import com.sqlaudit.dto.request.ExecuteRequest;
import com.sqlaudit.dto.response.AuditReport;

public interface SqlAuditorService {
    AuditReport auditSql(ExecuteRequest request);
}