import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'

import { setActivePinia, createPinia } from 'pinia'
import { useOrganiserStore } from '../organiser'
import axios from 'axios'

vi.mock('axios')

describe('Organiser Store', () => {
  var organiserStore

  beforeEach(() => {
    setActivePinia(createPinia())
    organiserStore = useOrganiserStore()
  })

  afterEach(() => {
    vi.clearAllMocks()
  })

  it('makes GET request on fetch', async () => {
    await organiserStore.fetch()

    expect(axios.get).toHaveBeenCalledOnce()
    expect(axios.get).toHaveBeenCalledWith('/api/auth/organisers')
  })

  it('makes POST request on authenticate', async () => {
    const organiser_id = 'testOrganiser'
    const password = 'testPassword'

    await organiserStore.authenticate(organiser_id, password)

    expect(axios.post).toHaveBeenCalledOnce()
    expect(axios.post).toHaveBeenCalledWith('/api/auth/organisers', {
      organiser_id: organiser_id,
      password: password
    })
  })
})
