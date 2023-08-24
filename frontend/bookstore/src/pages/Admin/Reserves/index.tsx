import { Route, Routes } from "react-router-dom";
import List from "./List";

const Reserves = () => {

    return (

        <div className='container-fluid'>

            <h2 className='mb-3' >Reservas</h2>

            <Routes>

                <Route path="/" element={<List />} />

            </Routes>

        </div>

    )
}

export default Reserves;