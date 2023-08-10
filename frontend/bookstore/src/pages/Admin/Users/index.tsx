import { AxiosRequestConfig, AxiosRequestHeaders } from 'axios';
import { useEffect, useState } from 'react';
import { User } from '../../../types/user';
import { SpringPage } from '../../../types/vendor/spring';
import { requestBackend } from '../../../utils/requests';
import './styles.css'
import { useNavigate } from 'react-router-dom';

const Users = () => {

    const [page, setPage] = useState<SpringPage<User>>();

    const history = useNavigate()

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
            history('/admin/users')
        });
    };

    return (

        <div className="user-container">

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