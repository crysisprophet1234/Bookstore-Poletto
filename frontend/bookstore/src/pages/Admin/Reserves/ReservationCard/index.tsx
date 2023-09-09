import { Reservation } from '../../../../types/reservation'

import { useState } from 'react'
import { BiSolidDownArrow } from 'react-icons/bi'
import ProductCrudCard from '../../../../components/ProductCrudCard'
import { formatDate } from '../../../../utils/formatters'
import ReservationModal from '../ReservationModal'
import ReservationStatus from '../ReservationStatus'
import './styles.css'

type Props = {

    reservation: Reservation
    reservationReload: () => void

}

const ReservationCard = ({ reservation, reservationReload }: Props) => {

    const [isModalOpen, setIsModalOpen] = useState(false);

    const handleReturn = () => {

        setIsModalOpen(false)

        reservationReload()

    }

    return (

        <div className='base-card reservation-card p-4 rounded-0'>

            <div className='folded-section'>

                <div className='field-value mx-2'>

                    <div className='upper-section my-3'>
                        <h3>NÚMERO DA RESERVA</h3>
                    </div>

                    <div className='bottom-section my-3'>
                        <h3>#{reservation.id}</h3>
                    </div>

                </div>

                <div className='field-value mx-2 status-field'>

                    <div className='upper-section my-3'>
                        <h3>STATUS</h3>
                    </div>

                    <div className='bottom-section my-3'>
                        <ReservationStatus statusInput={reservation.status} />
                    </div>

                </div>

                <div className='field-value mx-2'>

                    <div className='upper-section my-3'>
                        <h3>INÍCIO</h3>
                    </div>

                    <div className='bottom-section my-3'>
                        <h3>{formatDate(reservation.moment)}</h3>
                    </div>

                </div>

                <div className='field-value mx-2'>

                    <div className='upper-section my-3'>
                        <h3>SEMANAS</h3>
                    </div>

                    <div className='bottom-section my-3'>
                        <h3>{reservation.weeks}</h3>
                    </div>

                </div>

                <div className='field-value mx-2'>

                    <div className='upper-section my-3'>
                        <h3>RETORNO</h3>
                    </div>

                    <div className='bottom-section my-3'>
                        <h3>{formatDate(reservation.devolution)}</h3>
                    </div>

                </div>

                <div className='field-value mx-2'>

                    <div className='upper-section my-3'>
                        <h3>CLIENTE</h3>
                    </div>

                    <div className='bottom-section my-3'>
                        <h3>{reservation.client.firstname + ' ' + reservation.client.lastname}</h3>
                    </div>

                </div>

                <div className='field-value mx-2'>

                    <div className='upper-section my-3'>
                        <h3>LIVROS</h3>
                    </div>

                    <div className='bottom-section my-3'>
                        <h3 className='folded-books'>
                            {reservation.books.map((book) => book.name).join(', ')}
                        </h3>
                    </div>

                </div>

                <div className='expand-section mx-2'>

                    <button
                        className='expand-btn text-primary'
                        name='expand-reservation-card-button'
                        type='button'
                        data-bs-toggle='collapse'
                        data-bs-target={`#reservation-card-collapse${reservation.id}`}
                        aria-expanded='false'
                        aria-controls='collapseExample'
                        aria-label='expand-reservation-card-button'
                    >
                        {window.innerWidth < 768 ? <BiSolidDownArrow /> : <span>Expandir <BiSolidDownArrow /></span>}
                    </button>

                </div>

            </div>

            <div className='expanded-section collapse' id={`reservation-card-collapse${reservation.id}`}>

                <hr />

                <h4>LIVROS</h4>

                <div className='books-container'>

                    {

                        reservation.books.map((book) => (

                            <ProductCrudCard book={book} key={book.id} />

                        ))
                    }

                </div>

                <div className='bottom-section'>

                    <div className='client-container'>

                        <h4>CLIENTE #{reservation.client.id}</h4>

                        <h5 className='mt-3'><b>Nome: </b>{reservation.client.firstname + ' ' + reservation.client.lastname}</h5>

                        <h5 className='mt-1'><b>E-mail: </b>{reservation.client.email}</h5>

                    </div>

                    <div className='btn-container'>

                        <button
                            type='button'
                            className='btn btn-danger'
                            onClick={() => alert('currently disabled')}
                            disabled={reservation.status === 'FINISHED'}
                        >
                            CANCELAR
                        </button>

                        <button
                            type='button'
                            className='btn btn-primary'
                            onClick={() => setIsModalOpen(true)}
                            disabled={reservation.status === 'FINISHED'}
                        >
                            {reservation.status === 'FINISHED' ? 'FINALIZADA' : 'RETORNAR'}
                        </button>

                        <ReservationModal
                            visible={isModalOpen}
                            onConfirm={handleReturn}
                            onCancel={() => setIsModalOpen(false)}
                            reservation={reservation}
                        />

                    </div>

                </div>

            </div>

        </div>

    )

}

export default ReservationCard