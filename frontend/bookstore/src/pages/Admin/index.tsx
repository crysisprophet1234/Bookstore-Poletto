import { Route, Routes } from "react-router-dom";
import PrivateRoute from "../../components/PrivateRoute";
import Books from "./Books";
import Navbar from "./Navbar";
import Reserves from "./Reserves";
import Users from "./Users";
import './styles.css';

const Admin = () => {

  return (

    <div className="admin-container">

      <Navbar />

      <div className="admin-content">

        <Routes>

          <Route path='/books/*' element={<PrivateRoute element={<Books />} authority="ROLE_OPERATOR" />} />

          <Route path="/reserves/*" element={<PrivateRoute element={<Reserves />} authority="ROLE_OPERATOR" />} />

          <Route path="/users/*" element={<PrivateRoute element={<Users />} authority="ROLE_ADMIN" />} />

        </Routes>

      </div>

    </div>

  )

}

export default Admin;