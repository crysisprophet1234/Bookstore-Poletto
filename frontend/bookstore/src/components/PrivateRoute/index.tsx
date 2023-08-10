import { Navigate, useLocation } from 'react-router-dom'
import { isAuthenticated } from '../../utils/auth'
import { ReactNode } from 'react'

type Props = {
  element: ReactNode
}

const PrivateRoute = ({ element }: Props) => {

  const location = useLocation()

  return (

      isAuthenticated() ? <> {element} </> : <Navigate to='/auth/login' state={{ from: location }} /> 

  )

}

export default PrivateRoute
