import './styles.css'


import { useEffect, useState } from 'react'
import { MoonLoader } from 'react-spinners'
import { Book } from '../../types/book'
import BookStatus from '../BookStatus'

type Props = {

    book: Book

}

const BookCard = ({ book }: Props) => {

    const [isLoading, setIsLoading] = useState(true)

    const [imageUrl, setImageUrl] = useState('')

    function thumborUrl(url: string) {

        const thumborServer = 'http://localhost:8888/unsafe'

        const imageSize = '0x225'

        return `${thumborServer}/${imageSize}/${url}`

    }

    useEffect(() => {

        const useThumbor = false

        const image = new Image()
        image.src = useThumbor ? thumborUrl(book.imgUrl) : book.imgUrl

        const handleImageLoad = () => {
            setImageUrl(image.src)
            setIsLoading(false)
        }

        const handleImageError: OnErrorEventHandler = (event) => {

            image.src = book.imgUrl

            setImageUrl(image.src)

            setIsLoading(false)

        }

        image.onload = handleImageLoad
        image.onerror = handleImageError

        return () => {

            image.onload = null
            image.onerror = null
        }

    }, [book.imgUrl])

    return (

        <div className='base-card book-card'>

            <div className='card-top-container'>

                {isLoading ?
                    (

                        <div className='spinner-container'>
                            <MoonLoader loading={isLoading} color='#0044E0' speedMultiplier={0.65} />
                        </div>
                    )
                    :
                    (
                        <img
                            src={imageUrl}
                            alt={book.name}
                            loading='lazy'
                        />
                    )
                }

            </div>

            <div className='card-bottom-container'>

                <h2 data-bs-toggle='tooltip' data-bs-placement='top' title={book.name}>
                    {book.name}
                </h2>

                <BookStatus status={book.status} />

            </div>

        </div>

    )

}

export default BookCard