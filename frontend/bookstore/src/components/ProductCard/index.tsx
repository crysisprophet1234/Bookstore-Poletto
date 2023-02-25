import './styles.css';


import { Books } from '../../types/books';
import ProductPrice from '../ProductPrice';

type Props = {

    product : Books;

}

const ProductCard = ( { product } : Props) => {

    return (
        <div className="base-card product-card">
            <div className="card-top-container">
                <img src={product.imgUrl} alt={product.name} />
            </div>
            <div className="card-bottom-container">
                <h6>{product.name}</h6>
                <ProductPrice price = {product.id}/>
            </div>
        </div>
    );
}

export default ProductCard;