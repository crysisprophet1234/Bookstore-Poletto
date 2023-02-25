import ButtonIcon from '../../../components/ButtonIcon';
import { Link, useHistory, useLocation } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { getTokenData, requestBackendLogin, saveAuthData } from '../../../utils/requests';
import { useState, useContext } from 'react';
import { AuthContext } from '../../../AuthContext';

import './styles.css';

type FormData = {

    username: string;
    password: string;
    firstname: string;
    lastname: string;

}

type LocationState = {

    from: string;

}

const Login = () => {

    const location = useLocation<LocationState>();

    //const { from } = location.state || { from: { pathname: '/admin' } };

    //const { setAuthContextData } = useContext(AuthContext);

    const [hasError, setHasError] = useState(false);

    const { register, handleSubmit, formState: { errors } } = useForm<FormData>();

    //const history = useHistory();

    const onSubmit = (formData: FormData) => {

        requestBackendLogin(formData)
            .then(response => {

                setHasError(false);

            })
            .catch(err => {
                console.log(err)
                setHasError(true);
            })

    }

    return (

        <div className="base-card login-card">

            <h1>SIGN UP</h1>

            {hasError &&

                <div className="alert alert-danger" role="alert" style={{ textAlign: 'center' }}>
                    Usuário ou senha incorretos.
                    <br />
                    <a href="/auth/recover" className="alert-link">Esqueceu sua senha?</a>
                </div>

            }

            <form onSubmit={handleSubmit(onSubmit)}>

                <div className="mb-3">

                    <input
                        {...register("username", {
                            required: 'Campo obrigatório',
                            pattern: {
                                value: (/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i),
                                message: 'Email inválido'
                            }
                        })}
                        type="text"
                        className={`form-control base-input ${errors.username ? 'is-invalid' : ''}`}
                        placeholder="Email"
                        name="username"
                    />
                    <div className="invalid-feedback d-block">
                        {errors.username?.message}
                    </div>

                </div>

                <div className="mb-3">

                    <input
                        {...register("firstname", {
                            required: 'Campo obrigatório',
                            pattern: {
                                value: /[A-Za-z]/,
                                message: 'Nome não deve conter números ou caracteres especiais'
                            }
                        })}
                        type="text"
                        className={`form-control base-input ${errors.firstname ? 'is-invalid' : ''}`}
                        placeholder="Primeiro nome"
                        name="firstname"
                    />
                    <div className="invalid-feedback d-block">
                        {errors.firstname?.message}
                    </div>

                </div>

                <div className="mb-3">

                    <input
                        {...register("lastname", {
                            required: 'Campo obrigatório',
                            pattern: {
                                value: /[A-Za-z]/,
                                message: 'Nome não deve conter números ou caracteres especiais'
                            }
                        })}
                        type="text"
                        className={`form-control base-input ${errors.lastname ? 'is-invalid' : ''}`}
                        placeholder="Segundo nome"
                        name="lastname"
                    />
                    <div className="invalid-feedback d-block">
                        {errors.lastname?.message}
                    </div>

                </div>

                <div className="mb-3">

                    <input
                        {...register("password", {
                            required: 'Campo obrigatório',
                            minLength: {
                                value: 4,
                                message: 'Senha deve conter pelo menos 4 caracteres'
                            },
                            maxLength: {
                                value: 8,
                                message: 'Senha deve conter pelo menos 4 caracteres'
                            }
                        })}
                        type="password"
                        className={`form-control base-input ${errors.password ? 'is-invalid' : ''}`}
                        placeholder="Senha"
                        name="password"
                    />
                    <div className="invalid-feedback d-block">
                        {errors.password?.message}
                    </div>

                </div>

                <div className="mb-5">

                    <input
                        {...register("password", {
                            required: 'Campo obrigatório',
                            //validate: value => value === register..current || "Psw doesnt match" testar
                        })}
                        type="password"
                        className={`form-control base-input ${errors.password ? 'is-invalid' : ''}`}
                        placeholder="Confirmar senha"
                        name="password-repeat"
                    />
                    <div className="invalid-feedback d-block">
                        {errors.password?.message}
                    </div>

                </div>

                <div className="login-submit">
                    <ButtonIcon text="Salvar" />
                </div>

                <div className="signup-container">

                    <span className="not-registered">Já possui conta?</span>
                    <Link to="/auth/signup" className="login-link-register">
                        FAZER LOGIN
                    </Link>

                </div>

            </form>

        </div>

    );
};


export default Login;