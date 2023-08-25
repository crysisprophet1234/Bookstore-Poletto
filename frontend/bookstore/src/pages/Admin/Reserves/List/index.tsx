import { AxiosRequestConfig, AxiosRequestHeaders } from 'axios'
import { useCallback, useEffect, useState } from 'react'
import { Reservation } from '../../../../types/reservation'
import { SpringPage } from '../../../../types/vendor/spring'
import { requestBackend } from '../../../../utils/requests'

import Pagination from '../../../../components/Pagination'
import ReservesFilter, { ReserveFilterData } from '../../../../components/ReservesFilter'

import ReservationCard from '../ReservationCard'
import './styles.css'

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
                status: 'all',
                orderBy: 'id',
                sort: 'asc'
            },
        })

    const handlePageChange = (pageNumber: number) => {
        setControlComponentsData({ activePage: pageNumber, filterData: controlComponentsData.filterData })
    }

    const handleSubmitFilter = (data: ReserveFilterData) => {
        setControlComponentsData({ activePage: 0, filterData: data })
    }

    const getReservations = useCallback(() => {

        const reservationId = controlComponentsData.filterData.id

        const url = reservationId !== '' ? `/${reservationId}` : ''

        const config: AxiosRequestConfig = {
            method: 'GET',
            url: `/api/reservations/v2${url}`,
            withCredentials: true,
            headers: {} as AxiosRequestHeaders,
            params: {
                page: controlComponentsData.activePage,
                size: 5,
                client: controlComponentsData.filterData.clientId,
                book: controlComponentsData.filterData.bookId,
                startingDate: controlComponentsData.filterData.startingDate,
                devolutionDate: controlComponentsData.filterData.devolutionDate,
                status: controlComponentsData.filterData.status,
                orderBy: controlComponentsData.filterData.orderBy,
                sort: controlComponentsData.filterData.sort
            }
        }

        requestBackend(config)
            .then((response) => {

                if (reservationId !== '') {

                    setPage({
                        content: [response.data],
                        totalPages: 1,
                        totalElements: 1,
                    } as SpringPage<Reservation>)

                } else {

                    setPage(response.data)

                }

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

            <div className='reservation-cards-container'>

                {page?.content.map((reservation) => (

                    <ReservationCard reservation={reservation} key={reservation.id} />

                ))}

            </div>

        </>

    )
}

export default List