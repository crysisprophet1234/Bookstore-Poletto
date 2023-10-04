import { yupResolver } from '@hookform/resolvers/yup'
import { AxiosRequestConfig } from 'axios'
import { useState } from 'react'
import { Controller, useForm } from 'react-hook-form'
import { BiSolidDownArrow } from 'react-icons/bi'
import Select from 'react-select'
import { toast } from 'react-toastify'
import * as Yup from 'yup'
import { requestBackend } from '../../../../utils/requests'

import { debounce } from '../../../../utils/debounce'
import { selectStyles } from '../../../../utils/selectStyles'
import './styles.css'

export type ReserveFilterData = {

    id: string
    startingDate: Date
    devolutionDate: Date
    clientId: string
    bookId: string
    status: string
    orderBy: string
    sort: string

}

type Props = {

    onSubmitFilter: (data: ReserveFilterData) => void

}

const ReservesFilter = ({ onSubmitFilter }: Props) => {

    const initialDate = new Date(new Date().getTime() - 180 * 24 * 60 * 60 * 1000).toISOString().substring(0, 10)

    const devolutionDate = new Date(new Date().getTime() + 30 * 24 * 60 * 60 * 1000).toISOString().substring(0, 10)

    const [dates, setDates] = useState({
        initialDate: initialDate,
        devolutionDate: devolutionDate
    })

    const validationSchema = Yup.object().shape({

        id: Yup.string()
            .matches(/^(?:[1-9][0-9]*)?$/, 'Código deve ser maior que 0'),

        clientId: Yup.string()
            .matches(/^(?:[1-9][0-9]*)?$/, 'Código deve ser maior que 0'),

        bookId: Yup.string()
            .matches(/^(?:[1-9][0-9]*)?$/, 'Código deve ser maior que 0'),

        startingDate: Yup.date()
            .max(new Date(), 'Data inicial não pode ser maior que hoje'),

        devolutionDate: Yup.date()
            .min(dates.initialDate, 'Data final deve ser maior que inicial')

    })

    const formOptions = { resolver: yupResolver(validationSchema) }

    const { register, handleSubmit, setValue, control, formState: { errors } } = useForm<ReserveFilterData>(formOptions)

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

        setDates({
            initialDate: initialDate,
            devolutionDate: devolutionDate
        })

        setValue('id', '')
        setValue('clientId', '')
        setValue('startingDate', new Date(initialDate))
        setValue('devolutionDate', new Date(devolutionDate))
        setValue('bookId', '')
        setValue('status', 'all')
        setClientName('')
        setBookName('')

    }

    const handleClientChange = debounce((clientId: string) => {

        if (clientId !== '') {

            setClientName('Carregando...')

            const config: AxiosRequestConfig = {
                method: 'GET',
                url: `/api/users/v2/${clientId}`,
                withCredentials: true
            }

            requestBackend(config)
                .then((response) => {
                    setClientName(response.data.firstname + ' ' + response.data.lastname)
                })
                .catch((error) => {
                    setClientName('')
                    toast.error(error.response.data.message, { autoClose: 3000 })
                })

        } else {

            setClientName('')

        }

    }, 500)

    const handleBookChange = debounce((bookId: string) => {

        if (bookId !== '') {

            setBookName('Carregando...')

            const config: AxiosRequestConfig = {
                method: 'GET',
                url: `/api/books/v2/${bookId}`,
                withCredentials: true
            }

            requestBackend(config)
                .then((response) => {
                    setBookName(response.data.name + ', ' + response.data.author.name)
                })
                .catch((error) => {
                    setBookName('')
                    toast.error(error.response.data.message, { autoClose: 3000 })
                })

        } else {

            setBookName('')

        }

    }, 500)

    return (

        <div className='base-card p-3 mb-2 filter-container'>

            <div className='filter-btn-container'>

                <Controller
                    name='orderBy'
                    control={control}
                    defaultValue=''
                    render={({ field }) => (
                        <Select
                            {...field}
                            isMulti={false}
                            options={[
                                { label: 'Ordernar por', value: '' },
                                { label: 'Código', value: 'id' },
                                { label: 'Data de criação', value: 'startingDate' },
                                { label: 'Status', value: 'status' }
                            ]}
                            className='select-order filter-right-btn'
                            onChange={(selectedOption) => setValue('orderBy', selectedOption?.value as string)}
                            value={undefined}
                            defaultValue={{ label: 'Ordernar por', value: '' }}
                            getOptionLabel={(orderBy) => String(orderBy?.label)}
                            getOptionValue={(orderBy) => String(orderBy?.value)}
                            form='reservations-filter-form'
                            styles={selectStyles}
                        />
                    )}
                />

                <Controller
                    name='sort'
                    control={control}
                    defaultValue=''
                    render={({ field }) => (
                        <Select
                            {...field}
                            isMulti={false}
                            options={[
                                { label: 'Ordem', value: '' },
                                { label: 'Crescente', value: 'asc' },
                                { label: 'Decrescente', value: 'desc' }
                            ]}
                            className='select-order filter-right-btn'
                            onChange={(selectedOption) => setValue('sort', selectedOption?.value as string)}
                            value={undefined}
                            defaultValue={{ label: 'Ordem', value: '' }}
                            getOptionLabel={(sort) => String(sort?.label)}
                            getOptionValue={(sort) => String(sort?.value)}
                            form='reservations-filter-form'
                            styles={selectStyles}
                        />
                    )}
                />

                <button
                    className='btn btn-outline-primary filter-right-btn'
                    name='expand-filter-button'
                    type='button'
                    data-bs-toggle='collapse'
                    data-bs-target='#full-filter'
                    aria-expanded='false'
                    aria-controls='full-filter'
                    aria-label='expand-filter-button'
                    onClick={handleArrowClick}
                >
                    {window.innerWidth < 768 ? <BiSolidDownArrow /> : 'Expandir filtros'}
                </button>

            </div>

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
                        id='id'
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
                                inputId='status'
                                styles={{
                                    control: (provided) => ({
                                        ...provided,
                                        height: '45px',
                                        minWidth: '200px'
                                    }),
                                    valueContainer: (provided) => ({
                                        ...provided,
                                        height: '45px',
                                        padding: '0px 8px'
                                    }),
                                    singleValue: (provided) => ({
                                        ...provided,
                                        lineHeight: '20px'
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
                                onChange={e => handleClientChange(e.target.value)}
                                name='clientId'
                                id='clientId'
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
                                id='startingDate'
                                value={dates.initialDate}
                                onChange={e => setDates({ ...dates, initialDate: e.target.value })}
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
                                id='devolutionDate'
                                value={dates.devolutionDate}
                                onChange={e => setDates({ ...dates, devolutionDate: e.target.value })}
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
                                onChange={e => handleBookChange(e.target.value)}
                                name='bookId'
                                id='bookId'
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