<template>
  <form @submit.prevent id="loginForm">
    <h1>Guest Login</h1>
    <div class="loginForm-input">
      <label for="invitation_code">Invitation Code</label>
      <input placeholder="Invitation Code" type="text" v-model="invitationCode" required />
    </div>
    <div clas="loginForm-input">
      <input type="submit" value="Login" id="loginBtn" @click="login()" />
    </div>
    <div v-if="error">
      {{ errorMessage }}
    </div>
  </form>
</template>

<script>
import { useGuestStore } from '../stores/guest'
export default {
  data() {
    return {
      invitationCode: '',
      error: false,
      errorMessage: ''
    }
  },
  setup() {
    const guestStore = useGuestStore()
    return { guestStore }
  },
  methods: {
    async login() {
      // Check that the invitation code is not isEmpty
      if (this.invitationCode.trim() == '') {
        return
      }

      // Try to authenticate the invitation code
      try {
        await this.guestStore.getGuests(this.invitationCode)
        this.$router.push('/guests')
      } catch (err) {
        if (err.response.status == 500) {
          this.errorMessage = "Couldn't connect to the server"
        } else if (err.response.status == 401) {
          this.errorMessage = 'Invalid invitation code'
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
