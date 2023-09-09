import { FormEvent, useRef, useState } from "react"
import { Link } from "react-router-dom"
import ButtonIcon from "../../../components/ButtonIcon"

const Recover = () => {

    const [email, setEmail] = useState('')

    const captchaRef = useRef(null)

    const handleSubmit = (e: FormEvent) => {

        e.preventDefault()



    }

    return (

        <div className='base-card login-card'>

            <h1>RECUPERAR SENHA</h1>

            <form onSubmit={handleSubmit}>

                <div className='mb-4'>

                    <label htmlFor='email' className='mb-2'>Digite o email relacionado a sua conta</label>

                    <input
                        type='email'
                        className='form-control base-input'
                        placeholder='Email'
                        name='email'
                        id='email'
                        required
                        autoComplete='email'
                        value={email}
                        onChange={e => setEmail(e.target.value)}
                    />

                </div>

                <div className='mb-2'>

                    {/*<ReCAPTCHA sitekey='' ref={captchaRef} />*/}

                </div>

                <div className='login-submit' style={{ marginTop: '170px' }}>
                    <ButtonIcon text='Enviar e-mail de troca de senha [DISABLED]' />
                </div>

                <div className='signup-container'>

                    <span className='not-registered'>Lembrou senha?</span>
                    <Link to='/auth/login' className='login-link-register'>
                        VOLTAR
                    </Link>

                </div>

            </form>


        </div>

    )

}

export default Recover