import { Route, Routes } from 'react-router-dom'
import AuthImg from '../../assets/images/auth-img.svg?react'

import Login from './Login'
import Recover from './Recover'
import Signup from './Signup'
import './styles.css'

const Auth = () => {

    return (

        <div className="auth-container">

            <div className="auth-banner-container">

                <h1>Descubra os mais variados livros</h1>

                <AuthImg />

            </div>

            <div className="auth-form-container">

                <Routes>

                    <Route path="/login" element={<Login />} />

                    <Route path="/signup" element={<Signup />} />

                    <Route path="/recover" element={<Recover />} />

                </Routes>

            </div>

        </div>

    )

}

export default Auth