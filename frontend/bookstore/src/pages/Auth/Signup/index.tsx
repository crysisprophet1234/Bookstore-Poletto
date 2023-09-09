import { yupResolver } from '@hookform/resolvers/yup'
import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { Link } from 'react-router-dom'
import * as Yup from 'yup'
import ButtonIcon from '../../../components/ButtonIcon'
import { requestBackendSignup } from '../../../utils/requests'

import './styles.css'

type FormData = {

    username: string
    password: string
    passwordConfirm: string
    firstname: string
    lastname: string

}

const Login = () => {

    const validationSchema = Yup.object().shape({

        username: Yup.string()
            .required('Email é obrigatório')
            .email('Email deve ser válido'),

        firstname: Yup.string()
            .required('Primeiro nome é obrigatório')
            .min(2, 'Primeiro nome deve ser válido')
            .matches(/^([A-Za-z]*)$/gi, 'Primeiro nome deve ser válido'),

        lastname: Yup.string()
            .required('Segundo nome é obrigatório')
            .min(2, 'Segundo nome deve ser válido')
            .matches(/^([A-Za-z\s]*)$/gi, 'Segundo nome deve ser válido'),

        password: Yup.string()
            .required('Senha é obrigatório')
            .min(6, 'Senha deve conter entre 6 e 8 carácteres')
            .max(8, 'Senha deve conter entre 6 e 8 carácteres')
            .matches(/^(?=.*[0-9])(?=.*[a-zA-Z]).+$/, 'Senha deve conter 1 letra e 1 número'),

        passwordConfirm: Yup.string()
            .required('Confirmar senha é obrigatório')
            .oneOf([Yup.ref('password')], 'Senhas devem ser iguais')

    })

    const [hasError, setHasError] = useState(false)

    const [isSubmitSuccessful, setIsSubmitSuccessful] = useState(false)

    const formOptions = { resolver: yupResolver(validationSchema) }

    const { register, handleSubmit, reset, formState } = useForm<FormData>(formOptions)

    const { errors } = formState

    const onSubmit = (formData: FormData) => {

        requestBackendSignup(formData)

            .then(response => {


                setHasError(false)
                setIsSubmitSuccessful(true)

                reset({
                    username: '',
                    password: '',
                    passwordConfirm: '',
                    firstname: '',
                    lastname: ''
                })

            })

            .catch(err => {

                setHasError(true)
                setIsSubmitSuccessful(false)

            })

    }

    return (

        <div className='base-card login-card'>

            <h1>SIGN UP</h1>

            {isSubmitSuccessful &&

                <div className='alert alert-success' style={{ textAlign: 'center' }}>
                    Usuário cadastrado com sucesso!
                    <br />
                    <Link to='/auth/login' className='link-primary'>Fazer login</Link>
                </div>

            }

            {hasError &&

                <div className='alert alert-danger' role='alert' style={{ textAlign: 'center' }}>
                    Falha no cadastro, por favor tente novamente.
                </div>

            }

            <form onSubmit={handleSubmit(onSubmit)}>

                <div className='mb-3'>

                    <input
                        {...register('username')}
                        type='text'
                        className={`form-control base-input ${errors.username ? 'is-invalid' : ''}`}
                        placeholder='Email'
                        name='username'
                        autoComplete='email'
                    />
                    <div className='invalid-feedback d-block'>
                        {errors.username?.message as React.ReactNode}
                    </div>

                </div>

                <div className='mb-3'>

                    <input
                        {...register('firstname')}
                        type='text'
                        className={`form-control base-input ${errors.firstname ? 'is-invalid' : ''}`}
                        placeholder='Primeiro nome'
                        name='firstname'
                        autoComplete='given-name'
                    />
                    <div className='invalid-feedback d-block'>
                        {errors.firstname?.message as React.ReactNode}
                    </div>

                </div>

                <div className='mb-3'>

                    <input
                        {...register('lastname')}
                        type='text'
                        className={`form-control base-input ${errors.lastname ? 'is-invalid' : ''}`}
                        placeholder='Segundo nome'
                        name='lastname'
                        autoComplete='family-name'
                    />
                    <div className='invalid-feedback d-block'>
                        {errors.lastname?.message as React.ReactNode}
                    </div>

                </div>

                <div className='mb-3'>

                    <input
                        {...register('password')}
                        type='password'
                        className={`form-control base-input ${errors.password ? 'is-invalid' : ''}`}
                        placeholder='Senha'
                        name='password'
                        autoComplete='new-password'
                    />
                    <div className='invalid-feedback d-block'>
                        {errors.password?.message as React.ReactNode}
                    </div>

                </div>

                <div className='mb-5'>

                    <input
                        {...register('passwordConfirm')}
                        type='password'
                        className={`form-control base-input ${errors.passwordConfirm ? 'is-invalid' : ''}`}
                        placeholder='Confirmar senha'
                        name='passwordConfirm'
                        autoComplete='new-password'
                    />
                    <div className='invalid-feedback d-block'>
                        {errors.passwordConfirm?.message as React.ReactNode}
                    </div>

                </div>

                <div className='login-submit'>
                    <ButtonIcon text='Salvar' />
                </div>

                <div className='signup-container'>

                    <span className='not-registered'>Já possui conta?</span>
                    <Link to='/auth/login' className='login-link-register'>
                        FAZER LOGIN
                    </Link>

                </div>

            </form>

        </div>

    )
}

export default Login