import { NavLink } from 'react-router-dom';
import './styles.css';

const Navbar = () => {

    return (

        <nav className="admin-nav-container">

            <ul>

                <li>
                    <NavLink
                        to='/admin/books'
                        className={({ isActive }) => isActive ? 'admin-nav-item active bg-primary border border-dark' : 'admin-nav-item'}
                    >
                        Livros
                    </NavLink>
                </li>

                <li>
                    <NavLink
                        to='/admin/reserves'
                        className={({ isActive }) => isActive ? 'admin-nav-item active bg-primary border border-dark' : 'admin-nav-item'}
                    >
                        Reservas
                    </NavLink>
                </li>

                <li>
                    <NavLink
                        to='/admin/users'
                        className={({ isActive }) => isActive ? 'admin-nav-item active bg-primary border border-dark' : 'admin-nav-item'}
                    >
                        Usuários
                    </NavLink>
                </li>

            </ul>

        </nav>

    )

}

export default Navbar;