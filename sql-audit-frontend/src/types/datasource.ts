export interface DataSourceConfig {
  id?: number
  name: string
  type: 'MYSQL' | 'POSTGRESQL' | 'ORACLE' | 'SQLSERVER'
  host: string
  port: number
  database: string
  username: string
  password: string
  createdAt?: string
  updatedAt?: string
}

export interface ExecuteRequest {
  dataSourceId: number
  sql: string
}

export interface QueryResult {
  columns: string[]
  rows: any[][]
  executionTime: number
}

export interface AuditReport {
  score: number
  riskLevel: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'
  performanceIssues: string[]
  securityIssues: string[]
  suggestions: string[]
}

export interface OptimizeRequest {
  sql: string
}

export interface OptimizationResponse {
  originalSql: string
  optimizedSql: string
  suggestions: string[]
}

export interface ExplainRequest {
  dataSourceId: number
  sql: string
}

export interface ExecutionPlan {
  plan: string
  cost: number
}