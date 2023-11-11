import { AxiosRequestConfig, AxiosRequestHeaders } from 'axios'
import { useCallback, useEffect, useState } from 'react'
import { PulseLoader } from 'react-spinners'
import { toast } from 'react-toastify'
import Pagination from '../../../../components/Pagination'
import { User } from '../../../../types/user'
import { SpringPage } from '../../../../types/vendor/spring'
import { requestBackend } from '../../../../utils/requests'
import UsersFilter, { UserFilterData } from '../UsersFilter'

type ControlComponentsData = {
    activePage: number
    filterData: UserFilterData
}

const List = () => {

    const [page, setPage] = useState<SpringPage<User>>()

    const [isLoading, setIsLoading] = useState(false)

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

        setIsLoading(true)

        requestBackend(params)
            .then((response) => {
                setPage(response.data)
                setIsLoading(false)
            })
            .catch((error) => {
                toast.error(`Falha ao buscar usuários: \n ${error.response.data.message}`)
            })

    }, [controlComponentsData])

    useEffect(() => {
        fetchUsers()
    }, [fetchUsers])

    /*
    const optionsProps: OptionProps[] = [
        { name: 'id', placeholder: 'Código', type: 'number', autocomplete: 'off' },
        { name: 'name', placeholder: 'Nome ou e-mail', type: 'text', autocomplete: 'off' },
        {
            name: 'role', placeholder: 'Nível de acesso', type: 'text', isMulti: false, isCleareable: true, options: [
                { label: 'Admnistrador', value: 'ROLE_ADMIN' },
                { label: 'Operator', value: 'ROLE_OPERATOR' }
            ]
        }
    ]
    */

    return (

        <>

            <UsersFilter onSubmitFilter={handleSubmitFilter} isSubmiting={isLoading} />

            {/*<Filter<UserFilter> onSubmitFilter={handleSubmitFilterTest} optionsProps={optionsProps} />*/}

            <div className='mt-3 d-flex justify-content-center'>
                <Pagination
                    forcePage={page?.number}
                    pageCount={page?.totalPages || 1}
                    range={window.innerWidth > 768 ? 3 : 0}
                    onChange={handlePageChange}
                />
            </div>

            {isLoading ? (

                <div className='d-flex justify-content-center align-items-center mt-4'>
                    <PulseLoader loading={isLoading} speedMultiplier={0.65} color='#0044E0' size={20} margin={15} />
                </div>

            ) : (

                <table className='table table-hover border-primary table-bordered table-striped'>

                    <thead>

                        <tr>
                            <th scope='col'>#</th>
                            <th scope='col'>EMAIL</th>
                            <th scope='col'>NOME</th>
                        </tr>

                    </thead>

                    <tbody>

                        {

                            page?.content.length !== 0 ? (

                                page?.content.map((user) => (

                                    <tr key={user.id}>
                                        <td>{user.id}</td>
                                        <td>{user.email}</td>
                                        <td>{user.firstname + ' ' + user.lastname}</td>
                                    </tr>

                                ))

                            ) : (

                                <tr>
                                    <td colSpan={3} className='text-center'>Nenhum usuário encontrado</td>
                                </tr>

                            )
                        }

                    </tbody>

                </table>

            )

            }

        </>

    )

}

export default List