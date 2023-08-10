import { AxiosRequestConfig, AxiosRequestHeaders } from 'axios'
import { useCallback, useEffect, useState } from 'react'
import { Reservation } from '../../../../types/reservation'
import { SpringPage } from '../../../../types/vendor/spring'
import { formatDate, formatDateTime } from '../../../../utils/formatters'
import { requestBackend } from '../../../../utils/requests'
import { FiExternalLink } from 'react-icons/fi'
//import { getAuthData } from '../../../utils/storage'

import ReservesFilter, { ReserveFilterData } from '../../../../components/ReservesFilter'
import Pagination from '../../../../components/Pagination'
import { Link } from 'react-router-dom'

type ControlComponentsData = {
    activePage: number
    filterData: ReserveFilterData
}

const List = () => {

    const [page, setPage] = useState<SpringPage<Reservation>>()

    const [controlComponentsData, setControlComponentsData] =
        useState<ControlComponentsData>({
            activePage: 0,
            filterData: {
                id: '',
                startingDate: new Date(),
                devolutionDate: new Date(),
                clientId: '',
                bookId: '',
                status: 'all'
            },
        })

    const handlePageChange = (pageNumber: number) => {
        setControlComponentsData({ activePage: pageNumber, filterData: controlComponentsData.filterData })
    }

    const handleSubmitFilter = (data: ReserveFilterData) => {
        setControlComponentsData({ activePage: 0, filterData: data })
    }

    const getReservations = useCallback(() => {

        //TODO: se tiver reservation id, deve direcionar pra página da reservation

        const config: AxiosRequestConfig = {
            method: 'GET',
            url: '/api/reservations/v2',
            withCredentials: true,
            headers: {} as AxiosRequestHeaders,
            params: {
                page: controlComponentsData.activePage,
                size: 10,
                client: controlComponentsData.filterData.clientId,
                book: controlComponentsData.filterData.bookId,
                startingDate: controlComponentsData.filterData.startingDate,
                devolutionDate: controlComponentsData.filterData.devolutionDate,
                status: controlComponentsData.filterData.status
            }
        }

        requestBackend(config)
            .then((response) => {
                setPage(response.data)
            })
            .catch((error) => {
                //FIXME: handling apropriado
                console.log(error)
            })
    }, [controlComponentsData])

    useEffect(() => {
        getReservations()
    }, [getReservations])

    return (

        <>

            <ReservesFilter onSubmitFilter={handleSubmitFilter} />

            <div className='mt-3'>
                <Pagination
                    forcePage={page?.number}
                    pageCount={page ? page.totalPages : 0}
                    range={1}
                    onChange={handlePageChange}
                />
            </div>

            <div className='table-responsive'>

                <table className='table table-striped table-hover table-bordered border-dark'>

                    <thead className='bg-primary text-white'>

                        <tr>
                            <th scope='col'>ID</th>
                            <th scope='col'>ÍNICIO</th>
                            <th scope='col'>SEMANAS</th>
                            <th scope='col'>DEVOLUÇÃO</th>
                            <th scope='col'>STATUS</th>
                            <th scope='col'>CLIENTE</th>
                            <th scope='col' className='text-center'>AÇÃO</th>
                        </tr>

                    </thead>

                    <tbody>

                        {page?.content.map((reservation) => (

                            <tr key={reservation.id}>
                                <td>{reservation.id}</td>
                                <td>{formatDateTime(reservation.moment)}</td>
                                <td>{reservation.weeks}</td>
                                <td>{formatDate(reservation.devolution)}</td>
                                <td>{reservation.status === 'IN_PROGRESS' ? 'Em andamento' : 'Finalizado'}</td>
                                <td>{reservation.client.firstname + ' ' + reservation.client.lastname}</td>
                                <td className='text-center'>
                                    <Link to={`/admin/reserves/${reservation.id}`}>
                                        <FiExternalLink />
                                    </Link>
                                </td>
                            </tr>

                        ))}

                    </tbody>

                </table>

            </div>

        </>

    )
}

export default List