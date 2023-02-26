import { Link } from 'react-router-dom';
import './styles.css';

type Props = {

    status?: string;

}

const BookStatus = ({ status }: Props) => {

    return (
        
        <div className="book-status-container primary">

            <button type='button' className={status?.toLowerCase()} disabled={status === 'BOOKED'}>
                {status === 'AVAILABLE' ? 'Reservar' : 'Reservado'}
            </button>

        </div>
    );
}

export default BookStatus;
