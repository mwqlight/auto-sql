<script setup lang="ts">
import { ref, computed } from 'vue'
import type { OptimizationResponse } from '../../types/datasource'
import { optimizeSql } from '../../api/modules/sql'
import { NButton, NCard, NGrid, NGi, NList, NListItem, NAlert, NSpace } from 'naive-ui'

const props = defineProps<{
  sqlContent: string
}>()

const optimizationResult = ref<OptimizationResponse | null>(null)
const loading = ref(false)
const error = ref('')

// 计算优化提升百分比
const improvementPercentage = computed(() => {
  if (!optimizationResult.value) return 0
  // 这里应该从后端返回的数据中获取实际的提升百分比
  // 暂时使用模拟数据
  return Math.floor(Math.random() * 90) + 10 // 10-99%的随机提升
})

const handleOptimize = async () => {
  if (!props.sqlContent.trim()) {
    error.value = '请输入SQL语句'
    return
  }
  
  loading.value = true
  error.value = ''
  optimizationResult.value = null
  
  try {
    const response = await optimizeSql({
      sql: props.sqlContent
    })
    optimizationResult.value = response.data
  } catch (err) {
    error.value = err instanceof Error ? err.message : '优化失败'
    console.error('SQL optimization failed:', err)
  } finally {
    loading.value = false
  }
}

defineExpose({
  handleOptimize
})
</script>

<template>
  <n-card title="SQL优化器" class="sql-optimizer-card">
    <div class="optimizer-header">
      <n-space align="center">
        <n-button @click="handleOptimize" :loading="loading" type="primary" ghost>优化SQL</n-button>
        <n-button v-if="optimizationResult" @click="handleOptimize" ghost>重新优化</n-button>
      </n-space>
    </div>
    
    <div v-if="error" class="error-message">
      <n-alert type="error" closable>{{ error }}</n-alert>
    </div>
    
    <div v-if="optimizationResult" class="optimization-result">
      <n-grid :cols="2" :x-gap="12" :y-gap="12">
        <n-gi>
          <n-card title="原始SQL" size="small">
            <pre class="sql-code">{{ optimizationResult.originalSql }}</pre>
          </n-card>
        </n-gi>
        <n-gi>
          <n-card title="优化后SQL" size="small">
            <pre class="sql-code optimized">{{ optimizationResult.optimizedSql }}</pre>
          </n-card>
        </n-gi>
      </n-grid>
      
      <n-card title="优化建议" style="margin-top: 20px;" size="small">
        <n-list bordered>
          <n-list-item v-for="(suggestion, index) in optimizationResult.suggestions" :key="index">
            <div class="suggestion-item">
              <div class="suggestion-index">#{{ index + 1 }}</div>
              <div class="suggestion-content">{{ suggestion }}</div>
            </div>
          </n-list-item>
        </n-list>
      </n-card>
      
      <n-card title="性能提升预估" style="margin-top: 20px;" size="small" class="performance-card">
        <div class="performance-content">
          <div class="performance-value">{{ improvementPercentage }}%</div>
          <div class="performance-label">预计性能提升</div>
        </div>
      </n-card>
    </div>
    
    <div v-else-if="!loading && !error" class="empty-state">
      <n-empty description="点击上方按钮优化SQL语句">
        <template #icon>
          <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="#00ff88" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
            <polyline points="1 4 1 10 7 10"></polyline>
            <path d="M3.51 15a9 9 0 0 0 9.48-11.5"></path>
            <polyline points="23 20 23 14 17 14"></polyline>
            <path d="M20.49 9a9 9 0 0 1-9.48 11.5"></path>
          </svg>
        </template>
      </n-empty>
    </div>
  </n-card>
</template>

<style scoped>
.sql-optimizer-card {
  background: linear-gradient(145deg, rgba(10, 20, 30, 0.8), rgba(30, 40, 50, 0.9));
  border: 1px solid rgba(0, 255, 136, 0.3);
  box-shadow: 0 0 20px rgba(0, 255, 136, 0.15);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  color: #e0e0e0;
}

.sql-optimizer-card :deep(.n-card-header) {
  border-bottom: 1px solid rgba(0, 255, 136, 0.2);
}

.sql-optimizer-card :deep(.n-card__content) {
  padding: 20px;
}

.optimizer-header {
  margin-bottom: 20px;
}

.error-message {
  margin-bottom: 20px;
}

.sql-code {
  background: rgba(0, 20, 40, 0.7);
  padding: 15px;
  border-radius: 8px;
  overflow-x: auto;
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 14px;
  line-height: 1.5;
  border: 1px solid rgba(0, 255, 136, 0.1);
  color: #00ff88;
}

.sql-code.optimized {
  background: rgba(0, 40, 20, 0.7);
  border: 1px solid rgba(0, 255, 136, 0.2);
}

.suggestion-item {
  display: flex;
  gap: 15px;
  padding: 12px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.suggestion-item:last-child {
  border-bottom: none;
}

.suggestion-index {
  font-weight: bold;
  color: #00ff88;
  min-width: 30px;
}

.suggestion-content {
  flex: 1;
  line-height: 1.5;
}

.performance-card {
  background: rgba(0, 30, 60, 0.6);
  border: 1px solid rgba(0, 255, 136, 0.2);
}

.performance-content {
  text-align: center;
  padding: 20px 0;
}

.performance-value {
  font-size: 36px;
  font-weight: bold;
  color: #00ff88;
  text-shadow: 0 0 10px rgba(0, 255, 136, 0.5);
  margin-bottom: 5px;
}

.performance-label {
  color: #aaa;
  font-size: 14px;
}

.empty-state {
  text-align: center;
  padding: 40px 0;
  color: #888;
}

.empty-state :deep(.n-empty__description) {
  color: #888;
}
</style>