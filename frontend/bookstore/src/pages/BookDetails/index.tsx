import './styles.css';

import { ReactComponent as ArrowIcon } from "../../assets/images/arrow.svg";

import { Link, useParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { AxiosRequestHeaders } from 'axios';
import { requestBackend } from '../../utils/requests';
import BookInfoLoader from './BookInfoLoader';
import BookDetailsLoader from './BookDetailsLoader';
import { Book } from '../../types/book';
import BookStatus from '../../components/BookStatus';
import ReservationModal from '../../components/ReservationModal/ReservationModal';

type UrlParam = {

    bookId: string;

}

const BookDetails = () => {

    const { bookId } = useParams<UrlParam>();

    const [book, setBook] = useState<Book>();

    const [showModal, setShowModal] = useState(false);

    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {

        const params = {

            method: 'GET',
            url: `/api/v1/books/${bookId}`,
            headers: {} as AxiosRequestHeaders

        }

        setIsLoading(true)

        requestBackend(params)
            .then(response => {
                setBook(response.data);
            })
            .finally(() => {
                setIsLoading(false)
            })
    }, [bookId])

    return (

        <div className="book-details-container">

            <div className="book-details-card">

                <div className="goback-container">
                    <Link to="/books" style={{ display: 'flex', alignItems: 'center' }}>
                        <ArrowIcon />
                        <h2>VOLTAR</h2>
                    </Link>
                </div>

                <div className="row">

                    <div className="col-xl-6">

                        {isLoading ? (<BookInfoLoader />) :

                            (
                                <>

                                    {window.innerWidth > 1200 ? '' :

                                        <h1 style={{ textAlign: 'center' }}><u>{book?.name}</u></h1>

                                    }

                                    <div className="img-container">
                                        <img
                                            src={book?.imgUrl}
                                            alt={book?.name}
                                        />
                                    </div>

                                    <div className="book-desc-container">
                                        <span>
                                            <h2 className='mb-2'>{book?.author.name}</h2>
                                            <p>Data de lançamento {book?.releaseDate}</p>
                                        </span>
                                        <hr></hr>
                                        <h2>Descrição</h2>
                                        <p>
                                            &nbsp; Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus nec euismod libero. Nunc et molestie lectus.
                                            Quisque sollicitudin commodo turpis, in condimentum sapien. Vivamus id hendrerit risus. Mauris euismod vel arcu sit amet varius.
                                            Integer elementum arcu vitae lacinia ornare. Vestibulum ac mi accumsan, rutrum justo tempus, mattis arcu. Pellentesque.
                                        </p>
                                    </div>
                                </>
                            )
                        }

                    </div>

                    <div className="col-xl-6">

                        {isLoading ? (<BookDetailsLoader />) :

                            (
                                <div className="title-container">
                                    <h1 className="mb-5">{book?.name}</h1>
                                    <span className='mb-5'>
                                        <div className="categories mb-2">
                                            {book?.categories.map(category => {
                                                return (

                                                    <p key={category.id} >{category.name}</p>

                                                )
                                            })}
                                        </div>
                                        <p>Código {book?.id}</p>
                                    </span>
                                    <div onClick={() => setShowModal(true)}>
                                        <BookStatus status={book?.status} />
                                    </div>

                                            //adicionar autenticao
                                    <ReservationModal onClose={() => setShowModal(false)} show={showModal} book={book as Book} />

                                </div>

                            )
                        }

                    </div>

                </div>

            </div>

        </div>

    )
}

export default BookDetails;