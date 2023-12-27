<script setup>
import "@/sass/views/UserManage.scss";
import AdminService from "@/services/AdminService.js";
import { BTable, BButton, BFormSelect } from "bootstrap-vue-3";
</script>

<template>
    <div class="page-setup">
      <span class="user-count">
        Total number of users: {{ userCount }}
      </span>
      <span class="user-per-page">
        No. of users displayed per page:
        <select v-model="pageSize" @change="changeCap">
          <option :value=10>10</option>
          <option :value=50 selected="selected">50</option>
          <option :value=100>100</option>
        </select>
      </span>
      <span class="page-no">
        Page:
        <select v-model="pageNo" @change="loadUsers">
          <option v-for="page in totalPages" :value="page">{{ page }}</option>
        </select>
      </span>
    </div>
    <div>
        <b-table :items="items" :fields="fields">
          <template v-slot:cell(button)="row">
            <div v-if="row.item.self">
              N/A
            </div>
            <div v-else>
              <b-button @click="removeUser(row.item._id)">
                <font-awesome-icon icon="fa-solid fa-trash"/>
              </b-button>
            </div>
          </template>
          <template v-slot:cell(changeRole)="row">
            <div v-if="row.item.self">
              N/A
            </div>
            <div v-else>
              <!-- make it into check key -->
              <div v-if="row.item._id in editingIDRoles">
                <b-button @click="viewMode(row.item._id)">
                  <font-awesome-icon icon="fa-solid fa-close"/>
                </b-button>
              </div>
              <div v-else>
                <b-button @click="editMode(row.item._id,row.item.role)">
                  <font-awesome-icon icon="fa-solid fa-pencil"/>
                </b-button>
              </div>
            </div>
          </template>
          <template v-slot:cell(role)="row">
            <div v-if="row.item._id in editingIDRoles">
              <b-form-select v-model="editingIDRoles[row.item._id]" :options="roleOptions" @change="updateRole(row.item._id,$event)"></b-form-select>
            </div>
            <div v-else>
              {{ row.item.role }}
            </div>
          </template>
        </b-table>
    </div>

</template>

<script>
export default {
   components: {
       BTable, BButton, BFormSelect
   },
   data(){
    return{
      items: [],
      fields: [
        {key: '_id', label: 'User ID'},
        {key: 'groupID', label: 'group'},
        {key: 'displayName', label: 'Display Name'},
        {key: 'email', label: 'Email'},
        {key: 'creationDate', label: 'Account Creation Time'},
        {key: 'role', label: 'Role'},
        {key: 'changeRole', label: 'Change Role?'},
        {key: 'button', label: 'Remove?'}
      ],
      roleOptions: [
        { value: 'SOLVER', text: 'SOLVER' },
        { value: 'SETTER', text: 'SETTER' },
        { value: 'ADMIN', text: 'ADMIN' }],
      pageNo: 1,
      pageSize: 50,
      totalPages: 1,
      userCount: 1,
      editingIDRoles: [],
    }
   },
   methods: {
    async removeUser(id){
      if(confirm(`Do you want to remove user account (id: ${id}) from the database?\nThis means that the user can no longer log into the website.`)){
        await AdminService.removeUser(id)
          .then((res) => {
            // check response
            // alert(res.data.message)
            this.loadUsers()
          })
          .catch((err) => {
            alert(err.response.data.message)
          })
      }
    },
    async loadUsers() {
      await AdminService.getUsers(this.pageSize, this.pageNo)
        .then((res) => {
          const data = res.data
          this.items = data.users
          this.userCount = data.noOfUsers
          this.totalPages = data.totalPages
          this.roleOptions = data.roleOptions
        })
        .catch((err) => {
          alert(err.response.data.message)
        })

    },
    async changeCap() {
      this.pageNo = 1
      this.loadUsers()
    },
    editMode(id,role){
      this.editingIDRoles[id] = role
      // this.editingRoleIDs.push(id)
    },
    viewMode(id){
      // this.editingRoleIDs = this.editingRoleID.filter(u => {return u != id})
      delete this.editingIDRoles[id]
    },
    async updateRole(id, newRole) {
      await AdminService.updateRole(id, newRole)
        .then((res) => {
          const response = res.data
          const userID = response._id
          this.items = this.items.map(user => {
                          if(user._id == userID){
                            return response.newData
                          } else {
                            return user
                          }
                        })
          this.viewMode(userID)
          // alert(response.message)
        }).catch((err) => {
          alert(err.response.data.message)
        })
    },
    async updateRole2(id) {
      console.log(id)
      console.log(this.editingIDRoles)
      // console.log(this.editingIDRoles[id])
      //TOOSLOW?
      //AND CHECK ROLE VALIDITY
      await AdminService.updateRole(id, this.editingRoleIDs[id])
        .then((res) => {
          console.log('asdf')
        }).catch((err) => {
          console.log(err.response.data.message)
        })
    }
   },
   async mounted() {
      this.loadUsers();
   }
}
</script>
