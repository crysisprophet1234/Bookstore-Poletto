import './styles.css';

import ProductPrice from '../../../../components/BookStatus';
import { Book } from '../../../../types/book';
import CategoryBadge from '../CategoryBadge';
import { Link } from 'react-router-dom';
import { AxiosRequestConfig } from 'axios';
import { requestBackend } from '../../../../utils/requests';

type Props = {
  product: Book;
  onDelete: Function;
};

const ProductCrudCard = ({ product, onDelete }: Props) => {

  const handleDelete = (productId: number) => {

    if (!window.confirm("Tem certeza que deseja deletar?")) {
      return;
    }

    const config: AxiosRequestConfig = {
      method: 'DELETE',
      url: `/api/v1/books/${productId}`,
      withCredentials: true,
    };

    requestBackend(config).then(() => {
      onDelete();
    });
  };

  return (
    <div className="base-card product-crud-card">
      <div className="product-crud-card-top-container">
        <img src={product.imgUrl} alt={product.name} />
      </div>
      <div className="product-crud-card-description">
        <div className="product-crud-card-bottom-container">
          <h6>{product.name}</h6>
          <span>{product.status === 'BOOKED' ? 'RESERVADO' : 'DISPONÍVEL'}</span>
        </div>
        <div className="product-crud-categories-container">
          {product.categories.map((category) => (
            <CategoryBadge name={category.name} key={category.id} />
          ))}
        </div>
      </div>
      <div className="product-crud-card-buttons-container">
        <button
          onClick={() => handleDelete(product.id)}
          disabled={product.status === 'BOOKED'}
          className="btn btn-danger product-crud-card-button product-crud-card-button-first"
        >
          {product.status === 'BOOKED' ? 'RESERVADO' : 'EXCLUIR'}
        </button>
        <Link to={`/admin/products/${product.id}`}>
          <button className="btn btn-outline-primary product-crud-card-button">
            EDITAR
          </button>
        </Link>
      </div>
    </div>
  );
};

export default ProductCrudCard;
