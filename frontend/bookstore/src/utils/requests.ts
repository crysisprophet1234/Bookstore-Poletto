import axios, { AxiosRequestConfig } from 'axios'
import { getAuthData } from './storage'

export const BASE_URL = 'https://192.168.15.13:8443'

type LoginData = {

    username: string
    password: string

}

type SignupData = {

    username: string
    password: string
    firstname?: string
    lastname?: string

}

export const requestBackend = (config: AxiosRequestConfig) => {

    const headers: any = { ...config.headers }

    if (config.withCredentials) {

        headers.Authorization = `Bearer ${getAuthData()?.token}`

    }

    return axios({ ...config, baseURL: BASE_URL, headers, data: config.data })

}

export const requestBackendLogin = (loginData: LoginData) => {

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': 'No auth'
    }

    const data = JSON.stringify({

        email: loginData.username,
        password: loginData.password,

    })

    return axios({ method: 'POST', baseURL: BASE_URL, url: '/api/auth/v2/authenticate', data, headers })

}

export const requestBackendSignup = (signupData: SignupData) => {

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': 'No auth'
    }

    const data = JSON.stringify({

        email: signupData.username,
        password: signupData.password,
        firstname: signupData.firstname,
        lastname: signupData.lastname

    })

    return axios({ method: 'POST', baseURL: BASE_URL, url: '/api/auth/v2/register', data, headers })

}