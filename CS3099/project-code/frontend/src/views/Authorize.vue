<script setup>
import AuthService from "@/services/AuthenticationService.js"
import RegisterForm from "@/components/RegisterForm.vue"
import LoginForm from "@/components/LoginForm.vue"
</script>

<template>
    <RegisterForm v-if="$route.query.hasOwnProperty('register')" />
    <LoginForm v-else />
</template>

<script>
export default {
}

</script>