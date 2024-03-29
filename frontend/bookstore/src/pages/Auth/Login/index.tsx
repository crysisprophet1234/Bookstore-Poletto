import { useContext, useState } from 'react'
import { useForm } from 'react-hook-form'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { AuthContext } from '../../../AuthContext'
import ButtonIcon from '../../../components/ButtonIcon'
import { requestBackendLogin } from '../../../utils/requests'

import { getTokenData } from '../../../utils/auth'
import { saveAuthData } from '../../../utils/storage'
import './styles.css'

type FormData = {

    username: string
    password: string

}

const Login = () => {

    const location = useLocation()

    const from = location.state?.from.pathname || '/'

    const { setAuthContextData } = useContext(AuthContext)

    const [hasError, setHasError] = useState(false)

    const { register, handleSubmit, formState: { errors } } = useForm<FormData>()

    const navigate = useNavigate()

    const onSubmit = (formData: FormData) => {

        requestBackendLogin(formData)
            .then(response => {
                saveAuthData(response.data)
                setAuthContextData({
                    authenticated: true,
                    tokenData: getTokenData()
                })
                setHasError(false)
                navigate(from)
            })
            .catch(err => {
                console.log(err)
                setHasError(true)
            })

    }

    return (

        <div className='base-card login-card'>

            <h1>LOGIN</h1>

            {hasError &&

                <div className='alert alert-danger' role='alert' style={{ textAlign: 'center' }}>
                    Usuário ou senha incorretos.
                    <br />
                    <a href='/auth/recover' className='alert-link'>Esqueceu sua senha?</a>
                </div>

            }

            <form onSubmit={handleSubmit(onSubmit)}>

                <div className='mb-4'>

                    <input
                        {...register('username', {
                            required: 'Campo obrigatório',
                            pattern: {
                                value: (/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i),
                                message: 'Email inválido'
                            }
                        })}
                        type='text'
                        className={`form-control base-input ${errors.username ? 'is-invalid' : ''}`}
                        placeholder='Email'
                        name='username'
                        autoComplete='email'
                    />
                    <div className='invalid-feedback d-block'>
                        {errors.username?.message}
                    </div>

                </div>

                <div className='mb-2'>

                    <input
                        {...register('password', {
                            required: 'Campo obrigatório'
                        })}
                        type='password'
                        className={`form-control base-input ${errors.password ? 'is-invalid' : ''}`}
                        placeholder='Password'
                        name='password'
                        autoComplete='current-password'
                    />
                    <div className='invalid-feedback d-block'>
                        {errors.password?.message}
                    </div>

                </div>

                <Link to='/auth/recover' className='login-link-recover'>
                    Esqueci a senha
                </Link>

                <div className='login-submit'>
                    <ButtonIcon text='Fazer login' />
                </div>

                <div className='signup-container'>

                    <span className='not-registered'>Não tem Cadastro?</span>
                    <Link to='/auth/signup' className='login-link-register'>
                        CADASTRAR
                    </Link>

                </div>

            </form>

        </div>

    )
}


export default Login