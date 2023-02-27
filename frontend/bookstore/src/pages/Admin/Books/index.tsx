import { Route, Switch } from "react-router-dom";
import Form from "./Form";
import List from "./List";

const Products = () => {

    return (
        <Switch>
            <Route path="/admin/books" exact>
                <List />
            </Route>
            <Route path="/admin/books/:bookId">
                <Form />
            </Route>
        </Switch>
    )
}

export default Products;