import { Router, Switch, Route, Redirect } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Catalog from './pages/Catalog';
import Admin from './pages/Admin';
import Auth from './pages/Auth';
import history from './utils/history';
import BookDetails from './pages/BookDetails';

const Routes = () => (

    <Router history={history}>

        <Navbar />

        <Switch>

            <Route path="/" exact>
                <Home />
            </Route>

            <Route path="/books" exact>
                <Catalog />
            </Route>

            <Redirect from="/auth" to="/auth/login" exact />

            <Route path="/auth">
                <Auth />
            </Route>

            <Redirect from="/admin" to="/admin/books" exact />

            <Route path="/admin">
                <Admin />
            </Route>

            <Route path="/books/:bookId">
                <BookDetails />
            </Route>

            <Route path="*"> {/*verificar rota default 404*/}
                <p>Ops! A página não foi encontrada.</p>
                <p>Se o erro persistir, nos informe por favor: atendimento@dscatalog.com.br</p>
                <p>Erro 404: O conteúdo não está mais disponível ou você digitou o endereço errado.</p>
            </Route>

        </Switch>
    </Router>

);

export default Routes;