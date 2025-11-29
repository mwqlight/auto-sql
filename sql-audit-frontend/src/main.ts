import { createApp, h } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import { 
  NDialogProvider, 
  NNotificationProvider, 
  NMessageProvider,
  NConfigProvider
} from 'naive-ui'

const app = createApp({
  setup() {
    return () => h(NConfigProvider, null, {
      default: () => h(NDialogProvider, null, {
        default: () => h(NNotificationProvider, null, {
          default: () => h(NMessageProvider, null, {
            default: () => h(App)
          })
        })
      })
    })
  }
})

const pinia = createPinia()

app.use(router)
app.use(pinia)

app.mount('#app')