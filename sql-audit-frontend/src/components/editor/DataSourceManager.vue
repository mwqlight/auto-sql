<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { 
  getDataSourceConfigs,
  addDataSourceConfig,
  updateDataSourceConfig,
  deleteDataSourceConfig
} from '../../api/modules/datasource'
import type { DataSourceConfig } from '../../types/datasource'
import { 
  NButton, 
  NDataTable, 
  NModal, 
  NForm, 
  NFormItem, 
  NInput, 
  NSelect,
  useMessage
} from 'naive-ui'
import { h } from 'vue'
import type { DataTableColumns } from 'naive-ui'

let message: ReturnType<typeof useMessage> | null = null

const getMessage = () => {
  if (!message) {
    message = useMessage()
  }
  return message
}

const dataSources = ref<DataSourceConfig[]>([])
const showModal = ref(false)
const editingDataSource = ref<DataSourceConfig | null>(null)
const formData = ref<DataSourceConfig>({
  name: '',
  type: 'MYSQL',
  host: '',
  port: 3306,
  database: '',
  username: '',
  password: ''
})

const renderActions = (row: DataSourceConfig) => {
  return h('div', { style: { display: 'flex', gap: '10px' } }, [
    h(
      NButton,
      {
        size: 'small',
        onClick: () => handleEdit(row)
      },
      { default: () => '编辑' }
    ),
    h(
      NButton,
      {
        size: 'small',
        type: 'error',
        onClick: () => handleDelete(row.id!)
      },
      { default: () => '删除' }
    )
  ])
}

const columns: DataTableColumns<DataSourceConfig> = [
  { title: '名称', key: 'name' },
  { title: '类型', key: 'type' },
  { title: '主机', key: 'host' },
  { title: '端口', key: 'port' },
  { title: '数据库', key: 'database' },
  { title: '用户名', key: 'username' },
  { title: '创建时间', key: 'createdAt' },
  { 
    title: '操作', 
    key: 'actions', 
    render: (row) => renderActions(row) 
  }
]

const fetchDataSourceConfigs = async () => {
  try {
    const response = await getDataSourceConfigs()
    dataSources.value = response.data.items || []
  } catch (error) {
    console.error('Failed to fetch data sources:', error)
  }
}

const handleAdd = () => {
  editingDataSource.value = null
  formData.value = {
    name: '',
    type: 'MYSQL',
    host: '',
    port: 3306,
    database: '',
    username: '',
    password: ''
  }
  showModal.value = true
}

const handleEdit = (dataSource: DataSourceConfig) => {
  editingDataSource.value = { ...dataSource }
  formData.value = { ...dataSource }
  showModal.value = true
}

const handleDelete = async (id: number) => {
  try {
    await deleteDataSourceConfig(id)
    getMessage().success('删除成功')
    await fetchDataSourceConfigs()
  } catch (error) {
    getMessage().error('删除失败')
  }
}

const handleSubmit = async () => {
  try {
    if (editingDataSource.value && editingDataSource.value.id) {
      await updateDataSourceConfig(editingDataSource.value.id, formData.value)
    } else {
      await addDataSourceConfig(formData.value)
    }
    showModal.value = false
    await fetchDataSourceConfigs()
  } catch (error) {
    console.error('Failed to save data source:', error)
  }
}

onMounted(() => {
  fetchDataSourceConfigs()
})
</script>

<template>
  <div class="data-source-manager">
    <div class="header">
      <h2>数据源管理</h2>
      <n-button type="primary" @click="handleAdd">添加数据源</n-button>
    </div>
    
    <n-data-table
  :columns="columns"
  :data="dataSources"
  :bordered="true"
/>
    
    <n-modal v-model:show="showModal" preset="card" style="width: 600px;">
      <template #header>
        <h3>{{ editingDataSource ? '编辑数据源' : '添加数据源' }}</h3>
      </template>
      
      <n-form :model="formData" label-placement="left" label-width="100">
        <n-form-item label="名称">
          <n-input v-model:value="formData.name" placeholder="请输入数据源名称" />
        </n-form-item>
        
        <n-form-item label="类型">
          <n-select 
            v-model:value="formData.type" 
            :options="[
              { label: 'MySQL', value: 'MYSQL' },
              { label: 'PostgreSQL', value: 'POSTGRESQL' },
              { label: 'Oracle', value: 'ORACLE' },
              { label: 'SQL Server', value: 'SQLSERVER' }
            ]" 
          />
        </n-form-item>
        
        <n-form-item label="主机">
          <n-input v-model:value="formData.host" placeholder="请输入主机地址" />
        </n-form-item>
        
        <n-form-item label="端口">
          <n-input-number v-model:value="formData.port" />
        </n-form-item>
        
        <n-form-item label="数据库">
          <n-input v-model:value="formData.database" placeholder="请输入数据库名称" />
        </n-form-item>
        
        <n-form-item label="用户名">
          <n-input v-model:value="formData.username" placeholder="请输入用户名" />
        </n-form-item>
        
        <n-form-item label="密码">
          <n-input v-model:value="formData.password" type="password" placeholder="请输入密码" />
        </n-form-item>
      </n-form>
      
      <template #footer>
        <n-space justify="end">
          <n-button @click="showModal = false">取消</n-button>
          <n-button type="primary" @click="handleSubmit">保存</n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<style scoped>
.data-source-manager {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>