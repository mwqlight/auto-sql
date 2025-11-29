<script setup lang="ts">
import { ref } from 'vue'
import type { ExecutionPlan } from '../../types/datasource'
import { getExecutionPlan } from '../../api/modules/sql'

const props = defineProps<{
  sqlContent: string,
  dataSourceId: number | null
}>()

const executionPlan = ref<ExecutionPlan | null>(null)
const loading = ref(false)
const error = ref('')

const handleExplain = async () => {
  if (!props.dataSourceId) {
    error.value = '请选择数据源'
    return
  }
  
  if (!props.sqlContent.trim()) {
    error.value = '请输入SQL语句'
    return
  }
  
  loading.value = true
  error.value = ''
  executionPlan.value = null
  
  try {
    const response = await getExecutionPlan({
      dataSourceId: props.dataSourceId,
      sql: props.sqlContent
    })
    executionPlan.value = response.data
  } catch (err) {
    error.value = err instanceof Error ? err.message : '获取执行计划失败'
    console.error('Failed to get execution plan:', err)
  } finally {
    loading.value = false
  }
}

defineExpose({
  handleExplain
})
</script>

<template>
  <div class="execution-plan">
    <div class="plan-header">
      <n-button @click="handleExplain" :loading="loading">执行计划</n-button>
    </div>
    
    <div v-if="error" class="error-message">
      <n-alert type="error">{{ error }}</n-alert>
    </div>
    
    <div v-if="executionPlan" class="plan-result">
      <n-card title="执行计划">
        <n-descriptions label-placement="left" bordered>
          <n-descriptions-item label="预估成本">
            {{ executionPlan.cost }}
          </n-descriptions-item>
        </n-descriptions>
        
        <div class="plan-details">
          <pre>{{ executionPlan.plan }}</pre>
        </div>
      </n-card>
    </div>
  </div>
</template>

<style scoped>
.execution-plan {
  padding: 20px;
}

.plan-header {
  margin-bottom: 20px;
}

.plan-result {
  margin-top: 20px;
}

.plan-details {
  margin-top: 20px;
}

pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  background-color: #f5f5f5;
  padding: 15px;
  border-radius: 4px;
  max-height: 400px;
  overflow-y: auto;
}

.error-message {
  margin: 10px 0;
}
</style>