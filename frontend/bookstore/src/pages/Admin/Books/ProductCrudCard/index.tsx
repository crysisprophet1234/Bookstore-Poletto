import './styles.css';

import { Book } from '../../../../types/book';
import CategoryBadge from '../CategoryBadge';
import { Link } from 'react-router-dom';
import { AxiosRequestConfig } from 'axios';
import { requestBackend } from '../../../../utils/requests';

type Props = {
  book: Book;
  onDelete: Function;
};

const ProductCrudCard = ({ book, onDelete }: Props) => {

  const handleDelete = (bookId: number) => {

    if (!window.confirm("Tem certeza que deseja deletar?")) {
      return;
    }

    const config: AxiosRequestConfig = {
      method: 'DELETE',
      url: `/api/books/v2/${bookId}`,
      withCredentials: true,
    };

    requestBackend(config).then(() => {
      onDelete();
    });
  };

  return (
    <div className="base-card product-crud-card">
      <div className="product-crud-card-top-container">
        <img src={book.imgUrl} alt={book.name} />
      </div>
      <div className="product-crud-card-description">
        <div className="product-crud-card-bottom-container">
          <h6>{book.name}</h6>
          <span>{book.status === 'BOOKED' ? 'RESERVADO' : 'DISPONÍVEL'}</span>
        </div>
        <div className="product-crud-categories-container">
          {book.categories.map((category) => (
            <CategoryBadge name={category.name} key={category.id} />
          ))}
        </div>
      </div>
      <div className="product-crud-card-buttons-container">
        <button
          onClick={() => handleDelete(book.id)}
          disabled={book.status === 'BOOKED'}
          className="btn btn-danger product-crud-card-button product-crud-card-button-first"
        >
          {book.status === 'BOOKED' ? 'RESERVADO' : 'EXCLUIR'}
        </button>
        <Link to={`/admin/books/${book.id}`}>
          <button className="btn btn-outline-primary product-crud-card-button">
            EDITAR
          </button>
        </Link>
      </div>
    </div>
  );
};

export default ProductCrudCard;
