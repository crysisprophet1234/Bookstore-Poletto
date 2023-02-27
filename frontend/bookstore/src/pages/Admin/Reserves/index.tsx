import { AxiosRequestConfig, AxiosRequestHeaders } from 'axios';
import { useEffect, useState } from 'react';
import { Reservation } from '../../../types/reservation';
import { User } from '../../../types/user';
import { SpringPage } from '../../../types/vendor/spring';
import history from '../../../utils/history';
import { getAuthData, requestBackend } from '../../../utils/requests';

const Reserves = () => {

    const [reservations, setReservations] = useState();

    let reservas: [] = [];

    useEffect(() => {

        const params: AxiosRequestConfig = {

            url: '/api/v1/reservations',
            withCredentials: true,
            params: {
                client: getAuthData()?.id
            }

        };

        requestBackend(params)
            .then((response) => {
                console.log(response.data as [])
                setReservations(response.data);
                reservas = response.data;
                console.log(reservations)
                console.log(reservas);
            })
            .catch((err) => {
            })

    }, []);

    return (

        <div>

            {



            }

        </div>

    );
};

export default Reserves;