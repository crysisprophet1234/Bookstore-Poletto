import './styles.css'

import { Link } from 'react-router-dom'
import CategoryBadge from '../../pages/Admin/Books/CategoryBadge'
import { Book } from '../../types/book'

type Props = {

  book: Book
  showEdit?: boolean

}

const ProductCrudCard = ({ book, showEdit }: Props) => {

  return (

    <div className='base-card product-crud-card'>

      <div className='product-crud-card-top-container'>

        <img src={book?.imgUrl} alt={book?.name} loading='lazy' />

      </div>

      <div className='product-crud-card-description'>

        <div className='product-crud-card-bottom-container'>

          <h6>{book.name}</h6>

          <span>{book.status === 'BOOKED' ? 'RESERVADO' : 'DISPONÍVEL'}</span>

        </div>

        <div className='product-crud-categories-container'>

          {book.categories.map((category) => (
            <CategoryBadge name={category.name} key={category.id} />
          ))}

        </div>

      </div>

      {showEdit &&

        <div className='product-crud-card-buttons-container'>

          <Link to={`/admin/books/${book.id}`}>

            <button className='btn btn-outline-primary product-crud-card-button'>
              EDITAR
            </button>

          </Link>

        </div>

      }

    </div>

  )

}

export default ProductCrudCard
