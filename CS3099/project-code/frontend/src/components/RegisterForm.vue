<script setup>
import AuthenticationService from "@/services/AuthenticationService";
</script>

<template>
  <h1>Register</h1>
  <form>
    <label>email: </label>
    <input
      type="email"
      v-model="email"
      placeholder="your email"
      name="email"
    /><br />
    <label>password: </label>
    <input
      type="password"
      v-model="password"
      placeholder="your password"
      name="password"
    /><br />
    <label>Display Name: </label>
    <!-- contains ._a-zA-Z0-9 -->
    <input
      type="text"
      v-model="displayName"
      v-on:keyup.enter="register"
      placeholder="your name"
      name="displayName"
    /><br />
    <label>Role</label>
    <select name="role" id="role" v-model="role">
      <option value="" disabled selected hidden>Please Select</option>
      <option value="solver">solver</option> 
      <option value="setter">setter</option> <!-- setter & solver -->
      <!-- <option value="both">both of the above</option> -->
      <!-- <option value="admin">admin</option>  -->
      <!-- website admin should exist by default, not by creation -->
    </select>
    <br />
    <button type="button" @click="register">Register</button>
  </form>
</template>

<script>
export default {
  data() {
    return {
      email: "",
      password: "",
      displayName: "",
      role: "solver",
    };
  },
  methods: {
    register() {
      if(this.email == '' || this.password == '' || this.displayName == '' || this.role == ''){
        alert('Please fill in all the boxes for user registration.');
        return;
      }
      AuthenticationService.register(this.email, this.password, this.displayName, this.role)
        // .then((res) => {
        //   if (res.data) {
        //     console.log(res.data);
        //     alert(res.data.message);
        //     window.location.href = "/auth/authorize";
        //   }
        // })
        // .catch((err) => {
        //   if (err.response.status == 409) {
        //     alert("Error: " + err.response.data.message);
        //     return;
        //   } else {
        //     console.log(err);
        //   }
        // });
    },
  },
};
</script>
