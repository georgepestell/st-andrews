import { defineStore } from 'pinia'
import axios from 'axios'

export const useGuestStore = defineStore('guest', {
  state: () => ({
    invitationCode: null,
    guests: [],
    dietaryRequirements: []
  }),
  actions: {
    async getGuests(invitation_code) {
      const response = await axios.get('/api/guests?invitation=' + invitation_code)
      this.guests = response.data.guests
      this.dietaryRequirements = response.data.dietary_requirements
      this.invitationCode = invitation_code
    },
    async updateResponse(guest, response) {
      await axios.post('/api/guests/response', {
        invitation_code: this.invitationCode,
        guest_id: guest.id,
        response: response
      })

      guest.response = response
    },
    async updateDietaryRequirement(guest, dietary_requirement, value) {
      await axios.post('/api/guests/dietaryRequirements', {
        invitation_code: this.invitationCode,
        guest_id: guest.id,
        dietary_requirement: dietary_requirement,
        value: value
      })
    }
  }
})
