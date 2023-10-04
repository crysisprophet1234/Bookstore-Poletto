import { yupResolver } from '@hookform/resolvers/yup'
import { Controller, useForm } from 'react-hook-form'
import Select from 'react-select'
import * as Yup from 'yup'
import { Sort } from '../../../../types/vendor/spring'
import { selectStyles } from '../../../../utils/selectStyles'

import { Authority } from '../../../../utils/auth'
import '../../Reserves/ReservesFilter/styles.css'

export type UserFilterData = Sort & {

    id?: string
    name?: string
    role?: Authority

}

type Props = {

    onSubmitFilter: (data: UserFilterData) => void
    isSubmiting: boolean

}

const UsersFilter = ({ onSubmitFilter, isSubmiting }: Props) => {

    const validationSchema = Yup.object().shape({

        id: Yup.string()
            .notRequired()
            .matches(/^(?:[1-9][0-9]*)?$/, 'Código não pode ser 0'),


        name: Yup.string()
            .notRequired()
            .matches(/^(?=.*[a-zA-Z]).*|^\s*$/, 'Nome ou e-mail deve ser válido')

    })

    const formOptions = { resolver: yupResolver(validationSchema) }

    const { register, handleSubmit, setValue, control, reset, formState: { errors } } = useForm<UserFilterData>(formOptions)

    const onSubmit = (formData: UserFilterData) => {
        onSubmitFilter(formData)
    }

    return (

        <div className='base-card p-3 mb-2 filter-container'>

            <form id='users-filter-form' onSubmit={handleSubmit(onSubmit)}>

                <div className='filter-btn-container'>

                    <Controller
                        name='orderBy'
                        control={control}
                        render={({ field }) => (
                            <Select
                                {...field}
                                options={[
                                    { label: 'Código', value: 'id' },
                                    { label: 'Nome', value: 'name' },
                                    { label: 'E-mail', value: 'email' }
                                ]}
                                isClearable
                                className='select-order filter-right-btn'
                                onChange={(selectedOption) => setValue('orderBy', selectedOption?.value)}
                                value={undefined}
                                getOptionLabel={(sort) => sort?.label}
                                getOptionValue={(sort) => sort?.value}
                                form='user-filter-form'
                                placeholder='Ordenar por'
                                styles={{ ...selectStyles }}
                            />
                        )}
                    />

                    <Controller
                        name='sort'
                        control={control}
                        render={({ field }) => (
                            <Select
                                {...field}
                                options={[
                                    { label: 'Crescente', value: 'asc' },
                                    { label: 'Decrescente', value: 'desc' }
                                ]}
                                isClearable
                                className='select-order filter-right-btn'
                                onChange={(selectedOption) => setValue('sort', selectedOption?.value)}
                                value={undefined}
                                getOptionLabel={(sort) => sort?.label}
                                getOptionValue={(sort) => sort?.value}
                                placeholder='Ordem'
                                form='user-filter-form'
                                styles={{ ...selectStyles }}
                            />
                        )}
                    />

                </div>

                <div className='my-4 double-input-container'>

                    <div style={{ flex: '.15' }}>

                        <label htmlFor='id'>Código</label>

                        <input
                            {...register('id')}
                            type='number'
                            className={`form-control base-input ${errors.id ? 'is-invalid' : ''}`}
                            placeholder='Código do usuário'
                            form='users-filter-form'
                            id='id'
                            name='id'
                            autoComplete='off'
                        />
                        <div className='invalid-feedback d-block'>
                            {errors.id?.message}
                        </div>

                    </div>

                    <div style={{ flex: '.34' }}>

                        <label htmlFor='role'>Nível de acesso</label>

                        <Controller
                            name='role'
                            control={control}
                            render={({ field }) => (
                                <Select
                                    {...field}
                                    options={[
                                        { label: 'Administrador', value: 'ROLE_ADMIN' },
                                        { label: 'Operador', value: 'ROLE_OPERATOR' }
                                    ]}
                                    isClearable
                                    value={undefined}
                                    onChange={(role) => setValue('role', role?.value)}
                                    getOptionLabel={(role) => role?.label}
                                    getOptionValue={(role) => role?.value}
                                    className='filter-select'
                                    classNamePrefix='filter-select'
                                    form='users-filter-form'
                                    placeholder='Nível de acesso'
                                    inputId='role'
                                    styles={selectStyles}
                                />
                            )}
                        />

                    </div>

                </div>

                <div className='mb-4 double-input-container'>

                    <div style={{ flex: '.5' }}>

                        <label htmlFor='name'>Nome ou e-mail</label>

                        <input
                            {...register('name', {
                                required: false
                            })}
                            type='text'
                            className={`form-control base-input ${errors.name ? 'is-invalid' : ''}`}
                            placeholder='Nome ou e-mail do usuário'
                            form='users-filter-form'
                            id='name'
                            name='name'
                            autoComplete='off'
                        />
                        <div className='invalid-feedback d-block'>
                            {errors.name?.message}
                        </div>

                    </div>

                </div>

            </form>

            <div className='buttons-container mb-3'>

                <button
                    onClick={e => {
                        reset()
                    }}
                    type='button'
                    className='btn btn-outline-dark btn-product-filter'
                    form='users-filter-form'
                >
                    LIMPAR FILTRO
                </button>

                <button
                    type='submit'
                    className='btn btn-outline-primary btn-product-filter mx-4'
                    form='users-filter-form'
                    disabled={isSubmiting}
                >
                    PESQUISAR
                </button>

            </div>

        </div>

    )

}

export default UsersFilter