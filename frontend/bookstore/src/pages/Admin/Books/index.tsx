import { Route, Routes } from 'react-router-dom'
import Form from './Form'
import List from './List'

const Products = () => {

    return (

        <Routes>

            <Route path='/' element={<List />} />

            <Route path='/:bookId' element={<Form />} />

        </Routes>

    )
}

export default Products