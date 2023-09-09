import { Suspense, lazy } from 'react'
import { Route, Routes } from 'react-router-dom'
import Loading from '../../Loading'
import List from './List'
const Form = lazy(() => import('./Form'))

const Products = () => {

    return (

        <Routes>

            <Route path='/' element={<List />} />

            <Route path='/:bookId' element={
                <Suspense fallback={<Loading />}>
                    <Form />
                </Suspense>
            } />

        </Routes >

    )
}

export default Products