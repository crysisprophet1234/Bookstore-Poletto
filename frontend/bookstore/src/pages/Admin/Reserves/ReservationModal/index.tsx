import { AxiosRequestConfig } from 'axios'
import { useState } from 'react'
import { Reservation } from '../../../../types/reservation'
import { findLinkByRel } from '../../../../utils/findLinkByRel'
import { requestBackend } from '../../../../utils/requests'
import './styles.css'

type Props = {

    visible: boolean
    reservation: Reservation
    onConfirm: () => void
    onCancel: () => void

}

type ModalProps = {

    header: React.ReactNode
    body: React.ReactNode
    footer: React.ReactNode

}

const ModalContent = ({ header, body, footer }: ModalProps) => (

    <div className='modal-dialog modal-dialog-centered'>

        <div className='modal-content'>

            {header}

            {body}

            {footer}

        </div>

    </div>

)

const ReservationModal = ({ visible, reservation, onConfirm, onCancel }: Props) => {

    const [isFirstModalVisible, setFirstModalVisibility] = useState(true)

    const [isSecondModalVisible, setSecondModalVisibility] = useState(false)

    const [returnSuccess, setReturnSuccess] = useState(false)

    const handleReturn = () => {

        const url = findLinkByRel(reservation.links, 'return')

        const config: AxiosRequestConfig = {

            method: url?.type,
            url: url?.href,
            withCredentials: true,

        }

        requestBackend(config)
            .then((response) => setReturnSuccess(true))
            .catch((error) => setReturnSuccess(false))
            .finally(() => {
                setFirstModalVisibility(false)
                setSecondModalVisibility(true)
            })

    }

    if (!visible) {
        return null
    }

    const returnConfirmationModal = (

        <ModalContent

            header={

                <div className='modal-header'>

                    <h5 className='modal-title'>Retornar livros - Reserva #{reservation.id}</h5>

                    <button
                        type='button'
                        className='btn-close'
                        onClick={onCancel}
                        data-bs-dismiss='modal'
                        aria-label='Close'
                    >
                    </button>

                </div>

            }

            body={

                <div className='modal-body'>

                    <p>Tem certeza que deseja retornar livros e finalizar reserva?</p>

                    <ul>

                        {reservation.books.map((book) => (

                            <li key={book.id}>
                                <div className='book-list'>
                                    <p>#{book.id}</p>
                                    <p>{book.name}</p>
                                </div>
                            </li>

                        ))}

                    </ul>

                </div>

            }

            footer={

                <div className='modal-footer'>

                    <button className='btn btn-dark' onClick={onCancel}>
                        Voltar
                    </button>

                    <button className='btn btn-primary' onClick={handleReturn}>
                        Confirmar
                    </button>

                </div>

            }

        />

    )

    const returnResultModal = (

        <ModalContent

            header={

                <div className='modal-header'>

                    <h1 className={`modal-title fs-5 text-${returnSuccess ? 'success' : 'danger'}`}>
                        {returnSuccess ? 'Sucesso' : 'Falha encontrada'}
                    </h1>

                    <button
                        type='button'
                        className='btn-close'
                        data-bs-dismiss='modal'
                        aria-label='Close'>
                    </button>

                </div>

            }

            body={

                <div className='modal-body'>
                    <p>{returnSuccess ? 'Reserva finalizada com sucesso.' : 'Houve erro na finalização da reserva, tente novamente.'}</p>
                </div>

            }

            footer={

                <div className='modal-footer'>

                    <button className='btn btn-primary' onClick={onConfirm}>
                        Voltar
                    </button>

                </div>

            }

        />

    )

    return (

        <>

            <div className={`modal ${isFirstModalVisible ? 'visible' : 'invisible'}`} tabIndex={-1}>
                {returnConfirmationModal}
            </div>

            <div className={`modal ${isSecondModalVisible ? 'visible' : 'invisible'}`} tabIndex={-1}>
                {returnResultModal}
            </div>

        </>

    )

}

export default ReservationModal