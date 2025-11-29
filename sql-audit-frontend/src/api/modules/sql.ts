import apiClient from '../index'
import type { ExecuteRequest, QueryResult, AuditReport, OptimizeRequest, OptimizationResponse, ExplainRequest, ExecutionPlan } from '../../types/datasource'

// 执行SQL
export const executeSql = (data: ExecuteRequest) => {
  return apiClient.post<QueryResult>('/v1/sql/execute', data)
}

// 审计SQL
export const auditSql = (data: ExecuteRequest) => {
  return apiClient.post<AuditReport>('/v1/sql/audit', data)
}

// 优化SQL
export const optimizeSql = (data: OptimizeRequest) => {
  return apiClient.post<OptimizationResponse>('/v1/sql/optimize', data)
}

// 获取执行计划
export const getExecutionPlan = (data: ExplainRequest) => {
  return apiClient.post<ExecutionPlan>('/v1/sql/explain', data)
}