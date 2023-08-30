import { ReactNode } from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { Authority, hasAuthority, isAuthenticated } from '../../utils/auth'
import NotAuthorized from '../NotAuthorized'

type Props = {
  element: ReactNode
  authority?: Authority
}

const PrivateRoute = ({ element, authority }: Props) => {

  const location = useLocation()

  function checkAuthority() {

    if (authority) {
      return hasAuthority(authority)
    }

    return true

  }

  if (!isAuthenticated()) {

    return <Navigate to='/auth/login' state={{ from: location }} />

  } else {

    if (checkAuthority()) {

      return <>{element}</>

    }

    if (!checkAuthority() && authority === 'ROLE_ADMIN') {

      return <>{<NotAuthorized />}</>

    }

    return <Navigate to='/auth/login' state={{ from: location }} />

  }

}

export default PrivateRoute
