import './styles.css'

import { ReactComponent as ArrowIcon } from '../../assets/images/arrow.svg'

import { AxiosRequestHeaders } from 'axios'
import { Suspense, useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { MoonLoader } from 'react-spinners'
import BookStatus from '../../components/BookStatus'
import { Book } from '../../types/book'
import { formatDate } from '../../utils/formatters'
import { requestBackend } from '../../utils/requests'
import BookDetailsLoader from './BookDetailsLoader'
import BookInfoLoader from './BookInfoLoader'

type UrlParam = {

    bookId: string

}

const BookDetails = () => {

    const { bookId } = useParams<UrlParam>()

    const [book, setBook] = useState<Book>()

    const [isLoading, setIsLoading] = useState(false)

    const [isImageLoading, setIsImageLoading] = useState(true)

    useEffect(() => {

        const params = {

            method: 'GET',
            url: `/api/books/v2/${bookId}`,
            headers: {} as AxiosRequestHeaders

        }

        setIsLoading(true)

        requestBackend(params)
            .then(response => {
                setBook(response.data)
            })
            .finally(() => {
                setIsLoading(false)
            })

    }, [bookId])

    return (

        <div className='book-details-container'>

            <div className='book-details-card'>

                <div className='goback-container'>

                    <Link to='/books' style={{ display: 'flex', alignItems: 'center' }}>
                        <ArrowIcon />
                        <h2>VOLTAR</h2>
                    </Link>

                </div>

                <div className='row'>

                    <div className='col-xl-6'>

                        {isLoading ? (<BookInfoLoader />) :

                            (
                                <>

                                    <div className='img-container'>

                                        <Suspense fallback={<MoonLoader loading={isImageLoading} />}>
                                            <img
                                                src={book?.imgUrl}
                                                alt={book?.name}
                                                loading='lazy'
                                                onLoad={e => setIsImageLoading(false)}
                                            />
                                        </Suspense>

                                    </div>

                                    <div className='book-desc-container'>
                                        <span>
                                            <h2 className='mb-2'>{book?.author.name}</h2>
                                            <p>Data de lançamento {formatDate(book?.releaseDate)}</p>
                                        </span>
                                        <hr></hr>
                                        <h2>Descrição</h2>
                                        <p>
                                            &nbsp Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus nec euismod libero. Nunc et molestie lectus.
                                            Quisque sollicitudin commodo turpis, in condimentum sapien. Vivamus id hendrerit risus. Mauris euismod vel arcu sit amet varius.
                                            Integer elementum arcu vitae lacinia ornare. Vestibulum ac mi accumsan, rutrum justo tempus, mattis arcu. Pellentesque.
                                        </p>
                                    </div>
                                </>
                            )
                        }

                    </div>

                    <div className='col-xl-6'>

                        {isLoading ? (<BookDetailsLoader />) :

                            (
                                <div className='title-container'>

                                    <h1 className='mb-5'>{book?.name}</h1>

                                    <span className='mb-5'>

                                        <div className='categories mb-2'>

                                            {book?.categories.map(category => {

                                                return (

                                                    <span
                                                        className='badge rounded-pill bg-primary'
                                                        key={category.id}
                                                    >
                                                        {category.name}
                                                    </span>

                                                )
                                            })}

                                        </div>

                                        <p>Código {book?.id}</p>

                                    </span>

                                    <BookStatus status={book?.status} />

                                    {/* TODO: será implementado de volta quando houver carrinho de livros */}

                                    {/* <Link to={`/books/${book?.id}/reservation`} onClick={() => setShowModal(true)}>
                                        <BookStatus status={book?.status} />
                                    </Link>

                                    <PrivateRoute
                                        path={`/books/${book?.id}/reservation`}
                                        element={<ReservationModal onClose={() => setShowModal(false)} show={showModal} book={book as Book} />}
                                    /> */}

                                </div>

                            )
                        }

                    </div>

                </div>

            </div>

        </div>

    )
}

export default BookDetails