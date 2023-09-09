import { useEffect, useState } from "react"

type Props = {
    errorCode: number
}

const styles = {
    fontWeight: 'bold',
    fontSize: '1.25rem',
    marginTop: '1rem'
}


const ErrorFallback = ({ errorCode }: Props) => {

    const [content, setContent] = useState<JSX.Element>()

    useEffect(() => {

        switch (errorCode) {

            case (403): setContent(

                <>
                    <p style={styles}>Você não tem permissão para acessar este recurso.</p>
                </>

            )
                break

            case (404): setContent(
                <>
                    <p style={styles}>Ops! A página não foi encontrada.</p>
                    <p style={styles}>Se o erro persistir, nos informe por favor: polettobookstore@gmail.com</p>
                    <p style={styles}>Erro 404: O conteúdo não está mais disponível ou você digitou o endereço errado.</p>
                </>
            )
                break
        }
    }, [errorCode])

    return (

        <div className="d-flex justify-content-center align-items-center mt-4">

            <div className="text-center mt-4">

                <h2 className="text-danger fw-bold fs-1 mt-4">{errorCode}</h2>

                {content}

            </div>

        </div>

    )

}

export default ErrorFallback
