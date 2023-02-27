import axios, { AxiosRequestConfig } from 'axios';
import jwtDecode from 'jwt-decode';
import history from './history';

//import * as dotenv from 'dotenv'
//dotenv.config();

type LoginResponse = {

    token: string;
    token_type: string;
    expires_in: number;
    scope: string;
    firstName: string;
    id: number;
    email: string;
    roles: [{ id: number, authority: Role }];

}

/*
enum Role {

    ROLE_OPERATOR = 'ROLE_OPERATOR',
    ROLE_ADMIN = 'ROLE_ADMIN'

}
*/

type Role = 'ROLE_CUSTOMER' | 'ROLE_OPERATOR' | 'ROLE_ADMIN';

export type TokenData = {

    exp: number;
    sub: string;
    authorities: Role[];

}

//export const BASE_URL = process.env.BASE_URL;

export const BASE_URL = process.env.REACT_APP_BACKEND_URL ?? 'http://localhost:8080';

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

export const requestBackend = (config: AxiosRequestConfig, data?: any) => {

    const headers: any = { ...config.headers };

    if (config.withCredentials) {

        headers.Authorization = `Bearer ${getAuthData()?.token}`;

    }

    return axios({ ...config, baseURL: BASE_URL, headers, data });

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

    return axios({ method: 'POST', baseURL: BASE_URL, url: '/api/v1/auth/authenticate', data, headers });

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

    return axios({ method: 'POST', baseURL: BASE_URL, url: '/api/v1/auth/register', data, headers });

}

export const saveAuthData = (loginResponse: LoginResponse) => {

    localStorage.setItem('authData', JSON.stringify(loginResponse));

}

export const getAuthData = () => {

    try {

        const str = localStorage.getItem('authData') ?? "";
        return JSON.parse(str) as LoginResponse;

    } catch (err) {

    };

}

export const clearAuthData = () => {

    localStorage.clear();

}

axios.interceptors.request.use(function (config) {
    console.log('INTERCEPTOR BEFORE REQUEST')
    return config;
}, function (error) {
    console.log('INTERCEPTOR REQUEST ERROR')
    return Promise.reject(error);

});

axios.interceptors.response.use(function (response) {
    console.log('INTERCEPTOR SUCESSFUL RESPONSE')
    return response;
}, function (error) {

    const status = error.response.status;

    if (status === 401 /* status === 403 --handling do 403 exibido na página */) {
        history.push('/admin/auth/login');
    }

    return Promise.reject(error);
});

export const getTokenData = (): TokenData | undefined => {

    try {

        return jwtDecode(getAuthData()!.token) as TokenData;

    } catch (error) {

        return undefined;

    }

}

export const isAuthenticated = (): boolean | undefined => {

    const tokenData = getTokenData();

    return (tokenData && tokenData.exp * 1000 > Date.now()) ? true : false;

}

export const hasAnyRoles = (): any => {

    if (isAuthenticated()) {

        const roles = getAuthData()?.roles;

        roles?.forEach((role) => {

            console.log(role.authority)
            if (role.authority === 'ROLE_ADMIN' || role.authority === 'ROLE_OPERATOR') {
                return true;
            }

        })

        return false;
    }
};