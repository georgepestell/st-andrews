import { describe, it, expect, vi, beforeEach, vitest, afterEach } from 'vitest'

import { createTestingPinia } from '@pinia/testing'

import { shallowMount } from '@vue/test-utils'
import OrganisersLogin from '../OrganisersLogin.vue'
import { createPinia, setActivePinia } from 'pinia'

import { useOrganiserStore } from '../../stores/organiser'

describe('Organisers Login', () => {
  var wrapper
  var organiserStore

  const mockRouter = {
    push: vi.fn()
  }

  beforeEach(() => {
    setActivePinia(createPinia())
    wrapper = shallowMount(OrganisersLogin, {
      global: {
        plugins: [
          createTestingPinia({
            createSpy: vitest.fn
          })
        ],
        mocks: {
          $router: mockRouter
        }
      }
    })
    organiserStore = useOrganiserStore()
  })

  afterEach(() => {
    vi.clearAllMocks()
  })

  it('Renders properly', () => {
    expect(wrapper.text()).toContain('Organiser ID')
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

  it('Calls login function on form submit', async () => {
    await wrapper.setData({
      organiser_id: 'not empty',
      password: 'not empty'
    })

    const login = vi.fn()
    wrapper.vm.login = login

    const button = wrapper.find('#loginBtn')
    await button.trigger('click')

    expect(login).toHaveBeenCalledOnce()
  })

  it('Does not call login when ID empty', async () => {
    await wrapper.setData({
      password: 'not empty'
    })

    const button = wrapper.find('#loginBtn')
    await button.trigger('click')

    expect(organiserStore.authenticate).not.toHaveBeenCalled()
  })

  it('Does not call login when password empty', async () => {
    await wrapper.setData({
      organiser_id: 'not empty'
    })

    const button = wrapper.find('#loginBtn')
    await button.trigger('click')

    expect(organiserStore.authenticate).not.toHaveBeenCalled()
  })

  it('Displays correct error when invalid ID/password given', async () => {
    await wrapper.setData({
      organiser_id: 'not empty',
      password: 'not empty'
    })

    // Simulate the 403 error axios response
    organiserStore.authenticate = vi.fn(async () => Promise.reject({ response: { status: 403 } }))

    await wrapper.vm.login()
    expect(wrapper.text()).contains('Invalid ID or password')
  })

  it('Routes the user to the organiser view on successful authentication', async () => {
    await wrapper.setData({
      organiser_id: 'not empty',
      password: 'not empty'
    })

    wrapper.$router = mockRouter

    // Simulate the 403 error axios response
    organiserStore.authenticate = vi.fn(async () => Promise.resolve({ response: { status: 200 } }))

    // Simulate submit login form
    await wrapper.vm.login()

    // Expect an error message to appear
    expect(mockRouter.push).toHaveBeenCalledOnce()
  })
})
