import './assets/styles/custom.scss';
import './App.css';
import AppRoutes from './AppRoutes';
import { AuthContext, AuthContextData } from './AuthContext';
import { useState } from 'react';

function App() {

  const [authContextData, setAuthContextData] = useState<AuthContextData>({ authenticated: false })

  return (
    <AuthContext.Provider value={{ authContextData, setAuthContextData }}>
      <AppRoutes />
  </AuthContext.Provider>
  );
}

export default App;
