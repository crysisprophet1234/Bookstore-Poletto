import Navbar from "./Navbar";
import './styles.css';
import { Switch } from "react-router-dom";
import Users from "./Users";
import PrivateRoute from "../../components/PrivateRoute";
import Books from "./Books";

const Admin = () => {

  return (

    <div className="admin-container">

      <Navbar />

      <div className="admin-content">

        <Switch>

          <PrivateRoute path="/admin/books">
            <Books />
          </PrivateRoute>

          <PrivateRoute path="/admin/categories">
            <h1>CRUD categories</h1>
          </PrivateRoute>

          <PrivateRoute path="/admin/users">
            <Users />
          </PrivateRoute>

        </Switch>

      </div>

    </div>

  )

}

export default Admin;