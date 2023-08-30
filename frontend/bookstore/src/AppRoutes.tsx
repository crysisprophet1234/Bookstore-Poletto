import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import Catalog from './pages/Catalog';
import Admin from './pages/Admin';
import Auth from './pages/Auth';
import BookDetails from './pages/BookDetails';
import Home from './pages/Home';
import NotFound from './pages/NotFound';
import ToastMessage from './components/ToastMessage';

const AppRoutes = () => (

    <BrowserRouter>

        <Navbar />

        <ToastMessage />

        <Routes>

            <Route path="/" element={<Home />} />

            <Route path="/books" element={<Catalog />} />

            <Route path="/books/:bookId/*" element={<BookDetails />} />

            <Route path="/auth/*" element={<Auth />} />

            <Route path="/auth" element={<Navigate to="/auth/login" />} />

            <Route path="/admin/*" element={<Admin />} />

            <Route path="/admin" element={<Navigate to="/admin/books" />} />

            <Route path="*" element={<NotFound />} />

        </Routes>

    </BrowserRouter>

);

export default AppRoutes;