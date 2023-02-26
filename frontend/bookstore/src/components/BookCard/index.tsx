import './styles.css';


import { Book } from '../../types/book';
import BookStatus from '../BookStatus';

type Props = {

    book : Book;

}

const BookCard = ( { book } : Props) => {

    return (
        <div className="base-card book-card">
            <div className="card-top-container">
                <img src={book.imgUrl} alt={book.name} />
            </div>
            <div className="card-bottom-container">
                <h6>{book.name}</h6>
                <BookStatus status={book.status} />
            </div>
        </div>
    );
}

export default BookCard;