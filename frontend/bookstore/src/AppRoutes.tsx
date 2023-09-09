import { Suspense, lazy } from 'react'
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import Navbar from './components/Navbar'
import ToastMessage from './components/ToastMessage'
import Home from './pages/Home'
import Loading from './pages/Loading'
const Catalog = lazy(() => import('./pages/Catalog'))
const BookDetails = lazy(() => import('./pages/BookDetails'))
const Auth = lazy(() => import('./pages/Auth'))
const Admin = lazy(() => import('./pages/Admin'))
const ErrorFallback = lazy(() => import('./components/ErrorFallback'))

const AppRoutes = () => (

    <BrowserRouter>

        <Navbar />

        <ToastMessage />

        <Routes>

            <Route path='/' element={<Home />} />

            <Route path='/books' element={
                <Suspense fallback={<Loading />}>
                    <Catalog />
                </Suspense>
            } />

            <Route path='/books/:bookId/*' element={
                <Suspense fallback={<Loading />}>
                    <BookDetails />
                </Suspense>
            } />

            <Route path='/auth/*' element={
                <Suspense fallback={<Loading />}>
                    <Auth />
                </Suspense>
            } />

            <Route path='/auth' element={<Navigate to='/auth/login' />} />

            <Route path='/admin/*' element={
                <Suspense fallback={<Loading />}>
                    <Admin />
                </Suspense>
            } />

            <Route path='/admin' element={<Navigate to='/admin/books' />} />

            <Route path='*' element={
                <Suspense fallback={<Loading />}>
                    <ErrorFallback errorCode={404} />
                </Suspense>
            } />

        </Routes>

    </BrowserRouter>

)

export default AppRoutes