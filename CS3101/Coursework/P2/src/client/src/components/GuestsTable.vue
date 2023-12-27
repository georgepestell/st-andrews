<template>
  <form @submit.prevent="addGuest" id="addGuestForm">
    <input style="display: none" type="checkbox" />
  </form>

  <datalist id="tableNumbers">
    <option value="1"></option>
    <option value="3"></option>
  </datalist>

  <div id="container">
    <table id="guestsTable">
      <thead>
        <tr>
          <th v-if="isOrganiser"></th>
          <th></th>
          <th></th>
          <th></th>
          <th></th>
          <th class="dietaryRequirements" :colspan="dietaryRequirements.length + 1">
            Dietary Requirements
          </th>
          <th></th>
        </tr>
        <tr>
          <th v-if="isOrganiser"></th>
          <th>Full Name</th>
          <th>RSVP</th>
          <th v-if="isOrganiser">Table</th>
          <th v-if="isOrganiser">Invitation</th>
          <th v-for="(req, reqIndex) in dietaryRequirements" :key="reqIndex" class="dietaryRequirements">
            <abbr :title="req.description">{{ req.short_name }}</abbr>
          </th>
          <th>
            <button @click="addDietaryRequirement">
              <font-awesome-icon icon=" fa-solid fa-plus" />
            </button>
          </th>
          <th v-if="isOrganiser">Notes</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="isOrganiser">
          <td>
            <button type="submit" form="addGuestForm">
              <font-awesome-icon icon="fa-solid fa-user-plus" />
            </button>
          </td>
          <td class="fullName">
            <input form="addGuestForm" name="full_name" type="text" placeholder="Full name" required />
          </td>
          <td></td>
          <td class="tableNumber">
            <select form="addGuestForm" name="table_no" required>
              <option selected></option>
              <option v-for="table in tables" :key="table">{{ table.table_no }}</option>
            </select>
          </td>
          <td class="invitationCode">
            <select form="addGuestForm" name="invitation_code" required>
              <option selected></option>
              <option v-for="invitation in invitations" :key="invitation.code">
                {{ invitation.code }}
              </option>
            </select>
          </td>
          <td v-for="(_, reqIndex) in dietaryRequirements" :key="reqIndex"></td>
          <td></td>
          <td class="notes">
            <input form="addGuestForm" name="notes" type="text" />
          </td>
        </tr>
        <tr v-for="guest in guests" :key="guest.id">
          <td v-if="isOrganiser">
            <button @click="deleteGuest(guest)">
              <font-awesome-icon icon="fa-solid fa-trash" />
            </button>
          </td>
          <td class="fullName">{{ guest.full_name }}</td>
          <td class="response">
            <select :disabled="!this.$attrs.onUpdateResponse" name="response" :value="guest.response"
              @change="updateResponse(guest, $event)">
              <option value="1">YES</option>
              <option value="0">NO</option>
              <option value="null"></option>
            </select>
          </td>
          <td class="tableNumber" v-if="isOrganiser">
            <select name="table_no" :value="guest.table_no" @change="updateTableNo(guest, $event)">
              <option v-for="table in tables" :key="table">{{ table.table_no }}</option>
            </select>
          </td>
          <td class="invitationCode" v-if="isOrganiser">{{ guest.invitation_code }}</td>
          <td v-for="(req, reqIndex) in dietaryRequirements" :key="reqIndex" class="dietaryRequirements">
            <input type="checkbox" :name="req.short_name" :disabled="!this.$attrs.onUpdateDietaryRequirement"
              :checked="guest.dietary_requirements.includes(req.short_name)"
              @change="updateDietaryRequirement(guest, $event, $this)" />
          </td>
          <td></td>
          <td class="notes" v-if="isOrganiser">{{ guest.notes }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  props: {
    guests: {
      type: Array[Object],
      default: []
    },
    isOrganiser: {
      type: Boolean,
      default: false
    },
    invitationCode: String,
    dietaryRequirements: {
      default: []
    },
    tables: {
      default: []
    },
    invitations: {
      default: []
    }
  },
  data() {
    return {
      filters: {
        response: {
          active: false,
          values: [1, 0, null],
          value: 0
        }
      }
    }
  },
  methods: {
    async updateResponse(guest, event) {
      this.$emit('updateResponse', guest, event)
    },
    async deleteGuest(guest) {
      this.$emit('deleteGuest', guest)
    },
    async updateDietaryRequirement(guest, event) {
      this.$emit('updateDietaryRequirement', guest, event)
    },
    async updateTableNo(guest, event) {
      this.$emit('updateTableNo', guest, event)
    },
    async addGuest(event) {
      this.$emit('addGuest', event)
    },
    async addDietaryRequirement() {
      const short_name = window.prompt('Dietary Requirement Name')

      if (!short_name) {
        return
      }

      if (this.dietaryRequirements.map((req) => req.short_name).includes(short_name)) {
        alert('Requirement already exists')
        return
      }

      // Do not allow special characters except "-" and "_"
      if (!/^[a-zA-Z0-9\-_]+/g.test(short_name) || short_name.length > 20) {
        alert(
          "Name cannot be longer than 20 characters, and can only contain letters, numbers, and the special characters '_-'"
        )
        return
      }

      const description = window.prompt('Dietary Requirement Description')

      if (!description) {
        return
      }

      if (description.length > 128) {
        alert('Description cannot exceed 128 characters')
        return
      }

      try {
        await axios.post('/api/guests/dietaryRequirements/add', {
          short_name: short_name,
          description: description
        })
        this.$emit('reload')
      } catch (err) {
        if (err.response.data.message) {
          alert(err.response.data.message)
        } else {
          alert('Could not add requirement')
        }
      }
    },
    toggleFilter(name) {
      const filter = this.filters[name]

      console.log(filter)

      if (!filter.active) {
        filter.active = true
        return
      }

      filter.value++
      filter.value = filter.value % filter.values.length

      if (filter.value == 0) {
        filter.active = false
      }
    },
    filterGuests() {
      return this.guests.filter((guest) => {
        return Object.keys(this.filters).every((filter) => {
          return (
            !this.filters[filter].active ||
            guest[filter] == this.filters[filter].values[this.filters[filter].value]
          )
        })
      })

      // const g = this.guests.filter((guest) => {

      // })
      // return this.guests.filter((guest) => {
      //   for (var key in Object.keys(filters)) {
      //     var filter = filters[key]
      //     if (filter.active && guest[key] != filter.value) {
      //       return false;
      //     }
      //     return true;
      //   }
      // })
    }
  }
}
</script>

<style scoped>
table {
  overflow: scroll;
  border-collapse: collapse;
}

th {
  padding: 5px;
  white-space: nowrap;
  text-overflow: ellipsis;
  overflow: hidden;
  font-weight: bold;
}

td {
  padding: 5px;
  text-align: center;
}

.fullName,
.notes {
  text-align: left;
  white-space: nowrap;
}

.tableNumber>input {
  width: 3.5em;
  overflow: hidden;
}

select[disabled] {
  border: none;
  appearance: none;
  color: inherit;
}

.fullName input {
  width: 150px;
}

.invitationCode,
.tableNumber,
.response select {
  text-transform: uppercase;
  font-family: monospace;
  text-align: center;
}

input,
select {
  font-family: monospace;
  text-align: center;
}

.notes input,
.fullName input {
  text-align: left;
}

.invitationCode input {
  width: 70px;
}

select[disabled],
button[disabled] {
  background: none;
}

.notes input {
  width: 100%;
}

button {
  aspect-ratio: 1 / 1;
  width: 100%;
}

table,
thead,
th,
tbody,
td {
  box-sizing: border-box;
  border: 1px solid #aaa;
}
</style>
