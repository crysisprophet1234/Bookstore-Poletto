import './assets/styles/custom.scss'

import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import { useState } from 'react'
import './App.css'
import AppRoutes from './AppRoutes'
import { AuthContext, AuthContextData } from './AuthContext'

function App() {

  const [authContextData, setAuthContextData] = useState<AuthContextData>({ authenticated: false })

  return (
    <AuthContext.Provider value={{ authContextData, setAuthContextData }}>
      <AppRoutes />
    </AuthContext.Provider>
  )
}

export default App
