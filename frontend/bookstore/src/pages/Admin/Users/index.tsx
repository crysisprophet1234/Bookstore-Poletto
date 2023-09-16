import { Route, Routes } from "react-router-dom"
import List from "./List"

//const UserPage = lazy(() => import('path-to-userpage'))

const Users = () => {

    return (

        <div className='container-fluid'>

            <h2 className='mb-3' >Usuários</h2>

            <Routes>

                <Route path="/" element={<List />} />

                {/* <Route path="/:id" element={<UserPage />} /> */}

            </Routes>

        </div>

    )
}

export default Users