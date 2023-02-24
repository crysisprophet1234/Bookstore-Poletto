import { Route, Switch } from "react-router-dom";
import Login from "./Login";
import Signup from "./Signup";




const Auth = () => {

    return (

        <>

        <h1>Auth</h1>

        <Switch>

            <Route path="/auth/login">
                <Login />
            </Route>

            <Route path="/auth/signup">
                <Signup />
            </Route>

        </Switch>

        </>

    )

}

export default Auth;