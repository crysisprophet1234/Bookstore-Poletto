import { useParams } from "react-router-dom"

type UrlParams = {
    reservationId: string
  }

const ReservationDetails = () => {

    const { reservationId } = useParams<UrlParams>()

    return (
       
        <div>
            <h2>{`Book id: ${reservationId}`}</h2>
        </div>

    )

}

export default ReservationDetails