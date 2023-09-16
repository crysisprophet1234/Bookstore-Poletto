import { AxiosRequestConfig, AxiosRequestHeaders } from 'axios'
import { useCallback, useEffect, useState } from 'react'
import { toast } from 'react-toastify'
import Pagination from '../../../../components/Pagination'
import UsersFilter, { UserFilterData } from '../../../../components/UsersFilter'
import { User } from '../../../../types/user'
import { SpringPage } from '../../../../types/vendor/spring'
import { requestBackend } from '../../../../utils/requests'

type ControlComponentsData = {
    activePage: number
    filterData: UserFilterData
}

const List = () => {

    const [page, setPage] = useState<SpringPage<User>>()

    const [controlComponentsData, setControlComponentsData] =
        useState<ControlComponentsData>({
            activePage: 0,
            filterData: {
                orderBy: 'id',
                sort: 'asc'
            },
        })

    const handlePageChange = (pageNumber: number) => {
        setControlComponentsData({ ...controlComponentsData, activePage: pageNumber })
    }

    const handleSubmitFilter = (data: UserFilterData) => {
        setControlComponentsData({ ...controlComponentsData, filterData: data })
    }

    const fetchUsers = useCallback(() => {

        const params: AxiosRequestConfig = {

            url: '/api/users/v2',
            withCredentials: true,
            headers: {} as AxiosRequestHeaders,
            params: {
                size: window.innerWidth > 768 ? 12 : 6,
                page: controlComponentsData.activePage,
                id: controlComponentsData.filterData.id,
                name: controlComponentsData.filterData.name,
                role: controlComponentsData.filterData.role,
                orderBy: controlComponentsData.filterData.orderBy,
                sort: controlComponentsData.filterData.sort
            },

        }

        requestBackend(params)
            .then((response) => {
                setPage(response.data)
            })
            .catch((err) => {
                toast.error(err)
            })

    }, [controlComponentsData])

    useEffect(() => {
        fetchUsers()
    }, [fetchUsers])

    return (

        <>

            <UsersFilter onSubmitFilter={handleSubmitFilter} />

            <div className='mt-3 d-flex justify-content-center'>
                <Pagination
                    forcePage={page?.number}
                    pageCount={page ? (page.totalPages > 0 ? page.totalPages : 1) : 0}
                    range={window.innerWidth > 768 ? 3 : 0}
                    onChange={handlePageChange}
                />
            </div>

            {true ?

                <table className='table table-hover border-primary table-bordered table-striped'>

                    <thead>

                        <tr>
                            <th scope='col'>ID</th>
                            <th scope='col'>EMAIL</th>
                            <th scope='col'>NOME</th>
                        </tr>

                    </thead>

                    <tbody>

                        {page?.content.map((user) => (

                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.email}</td>
                                <td>{user.firstname + ' ' + user.lastname}</td>
                            </tr>

                        ))}

                    </tbody>

                </table>

                : <h1>Usuário deve ser administrador para acessar</h1>

            }

        </>

    )

}

export default List