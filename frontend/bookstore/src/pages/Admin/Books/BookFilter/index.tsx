import { useEffect, useState } from 'react'
import { Controller, useForm } from 'react-hook-form'
import Select from 'react-select'

import { Category } from '../../../../types/category'
import { debounce } from '../../../../utils/debounce'
import { requestBackend } from '../../../../utils/requests'
import { selectStyles } from '../../../../utils/selectStyles'
import './styles.css'

export type BookFilterData = {
  name: string
  category: Category | undefined
}

type Props = {
  onSubmitFilter: (data: BookFilterData) => void
}

const BookFilter = ({ onSubmitFilter }: Props) => {

  const [selectCategories, setSelectCategories] = useState<Category[]>([])

  const { register, handleSubmit, setValue, getValues, control } = useForm <BookFilterData>()

  const onSubmit = (formData: BookFilterData) => {
    onSubmitFilter(formData)
  }

  const handleFormClear = () => {
    setValue('name', '')
    setValue('category', undefined)
  }

  const handleChangeCategory = (value: Category) => {
    setValue('category', value)

    const obj: BookFilterData = {
      name: getValues('name'),
      category: getValues('category')
    }

    onSubmitFilter(obj)
  }

  useEffect(() => {
    requestBackend({ url: '/api/categories/v2' })
      .then((response) => {
        setSelectCategories(response.data)
      })
  }, [])

  return (

    <div className='base-card product-filter-container'>

      <form onSubmit={handleSubmit(onSubmit)} onChange={debounce((handleSubmit(onSubmit)), 500)} className='product-filter-form'>

        <div className='product-filter-name-container'>

          <input
            {...register('name')}
            type='text'
            className='form-control base-input'
            placeholder='Nome do livro ou autor'
            name='name'
            aria-label='book-input-search'
            autoComplete='off'
          />

        </div>

        <div className='product-filter-bottom-container'>

          <div className='product-filter-category-container'>

            <Controller
              name='category'
              control={control}
              render={({ field }) => (
                <Select
                  {...field}
                  options={selectCategories}
                  isClearable
                  placeholder='Categoria'
                  className='product-filter-select'
                  classNamePrefix='product-filter-select'
                  onChange={(value) => handleChangeCategory(value as Category)}
                  getOptionLabel={(category: Category) => category.name}
                  getOptionValue={(category: Category) => String(category.id)}
                  styles={selectStyles}
                />
              )}
            />

          </div>

          <button
            onClick={handleFormClear}
            className='btn btn-dark btn-product-filter'
          >
            LIMPAR FILTRO
          </button>

        </div>

      </form>

    </div>

  )

}

export default BookFilter
