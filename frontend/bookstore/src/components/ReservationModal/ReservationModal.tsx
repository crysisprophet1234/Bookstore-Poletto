import { AxiosRequestHeaders } from 'axios';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Book } from '../../types/book';
import { getAuthData, requestBackend } from '../../utils/requests';
import './styles.css';

type Props = {

    show: boolean;

}

type FormData = {

    weeks: number;
    cliente: { id: number };
    books: [{ id: number }];

}

const ReservationModal = (props: { show: any; onClose: any | undefined; book: Book }) => {

    const [weeks, setWeeks] = useState(0);

    const [hasError, setHasError] = useState(false);

    const [isSubmitSuccessful, setIsSubmitSuccessful] = useState(false);

    const { register, handleSubmit, formState } = useForm<FormData>();

    const { errors } = formState;

    const onSubmit = (formData: FormData) => {

        const params = {

            method: 'POST',
            url: "/api/v1/reservations",
            headers: {} as AxiosRequestHeaders,

        }

        const data = {

            weeks: formData.weeks,
            client: {
                id: getAuthData()?.id
            },
            books: [
                {
                    id: props.book.id
                }
            ]

        }

        console.log(data)

        requestBackend(params, data)

            .then(response => {
                console.log('post com sucesso')
                setHasError(false);
                setIsSubmitSuccessful(true)
            })
            .catch(err => {
                setHasError(true);
                setIsSubmitSuccessful(false);
            })

    }

    if (!props.show) { return null }

    return (

        <div className="modal" onClick={props.onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>

                <div className="modal-header">
                    <h3 className="modal-title">Reservar livro</h3>
                    <button onClick={props.onClose}>X</button>
                </div>

                {hasError &&
                        <div className="alert alert-danger" role="alert">
                            Erro na inserção da reserva, favor tentar novamente.
                        </div>
                    }

                    {isSubmitSuccessful &&
                        <div className="alert alert-success">
                            Livro reservado com sucesso!
                            <br />
                            Devolução marcada para {weeks}
                        </div>
                    }

                <div className="modal-body">
                    <h2>{props.book.name}</h2>
                    <h5>{props.book.author.name}</h5>

                    <form className="modal-form" onSubmit={handleSubmit(onSubmit)}>

                        <p className='mb-2'>Quanto semanas deseja reservar?</p>

                        <select {...register("weeks")} name="weeks" id='selectWeeks' >
                            <option value="0"  >Selecionar...</option>
                            <option value="1" >1 semana</option>
                            <option value="2" >2 semanas</option>
                            <option value="3" >3 semanas</option>
                            <option value="4" >4 semanas</option>
                        </select>

                        <button type="submit">
                            CONFIRMAR
                        </button>

                    </form>

                </div>
            </div>
        </div>

    );

}

export default ReservationModal;