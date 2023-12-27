import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'

import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faPlus, faTrash, faUserPlus } from '@fortawesome/free-solid-svg-icons'

import './assets/main.css'

const pinia = createPinia()
const app = createApp(App)

library.add(faTrash, faUserPlus, faPlus)

app.component('font-awesome-icon', FontAwesomeIcon)

app.use(pinia)
app.use(router)

app.mount('#app')
