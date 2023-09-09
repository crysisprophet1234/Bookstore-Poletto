import { AxiosRequestConfig } from 'axios'
import { Suspense, lazy, useCallback, useEffect, useState } from 'react'
import { Reservation } from '../../../../types/reservation'
import { SpringPage } from '../../../../types/vendor/spring'
import { requestBackend } from '../../../../utils/requests'

import Pagination from '../../../../components/Pagination'
import ReservesFilter, { ReserveFilterData } from '../../../../components/ReservesFilter'

import { formatDateApi } from '../../../../utils/formatters'

import { PulseLoader } from 'react-spinners'
import './styles.css'

const LazyReservationCard = lazy(() => import('../ReservationCard'))

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
                startingDate: new Date(new Date().getTime() - 180 * 24 * 60 * 60 * 1000),
                devolutionDate: new Date(new Date().getTime() + 30 * 24 * 60 * 60 * 1000),
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
            params: {
                page: controlComponentsData.activePage,
                size: 5,
                client: controlComponentsData.filterData.clientId,
                book: controlComponentsData.filterData.bookId,
                startingDate: formatDateApi(controlComponentsData.filterData.startingDate),
                devolutionDate: formatDateApi(controlComponentsData.filterData.devolutionDate),
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
                    pageCount={page ? (page.totalPages > 0 ? page.totalPages : 1) : 0}
                    range={1}
                    onChange={handlePageChange}
                />
            </div>

            <div className='reservation-cards-container'>

                {

                    page?.content.map((reservation) => (

                        <Suspense
                            fallback={<div className='base-card loader'><PulseLoader speedMultiplier={0.65} color='#0044E0' size={20} margin={15} /></div>}
                            key={reservation.id}>
                            <LazyReservationCard reservation={reservation} reservationReload={getReservations} />
                        </Suspense>

                    ))

                }

            </div>

        </>

    )
}

export default List