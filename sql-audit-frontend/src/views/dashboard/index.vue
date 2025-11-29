<script setup lang="ts">
import { ref, onMounted } from 'vue'
import DataSourceManager from '../../components/editor/DataSourceManager.vue'
import SQLEditor from '../../components/editor/SQLEditor.vue'
import SQLAuditor from '../../components/editor/SQLAuditor.vue'
import SQLOptimizer from '../../components/editor/SQLOptimizer.vue'
import ExecutionPlan from '../../components/editor/ExecutionPlan.vue'
import type { DataSourceConfig } from '../../types/datasource'
import { getDataSourceConfigs } from '../../api/modules/datasource'

const dataSources = ref<DataSourceConfig[]>([])
const sqlContent = ref('')
const activeTab = ref('editor')
const auditorRef = ref<InstanceType<typeof SQLAuditor> | null>(null)
const optimizerRef = ref<InstanceType<typeof SQLOptimizer> | null>(null)
const planRef = ref<InstanceType<typeof ExecutionPlan> | null>(null)
const selectedDataSourceId = ref<number | null>(null)

const fetchDataSourceConfigs = async () => {
  try {
    const response = await getDataSourceConfigs()
    dataSources.value = response.data.items || []
  } catch (error) {
    console.error('Failed to fetch data sources:', error)
  }
}

const handleSqlChange = (sql: string) => {
  sqlContent.value = sql
}

const handleAudit = () => {
  activeTab.value = 'auditor'
  setTimeout(() => {
    auditorRef.value?.handleAudit()
  }, 100)
}

const handleOptimize = () => {
  activeTab.value = 'optimizer'
  setTimeout(() => {
    optimizerRef.value?.handleOptimize()
  }, 100)
}

const handleExplain = () => {
  activeTab.value = 'plan'
  setTimeout(() => {
    planRef.value?.handleExplain()
  }, 100)
}

onMounted(() => {
  fetchDataSourceConfigs()
})
</script>

<template>
  <div class="dashboard">
    <n-layout has-sider>
      <n-layout-sider
        bordered
        collapse-mode="width"
        :collapsed-width="64"
        :width="240"
        show-trigger
      >
        <DataSourceManager />
      </n-layout-sider>
      
      <n-layout>
        <n-layout-header bordered>
          <div class="header-tabs">
            <n-button 
              :type="activeTab === 'editor' ? 'primary' : 'default'" 
              @click="activeTab = 'editor'"
            >
              SQL编辑器
            </n-button>
            <n-button 
              :type="activeTab === 'auditor' ? 'primary' : 'default'" 
              @click="handleAudit"
            >
              SQL审计
            </n-button>
            <n-button 
              :type="activeTab === 'optimizer' ? 'primary' : 'default'" 
              @click="handleOptimize"
            >
              SQL优化
            </n-button>
            <n-button 
              :type="activeTab === 'plan' ? 'primary' : 'default'" 
              @click="handleExplain"
            >
              执行计划
            </n-button>
          </div>
        </n-layout-header>
        
        <n-layout-content>
          <div v-show="activeTab === 'editor'">
            <SQLEditor 
              :dataSources="dataSources" 
              @update:sql-content="handleSqlChange"
              v-model:selectedDataSourceId="selectedDataSourceId"
            />
          </div>
          
          <div v-show="activeTab === 'auditor'">
            <SQLAuditor 
              ref="auditorRef"
              :sqlContent="sqlContent" 
            />
          </div>
          
          <div v-show="activeTab === 'optimizer'">
            <SQLOptimizer 
              ref="optimizerRef"
              :sqlContent="sqlContent" 
            />
          </div>
          
          <div v-show="activeTab === 'plan'">
            <ExecutionPlan 
              ref="planRef"
              :sqlContent="sqlContent" 
              :data-source-id="selectedDataSourceId"
            />
          </div>
        </n-layout-content>
      </n-layout>
    </n-layout>
  </div>
</template>

<style scoped>
.dashboard {
  height: 100%;
}

.header-tabs {
  padding: 10px;
  display: flex;
  gap: 10px;
}

.n-layout {
  height: 100%;
}

.n-layout-content {
  padding: 10px;
}
</style>