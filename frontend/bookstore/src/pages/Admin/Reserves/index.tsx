import { Route, Routes } from "react-router-dom";
import List from "./List";
import ReservationDetails from "./ReservationDetails";

const Reserves = () => {

    return (

        <div className='container-fluid mx-2'>

            <h2 className='mb-3' >Reservas</h2>

            <Routes>

                <Route path="/" element={<List />} />

                <Route path="/:reservationId" element={<ReservationDetails />} />

            </Routes>

        </div>

    )
}

export default Reserves;