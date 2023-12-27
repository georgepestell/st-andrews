<script setup>
import AuthenticationService from "@/services/AuthenticationService";
</script>

<template>
  <h1>Login</h1>
  <form>
    <label>email: </label>
    <input
      type="email"
      v-model="email"
      placeholder="enter email"
      name="email"
    /><br />
    <div v-if="group == 24">
      <label>password: </label>
      <input
        type="password"
        v-model="password"
        v-on:keyup.enter="login"
        placeholder="enter password"
        name="password"
      /><br />
    </div>
    <div v-if="redirect == '/'">
      <label>Group Number: </label>
      <select name="group" id="group" v-model="group">
        <option value="20">20</option>
        <option value="21">21</option>
        <option value="22">22</option>
        <option value="23">23</option>
        <option value="24" selected="selected">24</option>
        <option value="25">25</option>
        <option value="26">26</option>
        <option value="27">27</option>
        <option value="28">28</option>
        <option value="29">29</option>
      </select>
    </div>

    <!-- <input type="number" name="group" value="24" /> -->
    <br />
    <button type="button" @click="login">Login</button>
  </form>
</template>

<script>
export default {
  data() {
    return {
      email: "",
      password: "",
      group: 24,
      redirect: "/",
    };
  },
  methods: {
    login() {
      if (this.group == 24) {
        AuthenticationService.login(this.email, this.password, this.redirect)
        // try {
        //   var response = await AuthenticationService.login(this.email, this.password, this.redirect)
        //   // response
        //   console.log(response.status);
        //           if (response.status == 200) {
        //             window.location.href = `${this.redirect}${
        //               response.data.token ? response.data.token : ""
        //             }`;
        //           }
        //     // else {
        //     //   console.log(response.data);
        //     //   if(response.data == 'byebye'){
        //     //     return;
        //     //   }
        //     // }
        // } catch (err) {
        //   // if (err.response.status == 401) {
        //   //   alert("Error: " + err.response.data.message);
        //   //   return;
        //   // } else {
        //   //   console.log(err);
        //   // }
        //   console.log(err)
        //   if (err) {
        //     alert("Wrong Email or Password. Please Try Again.");
        //     return;
        //   }
        // }

        
      } else {
        window.location.href = `https://cs3099user${this.group}.host.cs.st-andrews.ac.uk/auth/authorize?redirect=https://cs3099user24.host.cs.st-andrews.ac.uk/api/auth/token/`;
      }
    },
  },
  mounted() {
    if (this.$route.query.redirect) {
      this.redirect = this.$route.query.redirect;
    }
  },
};
</script>
