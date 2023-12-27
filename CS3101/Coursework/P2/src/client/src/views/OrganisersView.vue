<template>
  <main>
    <h1 id="title">Organiser View</h1>
    <GuestsTable
      v-if="loading == false && error == false"
      :guests="organiserStore.guests"
      :tables="organiserStore.tables"
      :isOrganiser="true"
      :invitations="organiserStore.invitations"
      @deleteGuest="deleteGuest"
      :dietaryRequirements="organiserStore.dietaryRequirements"
      @addGuest="addGuest"
      @reload="getGuests"
      @updateTableNo="updateTableNo"
    />
  </main>
</template>

<script>
import { useOrganiserStore } from '../stores/organiser'
import GuestsTable from '../components/GuestsTable.vue'

export default {
  components: { GuestsTable },
  data() {
    return {
      error: false,
      loading: true
    }
  },
  setup() {
    const organiserStore = useOrganiserStore()
    return { organiserStore }
  },
  methods: {
    async getGuests() {
      try {
        await this.organiserStore.getGuests()
        this.error = false
        this.loading = false
      } catch (err) {
        this.error = true
        this.loading = false
      }
    },
    async deleteGuest(guest) {
      var confirm = window.confirm(`Delete Guest ${guest.full_name} (${guest.id})`)
      if (confirm) {
        try {
          await this.organiserStore.deleteGuest(guest)
        } catch (err) {
          alert(err.message)
        }
      }
    },
    async addGuest(event) {
      event.preventDefault()
      var data = Object.fromEntries(new FormData(event.target).entries())
      try {
        await this.organiserStore.addGuest(
          data.full_name,
          data.notes,
          data.invitation_code,
          data.table_no
        )
        event.target.reset()
        this.organiserStore.getGuests()
      } catch (err) {
        alert(err.message)
      }
    },
    /**
     * Updates a guests response from a select input
     */
    async updateTableNo(guest, event) {
      var table_no = event.target.value
      try {
        // Try to update the guest response
        await this.organiserStore.updateTableNo(guest, table_no)
      } catch (err) {
        // Reset the input if failed
        event.target.value = guest.table_no
        alert(err.message)
      }
    }
  },
  created() {
    this.getGuests()
  }
}
</script>
