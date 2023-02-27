import { AxiosRequestConfig, AxiosRequestHeaders } from 'axios';
import { ReactNode, useEffect, useState } from 'react';
import { Reservation } from '../../../types/reservation';
import { SpringPage } from '../../../types/vendor/spring';
import { formatDate, formatDateTime } from '../../../utils/formatters';
import history from '../../../utils/history';
import { getAuthData, requestBackend } from '../../../utils/requests';

import '../Users/styles.css'

const Reserves = () => {

    const [reservations, setReservations] = useState<SpringPage<Reservation>>();

    useEffect(() => {

        const params: AxiosRequestConfig = {

            url: '/api/v1/reservations',
            withCredentials: true,
            headers: {} as AxiosRequestHeaders,
            params: {
                page: 0,
                size: 200,
                client: getAuthData()?.id
            }

        };

        requestBackend(params)
            .then((response) => {
                setReservations(response.data);
            })
            .catch((err) => {
            })

    }, []);

    return (

        <div>



            {reservations ?

                <table>
                    <tr>
                        <th>ID</th>
                        <th>ÍNICIO</th>
                        <th>SEMANAS</th>
                        <th>DEVOLUÇÃO</th>
                        <th>STATUS</th>
                        <th>LIVROS</th>
                    </tr>
                    <tbody>

                        {reservations.content.map((reservation) => (
                            <>
                                <tr>
                                    <td>{reservation.id}</td>
                                    <td>{formatDateTime(reservation.moment)}</td>
                                    <td>{reservation.weeks}</td>
                                    <td>{formatDate(reservation.devolution)}</td>
                                    <td>{reservation.status === 'IN_PROGRESS' ? 'Em andamento' : 'Finalizado'}</td>
                                    <td>{reservation.books.map(e => e.name + ' | ')}</td>

                                </tr>
                            </>
                        ))}

                    </tbody>
                </table>

                : <h1>Usuário deve ser administrador para acessar</h1>

            }





        </div>

    );
};

export default Reserves;