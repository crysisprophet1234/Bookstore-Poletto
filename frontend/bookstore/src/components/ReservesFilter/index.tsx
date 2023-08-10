import { useEffect, useState } from 'react'
import { BiSolidDownArrow } from 'react-icons/bi'
import Select from 'react-select'
import { Controller, useForm } from 'react-hook-form'
import { AxiosRequestConfig } from 'axios'
import { requestBackend } from '../../utils/requests'
import { toast } from 'react-toastify'
import * as Yup from 'yup';
import { yupResolver } from '@hookform/resolvers/yup';

import './styles.css'

export type ReserveFilterData = {

    id: string
    startingDate: Date
    devolutionDate: Date
    clientId: string
    bookId: string
    status: string

}

type Props = {

    onSubmitFilter: (data: ReserveFilterData) => void

}

const ReservesFilter = ({ onSubmitFilter }: Props) => {

    const validationSchema = Yup.object().shape({

        startingDate: Yup.date()
            .max(new Date(), 'Data inicial não pode ser maior que hoje')

        //devolutionDate: Yup.date() //TODO: verificar como limita data de devolução
        //   .min()

    });

    const formOptions = { resolver: yupResolver(validationSchema) };

    const { register, handleSubmit, setValue, getValues, control, formState: { errors } } = useForm<ReserveFilterData>(formOptions)

    const [clientName, setClientName] = useState<string>('')

    const [bookName, setBookName] = useState<string>('')

    const [isArrowUp, setIsArrowUp] = useState(false)

    const handleArrowClick = () => {
        setIsArrowUp(!isArrowUp)
    }

    const onSubmit = (formData: ReserveFilterData) => {
        onSubmitFilter(formData)
    }

    const handleFormClear = () => {
        setValue('id', '')
        setValue('clientId', '')
        setValue('startingDate', new Date())
        setValue('devolutionDate', new Date())
        setValue('bookId', '')
        setValue('status', 'all')
        setClientName('')
        setBookName('')
    }


    useEffect(() => {

        if (getValues('clientId')) {

            const config: AxiosRequestConfig = {
                method: 'GET',
                url: `/api/users/v2/${getValues('clientId')}`,
                withCredentials: true
            }

            requestBackend(config)
                .then((response) => {
                    setClientName(response.data.firstname + ' ' + response.data.lastname)
                })
                .catch((error) => {
                    setClientName('')
                    toast.error(error.response.data.message)
                })

        }

    }, [clientName, getValues])

    useEffect(() => {

        if (getValues('bookId')) {

            const config: AxiosRequestConfig = {
                method: 'GET',
                url: `/api/books/v2/${getValues('bookId')}`,
                withCredentials: true
            }

            requestBackend(config)
                .then((response) => {
                    setBookName(response.data.name + ', ' + response.data.author.name)
                })
                .catch((error) => {
                    //FIXME: ADICIONAR O TRIGGER DE ERRO DO INPUT
                    //FIXME: EM CASOS DE USER OU BOOK N ENCONTRADO, NOTIFICAÇÃO SERÁ VIA TOAST. FALTA CONFIGURAR TOAST PARAMETERS IGUAL EM admin/books/form
                    setBookName('')
                    toast.error(error.response.data.message)
                })

        }

    }, [bookName, getValues])

    return (

        <div className='base-card p-3 mb-2 filter-container'>

            <p>
                <button
                    className='btn btn-outline-primary expand-filter'
                    type='button'
                    data-bs-toggle='collapse'
                    data-bs-target='#full-filter'
                    aria-expanded='false'
                    aria-controls='full-filter'
                    onClick={handleArrowClick}
                >
                    {window.innerWidth < 768 ? <BiSolidDownArrow /> : 'Expandir filtros'}
                </button>
            </p>

            <div className='mb-3 double-input-container'>

                <div>

                    <label htmlFor='id'>Código</label>

                    <input
                        {...register('id', {
                            required: false
                        })}
                        type='number'
                        className={`form-control base-input ${errors.id ? 'is-invalid' : ''}`}
                        placeholder='Código da reserva'
                        form='reservations-filter-form'
                        name='id'
                    />
                    <div className='invalid-feedback d-block'>
                        {errors.id?.message}
                    </div>

                </div>

                <div>

                    <label htmlFor='status'>Status</label>

                    <Controller
                        name='status'
                        control={control}
                        defaultValue='all'
                        render={({ field }) => (
                            <Select
                                {...field}
                                isMulti={false}
                                options={[
                                    { label: 'Todos', value: 'all' },
                                    { label: 'Em andamento', value: 'in_progress' },
                                    { label: 'Finalizadas', value: 'finished' }
                                ]}
                                className='filter-select'
                                classNamePrefix='filter-select'
                                onChange={(selectedOption) => setValue('status', selectedOption?.value as string)}
                                value={undefined}
                                defaultValue={{ label: 'Todos', value: 'all' }}
                                getOptionLabel={(status) => String(status?.label)}
                                getOptionValue={(status) => String(status?.value)}
                                form='reservations-filter-form'
                                styles={{
                                    control: (provided) => ({
                                        ...provided,
                                        height: '45px',
                                        minWidth: '200px',
                                    }),
                                    valueContainer: (provided) => ({
                                        ...provided,
                                        height: '45px',
                                        padding: '0px 8px'
                                    }),

                                    input: (provided) => ({
                                        ...provided,
                                        margin: '0px',
                                    }),
                                    indicatorsContainer: (provided) => ({
                                        ...provided,
                                        height: '45px',
                                    })
                                }}
                            />
                        )}
                    />

                </div>

            </div>

            <div className='collapse' id='full-filter'>

                <form id='reservations-filter-form' onSubmit={handleSubmit(onSubmit)}>

                    <div className='mb-3 double-container'>

                        <label htmlFor='clientId'>Cliente</label>

                        <div className='input-container'>

                            <input
                                {...register('clientId', {
                                    required: false
                                })}
                                type='number'
                                className={`form-control base-input ${errors.clientId ? 'is-invalid' : ''}`}
                                placeholder='Código do cliente'
                                onBlur={(e) => { if (e.target.value !== '') { setClientName('Loading...') } }}
                                name='clientId'
                            />

                            <div className='entity-name'>
                                {clientName}
                            </div>

                        </div>

                        <div className='invalid-feedback d-block'>
                            {errors.clientId?.message}
                        </div>

                    </div>

                    <div className='mb-3 double-input-container'>

                        <div>

                            <label htmlFor='startingDate'>Data de ínicio</label>

                            <input
                                {...register('startingDate', {
                                    required: false
                                })}
                                type='date'
                                className={`form-control base-input ${errors.startingDate ? 'is-invalid' : ''}`}
                                name='startingDate'
                                defaultValue={(new Date(new Date().getTime() - 14 * 24 * 60 * 60 * 1000)).toISOString().substring(0, 10)}
                            />
                            <div className='invalid-feedback d-block'>
                                {errors.startingDate?.message}
                            </div>

                        </div>

                        <div>

                            <label htmlFor='devolutionDate'>Data de devolução</label>

                            <input
                                {...register('devolutionDate', {
                                    required: false
                                })}
                                type='date'
                                className={`form-control base-input ${errors.devolutionDate ? 'is-invalid' : ''}`}
                                name='devolutionDate'
                                defaultValue={(new Date(new Date().getTime() + 14 * 24 * 60 * 60 * 1000)).toISOString().substring(0, 10)}
                            />
                            <div className='invalid-feedback d-block'>
                                {errors.devolutionDate?.message}
                            </div>

                        </div>

                    </div>

                    <div className='mb-3 double-container'>

                        <label htmlFor='bookId'>Livro</label>

                        <div className='input-container'>

                            <input
                                {...register('bookId', {
                                    required: false
                                })}
                                type='number'
                                className={`form-control base-input ${errors.bookId ? 'is-invalid' : ''}`}
                                placeholder='Código do livro'
                                onBlur={(e) => { if (e.target.value !== '') { setBookName('Loading...') } }}
                                name='bookId'
                            />

                            <div className='entity-name'>
                                {bookName}
                            </div>

                        </div>

                        <div className='invalid-feedback d-block'>
                            {errors.bookId?.message}
                        </div>

                    </div>



                </form>

            </div>

            <div className='buttons-container'>

                <button
                    //FIXME: tem que voltar o select de status para ALL
                    onClick={handleFormClear}
                    type='button'
                    className='btn btn-outline-dark btn-product-filter'
                >
                    LIMPAR FILTRO
                </button>

                <button
                    type='submit'
                    className='btn btn-outline-primary btn-product-filter mx-4'
                    form='reservations-filter-form'
                >
                    PESQUISAR
                </button>

            </div>

        </div>

    )

}

export default ReservesFilter