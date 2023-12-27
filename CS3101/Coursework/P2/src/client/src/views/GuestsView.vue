<template>
  <h1>Guests</h1>
  <h3>Invite Code: {{ guestStore.invitationCode }}</h3>
  <GuestsTable
    v-if="guestStore.invitationCode"
    :guests="guestStore.guests"
    :invitationCode="guestStore.invitationCode"
    :dietaryRequirements="guestStore.dietaryRequirements"
    @updateResponse="updateResponse"
    @updateDietaryRequirement="updateDietaryRequirement"
    @reload="guestStore.getGuests(guestStore.invitationCode)"
  />
</template>

<script>
import GuestsTable from '../components/GuestsTable.vue'
import { useGuestStore } from '../stores/guest'

export default {
  components: { GuestsTable },
  setup() {
    const guestStore = useGuestStore()
    return { guestStore }
  },
  methods: {
    /**
     * Updates a guests response from a select input
     */
    async updateResponse(guest, event) {
      var response = event.target.value

      try {
        // Try to update the guest response
        await this.guestStore.updateResponse(guest, response)
      } catch (err) {
        // Reset the input if failed
        event.target.value = guest.response
        alert('Failed to update guest response')
      }
    },
    /**
     * Updates a guests dietary requirement
     * @param {*} guest  Guest to be updated
     * @param {*} dietary_requirement Dietary requirement to be updated
     * @param {*} event Checklist item updated
     */
    async updateDietaryRequirement(guest, event) {
      var dietaryRequirement = event.target.name
      var newValue = event.target.checked
      var oldValue = guest.dietary_requirements.includes(dietaryRequirement)

      try {
        // Try to update the dietary requirement
        await this.guestStore.updateDietaryRequirement(guest, dietaryRequirement, newValue)
      } catch (err) {
        // Reset value if update failed
        event.target.value = event.target.checked = oldValue
        alert('Failed to update dietary requirement')
      }
    }
  }
}
</script>
