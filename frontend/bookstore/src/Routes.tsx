

import { Redirect, Route, Router, Switch } from 'react-router-dom';
import Navbar from './components/Navbar';
import PrivateRoute from './components/PrivateRoute';
import Auth from './pages/Auth';
import Home from './pages/Home';
import history from './utils/history';

const AppRoutes = () => {

    return (

        <Router history={history}>

            <Navbar />

            <Switch>

                <PrivateRoute path="/">
                    <Home />
                </PrivateRoute>

                <Redirect from="/auth" to="/auth/login" exact />

                <Route path="/auth">
                    <Auth />
                </Route>

            </Switch>

        </Router>

    )

}

export default AppRoutes;