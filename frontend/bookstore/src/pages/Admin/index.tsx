import Navbar from "./Navbar";
import './styles.css';
import { Routes, Route } from "react-router-dom";
import Users from "./Users";
import Books from "./Books";
import Reserves from "./Reserves";
import PrivateRoute from "../../components/PrivateRoute";

const Admin = () => {

  return (

    <div className="admin-container">

      <Navbar />

      <div className="admin-content">

        <Routes>

          <Route path='/books/*' element={<PrivateRoute element={<Books />} />} />

          <Route path="/reserves/*" element={<PrivateRoute element={<Reserves />} />} />

          <Route path="/users/*" element={<PrivateRoute element={<Users />} />} />

        </Routes>

      </div>

    </div>

  )

}

export default Admin;