import { AxiosRequestConfig, AxiosRequestHeaders } from 'axios';
import { useEffect, useState } from 'react';
import { User } from '../../../types/user';
import { SpringPage } from '../../../types/vendor/spring';
import history from '../../../utils/history';
import { requestBackend } from '../../../utils/requests';

import './styles.css'

const Users = () => {

    const [page, setPage] = useState<SpringPage<User>>();

    useEffect(() => {

        const params: AxiosRequestConfig = {

            url: '/api/users/v2',
            withCredentials: true,
            headers: {} as AxiosRequestHeaders,
            params: {
                page: 0,
                size: 200,
            },

        };

        requestBackend(params)
            .then((response) => {
                setPage(response.data);
            })
            .catch((err) => {
            })

    }, []);

    const handleDelete = (bookId: number) => {

        if (!window.confirm(`Tem certeza que deseja deletar usuário ${bookId}?`)) {
            return;
        }

        const config: AxiosRequestConfig = {
            method: 'DELETE',
            url: `/api/users/v2/${bookId}`,
            withCredentials: true,
        };

        requestBackend(config).then(() => {
            history.replace('/admin/users')
        });
    };

    return (

        <div>

            {true ?

                <table>
                    <tr>
                        <th>ID</th>
                        <th>EMAIL</th>
                        <th>EXCLUIR</th>
                    </tr>

                    <tbody>

                        {page?.content.map((item) => (
                            <>
                                <tr>
                                    <td>{item.id}</td>
                                    <td>{item.email}</td>
                                    <td><button type='button' onClick={() => handleDelete(item.id)}>X</button></td>
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

export default Users;