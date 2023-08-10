import './styles.css';

type Props = {

    status?: string;

}

const BookStatus = ({ status }: Props) => {

    return (
        
        <div className="book-status-container primary">

            <button type='button' className={status?.toLowerCase()} disabled={status === 'BOOKED'}>
                {status === 'AVAILABLE' ? 'DISABLED' : 'DISABLED'}
            </button>

        </div>
    );
}

export default BookStatus;
