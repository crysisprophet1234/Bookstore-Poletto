import { ReactComponent as AuthImg } from '../../assets/images/auth-img.svg';
import { Route, Switch } from 'react-router-dom';

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

                <Switch>

                    <Route path="/auth/login">
                        <Login />
                    </Route>

                    <Route path="/auth/signup">
                        <Signup />
                    </Route>

                    <Route path="/auth/recover">
                        <h1>Card de recover</h1>
                    </Route>

                </Switch>

            </div>

        </div>

    )

}

export default Auth;