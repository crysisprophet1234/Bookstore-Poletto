import { ReactComponent as AuthImg } from '../../assets/images/auth-img.svg';
import { Route, Routes } from 'react-router-dom';

import './styles.css';
import Login from './Login';
import Signup from './Signup';

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

                    <Route path="/recover" element={<h2>Recover</h2>} />

                </Routes>

            </div>

        </div>

    )

}

export default Auth;