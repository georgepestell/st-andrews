import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'

import { setActivePinia, createPinia } from 'pinia'
import { useGuestStore } from '../guest'
import axios from 'axios'

vi.mock('axios')

const guests = [
  {
    id: '23',
    full_name: 'test',
    notes: ''
  }
]
axios.get = vi.fn(async () => Promise.resolve({ status: 200, data: guests }))

describe('Guest Store', () => {
  var guestStore

  beforeEach(() => {
    setActivePinia(createPinia())
    guestStore = useGuestStore()
  })

  afterEach(() => {
    vi.clearAllMocks()
  })

  it('makes GET request with invitation code on getGuests', async () => {
    const invitation_code = 'TestInvite'

    await guestStore.getGuests(invitation_code)

    expect(axios.get).toHaveBeenCalledOnce()
    expect(axios.get).toHaveBeenCalledWith(`/api/guests?invitation=${invitation_code}`)
  })

  it('sets guests state from getGuests', async () => {
    await guestStore.getGuests()

    expect(guestStore.guests).toStrictEqual(guests)
  })
})
