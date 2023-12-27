<template>
  <main>
    <form @submit.prevent action="#" id="loginForm">
      <h1>Organiser Login</h1>
      <div class="loginForm-input">
        <label for="organiser_id">Organiser ID</label>
        <input type="text" placeholder="Organiser ID" v-model="organiser_id" required />
      </div>
      <div class="loginForm-input">
        <label for="password">Password</label>
        <input type="password" placeholder="Password" v-model="password" required />
      </div>

      <div class="loginForm-input">
        <input type="submit" id="loginBtn" value="Login" @click="login()" />
      </div>
      <div id="errorMessage" v-if="error">
        {{ errorMessage }}
      </div>
    </form>
  </main>
</template>

<script>
import { useOrganiserStore } from '../stores/organiser'
export default {
  data() {
    return {
      organiser_id: '',
      password: '',
      error: false,
      errorMessage: ''
    }
  },
  setup() {
    const organiserStore = useOrganiserStore()
    return { organiserStore }
  },
  methods: {
    async login() {
      // Check that ID and password are given
      if (this.organiser_id.trim() == '' || this.password.trim() == '') {
        return
      }

      // Try to login to the server
      try {
        await this.organiserStore.authenticate(this.organiser_id, this.password)
        this.error = false
        this.$router.push('/organisers')
      } catch (err) {
        if (err.response.status == 500) {
          this.errorMessage = "Couldn't connect to the server"
        } else if (err.response.status == 403) {
          this.errorMessage = 'Invalid ID or password'
        } else {
          this.errorMessage = 'Unknown error'
        }
        this.error = true
      }
    }
  }
}
</script>

<style scoped>
#loginForm {
  display: flex;
  gap: 32px;
  flex-direction: column;
  max-width: 400px;
  margin: auto;
  align-items: center;
}

#loginForm div {
  width: auto;
}

.loginForm-input {
  display: flex;
  flex-direction: column;
  gap: 5px;
}
</style>
