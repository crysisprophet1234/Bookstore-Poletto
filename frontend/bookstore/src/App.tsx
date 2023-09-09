import './assets/styles/custom.scss'

import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import { useEffect, useState } from 'react'
import './App.css'
import AppRoutes from './AppRoutes'
import { AuthContext, AuthContextData } from './AuthContext'
import { getTokenData, isAuthenticated } from './utils/auth'
import { removeAuthData } from './utils/storage'

function App() {

  const [authContextData, setAuthContextData] = useState<AuthContextData>({ authenticated: false })

  useEffect(() => {

    if (isAuthenticated()) {

      setAuthContextData({
        authenticated: true,
        tokenData: getTokenData()
      })

    } else {

      setAuthContextData({
        authenticated: false,
      })

      removeAuthData()

    }

  }, [setAuthContextData])

  return (
    <AuthContext.Provider value={{ authContextData, setAuthContextData }}>
      <AppRoutes />
    </AuthContext.Provider>
  )
}

export default App
