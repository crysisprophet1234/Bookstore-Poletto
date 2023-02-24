import { Redirect, Route } from 'react-router-dom';
import { isAuthenticated } from '../../utils/requests';

type Props = {
  children: React.ReactNode;
  path: string;
};

const PrivateRoute = ({ children, path }: Props) => {

    return (
        <Route
          path={path}
          render={() =>
            isAuthenticated() ? <>{children}</> : <Redirect to={{
              pathname: "/auth/login"
            }} />
          }
        />
      );
};

export default PrivateRoute;