import { Role } from "./auth";

const tokenKey = 'authData';

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

export const saveAuthData = (obj: LoginResponse) => {
  localStorage.setItem(tokenKey, JSON.stringify(obj));
};

export const getAuthData = () => {
  const str = localStorage.getItem(tokenKey) ?? '{}';
  return JSON.parse(str) as LoginResponse;
};

export const removeAuthData = () => {
  localStorage.removeItem(tokenKey);
};