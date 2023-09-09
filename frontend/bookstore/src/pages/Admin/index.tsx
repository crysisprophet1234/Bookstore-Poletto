import { Suspense, lazy } from 'react'
import { Route, Routes } from 'react-router-dom'
import PrivateRoute from '../../components/PrivateRoute'
import Loading from '../Loading'
import Books from './Books'
import Navbar from './Navbar'
import './styles.css'
const Reserves = lazy(() => import('./Reserves'))
const Users = lazy(() => import('./Users'))

const Admin = () => {

  return (

    <div className='admin-container'>

      <Navbar />

      <div className='admin-content'>

        <Routes>

          <Route path='/books/*' element={
            <PrivateRoute element={<Books />} authority='ROLE_OPERATOR' />
          } />

          <Route path='/reserves/*' element={
            <Suspense fallback={<Loading />}>
              <PrivateRoute element={<Reserves />} authority='ROLE_OPERATOR' />
            </Suspense>
          } />

          <Route path='/users/*' element={
            <Suspense fallback={<Loading />}>
              <PrivateRoute element={<Users />} authority='ROLE_ADMIN' />
            </Suspense>
          } />

        </Routes>

      </div>

    </div >

  )

}

export default Admin