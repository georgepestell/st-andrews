import { defineStore } from 'pinia'
import axios from 'axios'

export const useOrganiserStore = defineStore('organiser', {
  state: () => ({
    isAuth: false,
    guests: [],
    dietaryRequirements: [],
    tables: [],
    invitations: []
  }),
  actions: {
    async getGuests() {
      try {
        const response = await axios.get('/api/organisers/guests')
        this.guests = response.data.guests
        this.dietaryRequirements = response.data.dietary_requirements
        this.tables = response.data.tables
        this.invitations = response.data.invitations
      } catch (err) {
        this.error = true
        this.loading = false
      }
    },
    async fetch() {
      try {
        await axios.get('/api/auth/organisers')
        this.isAuth = true
      } catch (err) {
        this.isAuth = false
      }
    },
    async authenticate(organiser_id, password) {
      await axios.post('/api/auth/organisers', {
        organiser_id: organiser_id,
        password: password
      })
      this.isOrganiser = true
    },
    async logout() {
      try {
        await axios.get('/api/auth/organisers/logout')
        this.isAuth = false
      } catch (err) {
        console.log('Logout failed')
      }
    },
    async deleteGuest(guest) {
      // Call the backend to delete the guest
      await axios.post('/api/organisers/guests/delete', {
        guest_id: guest.id
      })

      // Get the array index of the guest
      const guest_index = this.guests.indexOf(guest)

      // Remove the guest from guests
      this.guests.splice(guest_index, 1)
    },
    async addGuest(full_name, notes, invitation_code, table_no) {
      try {
        // Call the backend api
        await axios.post('/api/organisers/guests/add', {
          full_name: full_name,
          notes: notes,
          invitation_code: invitation_code,
          table_no: table_no
        })
      } catch (err) {
        if (err.response.data.message) {
          throw new Error(err.response.data.message)
        } else {
          throw new Error('Unknown error')
        }
      }
    },
    async updateTableNo(guest, table_no) {
      try {
        // Call the backend api
        await axios.post('/api/organisers/guests/table', {
          guest_id: guest.id,
          table_no: table_no
        })
        guest.table_no = table_no
      } catch (err) {
        if (err.response.data.message) {
          throw new Error(err.response.data.message)
        } else {
          throw new Error('Unknown error')
        }
      }
    }
  }
})
