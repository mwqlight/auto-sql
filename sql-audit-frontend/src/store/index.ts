import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    isLoading: false,
    errorMessage: ''
  }),
  
  actions: {
    setLoading(loading: boolean) {
      this.isLoading = loading
    },
    
    setErrorMessage(message: string) {
      this.errorMessage = message
    }
  }
})