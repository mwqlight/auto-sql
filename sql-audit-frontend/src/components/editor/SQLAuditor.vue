<script setup lang="ts">
import { ref } from 'vue'
import type { AuditReport } from '../../types/datasource'
import { auditSql } from '../../api/modules/sql'

const props = defineProps<{
  sqlContent: string
}>()

const auditReport = ref<AuditReport | null>(null)
const loading = ref(false)
const error = ref('')

const handleAudit = async () => {
  if (!props.sqlContent.trim()) {
    error.value = '请输入SQL语句'
    return
  }
  
  loading.value = true
  error.value = ''
  auditReport.value = null
  
  try {
    const response = await auditSql({
      dataSourceId: 0, // 这里应该从父组件传递
      sql: props.sqlContent
    })
    auditReport.value = response.data
  } catch (err) {
    error.value = err instanceof Error ? err.message : '审计失败'
    console.error('SQL audit failed:', err)
  } finally {
    loading.value = false
  }
}

defineExpose({
  handleAudit
})
</script>

<template>
  <div class="sql-auditor">
    <div class="auditor-header">
      <n-button @click="handleAudit" :loading="loading">审计SQL</n-button>
    </div>
    
    <div v-if="error" class="error-message">
      <n-alert type="error">{{ error }}</n-alert>
    </div>
    
    <div v-if="auditReport" class="report-container">
      <n-card title="审计报告">
        <n-descriptions label-placement="left" bordered>
          <n-descriptions-item label="评分">
            <n-progress
              type="circle"
              :percentage="auditReport.score"
              :color="auditReport.score >= 80 ? '#67C23A' : auditReport.score >= 60 ? '#E6A23C' : '#F56C6C'"
            />
          </n-descriptions-item>
          <n-descriptions-item label="风险等级">
            <n-tag :type="auditReport.riskLevel === 'LOW' ? 'success' : 
                          auditReport.riskLevel === 'MEDIUM' ? 'warning' : 
                          auditReport.riskLevel === 'HIGH' ? 'error' : 'error'">
              {{ auditReport.riskLevel }}
            </n-tag>
          </n-descriptions-item>
        </n-descriptions>
        
        <div v-if="auditReport.performanceIssues.length > 0" class="issues-section">
          <h4>性能问题</h4>
          <n-list bordered>
            <n-list-item v-for="(issue, index) in auditReport.performanceIssues" :key="index">
              {{ issue }}
            </n-list-item>
          </n-list>
        </div>
        
        <div v-if="auditReport.securityIssues.length > 0" class="issues-section">
          <h4>安全问题</h4>
          <n-list bordered>
            <n-list-item v-for="(issue, index) in auditReport.securityIssues" :key="index">
              {{ issue }}
            </n-list-item>
          </n-list>
        </div>
        
        <div v-if="auditReport.suggestions.length > 0" class="issues-section">
          <h4>优化建议</h4>
          <n-list bordered>
            <n-list-item v-for="(suggestion, index) in auditReport.suggestions" :key="index">
              {{ suggestion }}
            </n-list-item>
          </n-list>
        </div>
      </n-card>
    </div>
  </div>
</template>

<style scoped>
.sql-auditor {
  padding: 20px;
}

.auditor-header {
  margin-bottom: 20px;
}

.report-container {
  margin-top: 20px;
}

.issues-section {
  margin: 20px 0;
}

.issues-section h4 {
  margin-bottom: 10px;
}
</style>