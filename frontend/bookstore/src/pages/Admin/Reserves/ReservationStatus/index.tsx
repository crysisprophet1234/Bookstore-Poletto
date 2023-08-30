import { useEffect, useState } from "react"

type Props = {

    //statusInput: 'IN_PROGRESS' | 'FINISHED' | 'EXPIRED' | 'CANCELED'
    statusInput: string

}

type Status = {

    class: string
    text: string

}

const ReservationStatus = ({ statusInput }: Props) => {

    const [status, setStatus] = useState<Status>()

    useEffect(() => {

        switch (statusInput) {

            case 'IN_PROGRESS':
                setStatus({
                    class: 'text-primary',
                    text: 'Em andamento'
                }
                )
                break

            case 'FINISHED':
                setStatus({
                    class: 'text-success',
                    text: 'Finalizado'
                }
                )
                break

            case 'EXPIRED':
                setStatus({
                    class: 'text-warning',
                    text: 'Expirado'
                }
                )
                break

            case 'CANCELED':
                setStatus({
                    class: 'text-danger',
                    text: 'Cancelado'
                }
                )
                break

        }

    }, [statusInput])

    return (

        <h3 className={`fw-bold ${status?.class}`}>{status?.text}</h3>

    )


}

export default ReservationStatus