import axios, { AxiosRequestConfig } from 'axios';
import history from './history';
import { getAuthData } from './storage';

export const BASE_URL = "https://192.168.15.13:8443"

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

    const headers: any = { ...config.headers};

    if (config.withCredentials) {

        headers.Authorization = `Bearer ${getAuthData()?.token}`;

    }

    return axios({ ...config, baseURL: BASE_URL, headers, data: config.data });

}

export const requestBackendLogin = (loginData: LoginData) => {

    const headers = {
        'Content-Type': 'application/json',
        'Authorization': 'No auth'
    }

    const data = JSON.stringify({

        email: loginData.username,
        password: loginData.password,

    });

    return axios({ method: 'POST', baseURL: BASE_URL, url: '/api/auth/v2/authenticate', data, headers });

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

    });

    return axios({ method: 'POST', baseURL: BASE_URL, url: '/api/auth/v2/register', data, headers });

}

axios.interceptors.request.use(function (config) {
    return config;
}, function (error) {
    return Promise.reject(error);

});

axios.interceptors.response.use(function (response) {
    return response;
}, function (error) {

    const status = error.response.status;

    if (status === 401 /* status === 403 --handling do 403 exibido na página */) {
        history.push('/admin/auth/login');
    }

    return Promise.reject(error);
    
});