<script setup>
import "@/sass/components/Header.scss";
</script>

<template>
  <header>
    <router-link to="/">
      <img id="site-title" alt="SUDOku" src="/logo.svg" />
    </router-link>

    <nav v-if="user" class="links-container">
      <div class="dropdown" v-if="user.role == 'ADMIN'" >
        <a><span>Manage</span></a>
        <div class="dropdown-content">
          <router-link to="/manage/users"><span>Users</span></router-link>
          <router-link to="/manage/puzzles"><span>Puzzles</span></router-link>
        </div>
      
      </div>
      <router-link to="/play"><span>Play</span></router-link>
      <router-link v-if="['ADMIN', 'SETTER'].includes(user.role)" to="/create"
        ><span>Create</span></router-link
      >
    </nav>
    <div v-if="user" id="account-container">
      <a href="#user">
        <span>{{ user.displayName }}</span>
      </a>
      <a href="/logout">
        <font-awesome-icon
          class="headerIcon"
          alt="logout"
          title="logout"
          icon="fa-solid fa-right-from-bracket"
        />
      </a>
    </div>
    <div v-else id="account-container">
      <router-link to="/auth/authorize?register">
        <span>Register</span>
      </router-link>
      <router-link class="callToAction" to="/auth/authorize">
        <span>Login</span>
      </router-link>
    </div>
  </header>
</template>

<script>
export default {
  data() {
    return {
      user: this.$cookies.get("user"),
      isOpen: false
    }
  },
  methods: {
    show(){
      this.isOpen = true
      console.log(this.isOpen)
      this.$refs.show.visible = true
    },
    hide(){
      this.isOpen = false
      this.$refs.show.visible = false
    }
  },
};
</script>
