import apiClient from '../index'
import type { DataSourceConfig } from '../../types/datasource'

// 获取所有数据源配置
export const getDataSourceConfigs = () => {
  return apiClient.get('/v1/datasources')
}

// 添加数据源配置
export const addDataSourceConfig = (data: DataSourceConfig) => {
  return apiClient.post('/v1/datasources', data)
}

// 更新数据源配置
export const updateDataSourceConfig = (id: number, data: DataSourceConfig) => {
  return apiClient.put(`/v1/datasources/${id}`, data)
}

// 删除数据源配置
export const deleteDataSourceConfig = (id: number) => {
  return apiClient.delete(`/v1/datasources/${id}`)
}

// 测试数据源连接
export const testDataSourceConnection = (id: number) => {
  return apiClient.post(`/v1/datasources/${id}/test`)
}