<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { executeSql } from '../../api/modules/sql'
import { getDataSourceConfigs } from '../../api/modules/datasource'
import type { DataSourceConfig, QueryResult } from '../../types/datasource'
import { 
  NSelect, 
  NButton, 
  NCard, 
  NInput,
  useMessage
} from 'naive-ui'

const selectedDataSource = ref<number | null>(null)
const sqlContent = ref('')
const queryResult = ref<QueryResult | null>(null)
const dataSources = ref<DataSourceConfig[]>([])
const loading = ref(false)
let message: ReturnType<typeof useMessage> | null = null

const getMessage = () => {
  if (!message) {
    message = useMessage()
  }
  return message
}

// 移除未使用的computed属性
// const formattedSql = computed(() => {
//   // 这里应该实现SQL格式化逻辑
//   return sqlContent.value
// })

const loadDataSourceConfigs = async () => {
  try {
    const response = await getDataSourceConfigs()
    dataSources.value = response.data || []
  } catch (error) {
    getMessage().error('获取数据源配置失败')
  }
}

// 移除未使用的函数
// const handleFormat = () => {
//   // 这里应该调用SQL格式化API
//   console.log('格式化SQL')
// }

const handleExecute = async () => {
  if (!selectedDataSource.value) {
    getMessage().warning('请选择数据源')
    return
  }
  
  if (!sqlContent.value.trim()) {
    getMessage().warning('请输入SQL语句')
    return
  }
  
  loading.value = true
  try {
    const response = await executeSql({
      dataSourceId: selectedDataSource.value,
      sql: sqlContent.value
    })
    
    // 添加对response.data的检查
    if (response.data) {
      queryResult.value = response.data
    } else {
      queryResult.value = null
      getMessage().warning('查询成功但无数据返回')
    }
  } catch (error: any) {
    getMessage().error(error.response?.data?.message || '执行SQL失败')
  } finally {
    loading.value = false
  }
}

const handleClear = () => {
  sqlContent.value = ''
  queryResult.value = null
}

onMounted(() => {
  loadDataSourceConfigs()
})
</script>

<template>
  <n-card title="SQL编辑器" class="sql-editor-card">
    <div class="editor-header">
      <n-space align="center">
        <n-select
          v-model:value="selectedDataSource"
          :options="dataSources.map(ds => ({ label: ds.name, value: ds.id }))"
          placeholder="选择数据源"
          style="width: 200px;"
        />
        <n-button type="primary" @click="handleExecute" :loading="loading" ghost>执行</n-button>
        <n-button @click="handleClear" ghost>清空</n-button>
      </n-space>
    </div>
    
    <div class="editor-container">
      <n-input
        v-model:value="sqlContent"
        type="textarea"
        placeholder="请输入SQL语句..."
        :autosize="{ minRows: 10 }"
        round
      />
    </div>
    
    <div v-if="queryResult" class="result-container">
      <n-grid :cols="1" :x-gap="12">
        <n-gi>
          <n-card title="执行结果" size="small">
            <template #header-extra>
              <span>执行耗时: {{ queryResult.executionTime }}ms</span>
            </template>
            
            <n-data-table
              v-if="queryResult && queryResult.rows.length > 0"
              :columns="queryResult.columns.map(col => ({ title: col, key: col }))"
              :data="queryResult.rows.map((row, index) => {
                const rowData: Record<string, any> = { key: index }
                if (queryResult) {
                  queryResult.columns.forEach((col, i) => {
                    rowData[col] = row[i]
                  })
                }
                return rowData
              })"
              :bordered="true"
              striped
              :single-line="false"
            />
            
            <n-empty v-else description="查询结果为空" />
          </n-card>
        </n-gi>
      </n-grid>
    </div>
  </n-card>
</template>

<style scoped>
.sql-editor-card {
  background: linear-gradient(135deg, rgba(17, 17, 42, 0.8) 0%, rgba(10, 10, 26, 0.9) 100%);
  border: 1px solid #2a2a5a;
  box-shadow: 0 0 20px rgba(0, 255, 136, 0.1);
  backdrop-filter: blur(10px);
}

.editor-header {
  margin-bottom: 20px;
}

.editor-container {
  margin-bottom: 20px;
}

.result-container {
  margin-top: 20px;
}

:deep(.n-card-header) {
  border-bottom: 1px solid #2a2a5a;
}

:deep(.n-data-table) {
  border: 1px solid #2a2a5a;
}

:deep(.n-data-table-th) {
  background-color: #1a1a3a !important;
}

:deep(.n-data-table-td) {
  background-color: #11112a !important;
}
</style>