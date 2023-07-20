import jwtDecode from 'jwt-decode';
import { getAuthData } from './storage';

export type Authority = 'ROLE_CUSTOMER' | 'ROLE_OPERATOR' | 'ROLE_ADMIN';

export type TokenData = {
  exp: number;
  iat: number;
  sub: string;
  authorities: Authority[];
};

export const getTokenData = (): TokenData | undefined => {
  try {
    return jwtDecode(getAuthData().token) as TokenData;
  } catch (error) {
    return undefined;
  }
};

export const isAuthenticated = (): boolean => {
  let tokenData = getTokenData();
  return tokenData && tokenData.exp * 1000 > Date.now() ? true : false;
};

export const hasAnyAuthorities = (authorities: Authority[]): boolean => {
  if (typeof (authorities) === 'undefined') {
    return true;
  }

  const tokenAuthorities = getTokenData()?.authorities;

  tokenAuthorities?.forEach(a => {
    console.log(a)
    console.log(authorities[1])
    if (a.toString() === authorities[1]) {
      return true;
    }
  })

  return false;

};