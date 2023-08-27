import './styles.css';

import { useContext, useEffect } from 'react';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import { AuthContext } from '../../AuthContext';
import { getTokenData, isAuthenticated } from '../../utils/auth';
import { removeAuthData } from '../../utils/storage';

const Navbar = () => {

  const { authContextData, setAuthContextData } = useContext(AuthContext);

  const history = useNavigate()

  useEffect(() => {

    if (isAuthenticated()) {

      setAuthContextData({
        authenticated: true,
        tokenData: getTokenData()
      });

    } else {
      setAuthContextData({
        authenticated: false,
      });
    }

  }, [setAuthContextData]);

  const handleLogoutClick = (event: React.MouseEvent<HTMLAnchorElement>) => {

    event.preventDefault();

    removeAuthData();

    setAuthContextData({ authenticated: false });

    history('/');

  }

  return (
    <nav className="navbar navbar-expand-md navbar-dark bg-primary main-nav">
      <div className="container-fluid">
        <Link to="/" className="nav-logo-text">
          <h4>Bookstore</h4>
        </Link>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#dscatalog-navbar"
          aria-controls="dscatalog-navbar"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="dscatalog-navbar">
          <ul className="navbar-nav offset-md-2 main-menu">
            <li>
              <NavLink to="/" className={({ isActive }) => isActive ? 'active' : ''} end>
                HOME
              </NavLink>
            </li>
            <li>
              <NavLink to="/books" className={({ isActive }) => isActive ? 'active' : ''} end>
                CATÁLOGO
              </NavLink>
            </li>
            <li>
              <NavLink to="/admin" className={({ isActive }) => isActive ? 'active' : ''} end>
                ADMIN
              </NavLink>
            </li>
          </ul>
        </div>

        <div className="nav-login-logout">
          {authContextData.authenticated ?
            (
              <>
                <span className="nav-username">{authContextData.tokenData?.sub}</span>
                <a href="logout" onClick={handleLogoutClick}>LOGOUT</a>
              </>
            )
            :
            (
              <Link to="/auth">
                LOGIN
              </Link>
            )
          }
        </div>

      </div>
    </nav>
  );
};

export default Navbar;
