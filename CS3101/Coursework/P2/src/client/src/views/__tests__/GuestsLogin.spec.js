import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'

import { setActivePinia, createPinia } from 'pinia'
import { createTestingPinia } from '@pinia/testing'

import { shallowMount } from '@vue/test-utils'
import GuestsLogin from '../GuestsLogin.vue'

import { useGuestStore } from '../../stores/guest'

vi.mock('axios')

describe('Guest Login', () => {
  var wrapper
  var guestStore

  const mockRouter = {
    push: vi.fn()
  }

  beforeEach(() => {
    setActivePinia(createPinia())
    wrapper = shallowMount(GuestsLogin, {
      global: {
        plugins: [
          createTestingPinia({
            createSpy: vi.fn
          })
        ],
        mocks: {
          $router: mockRouter
        }
      }
    })
    guestStore = useGuestStore()
  })

  /**
   * Cleanup global mocks after each test
   */
  afterEach(() => {
    vi.clearAllMocks()
  })

  it('Renders properly', () => {
    expect(wrapper.text()).toContain('Invitation Code')
    expect(wrapper.find('#errorMessage').exists()).toBeFalsy()
  })

  it('Renders error properly', async () => {
    const errorMessage = 'Test Error Message'

    await wrapper.setData({
      error: true,
      errorMessage: errorMessage
    })

    expect(wrapper.text()).toContain(errorMessage)
  })

  it('Calls login function once on form submit', async () => {
    await wrapper.setData({
      invitationCode: 'not empty'
    })

    const login = vi.fn(async () => ({}))
    wrapper.vm.login = login

    const button = wrapper.find('#loginBtn')
    await button.trigger('click')

    expect(login).toHaveBeenCalledOnce()
  })

  it('Does nothing upon submitting form with empty invitation code', async () => {
    const button = wrapper.find('#loginBtn')
    await button.trigger('click')

    expect(guestStore.getGuests).not.toBeCalled()
  })

  it('Displays correct error when invitation code invalid', async () => {
    await wrapper.setData({
      invitationCode: 'not empty'
    })

    // Simulate the 403 error axios response
    guestStore.getGuests = vi.fn(async () => Promise.reject({ response: { status: 403 } }))

    // Simulate submit login form
    await wrapper.vm.login()

    // Expect an error message to appear
    expect(wrapper.text()).contains('Invalid invitation code')
  })
})
