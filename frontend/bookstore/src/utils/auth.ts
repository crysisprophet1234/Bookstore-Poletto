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
  if (typeof(authorities) === 'undefined') {
    return true;
  }

  const tokenData = getTokenData();

  console.log(tokenData?.authorities)

  if (tokenData !== undefined) {
    for (var i = 0; i < authorities.length; i++) {
      if (tokenData.authorities.includes(authorities[i])) {
        return true;
      }
    }

  }

  return false;
};