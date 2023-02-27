import { AxiosRequestConfig, AxiosRequestHeaders } from 'axios';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Book } from '../../types/book';
import { formatDate } from '../../utils/formatters';
import history from '../../utils/history';
import { getAuthData, requestBackend } from '../../utils/requests';
import './styles.css';

type FormData = {

    weeks: number;
    cliente: { id: number };
    books: [{ id: number }];

}

const ReservationModal = (props: { show: any; onClose: any | undefined; book: Book }) => {

    const [devolution, setDevolution] = useState();

    const [hasError, setHasError] = useState(false);

    const [isSubmitSuccessful, setIsSubmitSuccessful] = useState(false);

    const { register, handleSubmit } = useForm<FormData>();

    const onSubmit = (formData: FormData) => {

        const data = {
            ...formData,
            client: {
                id: getAuthData()?.id
            },
            books: [
                {
                    id: props.book.id
                }
            ]
        }

        const config: AxiosRequestConfig = {
            method: 'POST',
            url: "/api/v1/reservations",
            data,
            withCredentials: true,
          };

        console.log(data)

        requestBackend(config)

            .then(response => {
                console.log('post com sucesso')
                setDevolution(response.data.devolution)
                setHasError(false);
                setIsSubmitSuccessful(true)
                props.book.status = 'BOOKED'
            })
            .catch(err => {
                setHasError(true);
                setIsSubmitSuccessful(false);
            })

    }

    const onChange = (event: any) => {

        const element = document.getElementById('btn-submit') as HTMLButtonElement;

        //js weirdness 🙄

        if (event.target.value > 0) {
            element.disabled = false;
            element?.classList.add('active');
        }

        if (event.target.value < 1) {
            element.disabled = true;
            element?.classList.remove('active');
        }

    };

    function onClick(event: any) {
        event.stopPropagation()
        history.replace(`/books/${props.book.id}`);
    }

    if (!props.show) {
        history.replace(`/books/${props.book.id}`);
        return null
    }

    return (

        <div className="modal" onClick={props.onClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>

                <div className="modal-header">
                    <h3 className="modal-title">Reservar livro</h3>
                    <button onClick={onClick}>X</button>
                </div>

                {hasError &&
                    <div className="alert alert-danger" role="alert">
                        Erro na inserção da reserva, favor tentar novamente.
                    </div>
                }

                {isSubmitSuccessful &&
                    <>
                        <div className="alert alert-success">
                            Livro reservado com sucesso!
                            <br />
                            Devolução marcada para {formatDate(devolution)}
                        </div>
                        <button type="button" className="btn btn-primary" onClick={onClick}>Voltar</button>
                    </>
                }

                {!isSubmitSuccessful &&

                    <div className="modal-body">
                        <h2>{props.book.name}</h2>
                        <h5>{props.book.author.name}</h5>

                        <form className="modal-form" onSubmit={handleSubmit(onSubmit)}>

                            <p className='mb-2'>Quanto semanas deseja reservar?</p>

                            <select {...register("weeks")} name="weeks" id='selectWeeks' onChange={onChange} >
                                <option value={0} >Selecionar...</option>
                                <option value={1} >1 semana</option>
                                <option value={2} >2 semanas</option>
                                <option value={3} >3 semanas</option>
                                <option value={4} >4 semanas</option>
                            </select>

                            <button type="submit" id="btn-submit" disabled>
                                CONFIRMAR
                            </button>

                        </form>

                    </div>
                }

            </div>
        </div>

    );

}

export default ReservationModal;