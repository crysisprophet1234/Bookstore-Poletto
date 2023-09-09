import { AxiosRequestConfig } from 'axios'

import { useEffect, useState } from 'react'
import { Controller, useForm } from 'react-hook-form'
import { useNavigate, useParams } from 'react-router-dom'

import Select from 'react-select'
import AsyncSelect from 'react-select/async'

import { Author } from '../../../../types/author'
import { Book } from '../../../../types/book'
import { Category } from '../../../../types/category'

import { requestBackend } from '../../../../utils/requests'

import { toast } from 'react-toastify'

import { debounce } from '../../../../utils/debounce'
import './styles.css'

type UrlParams = {
  bookId: string
}

const Form = () => {

  const { bookId } = useParams<UrlParams>()

  const [initialData, setInitialData] = useState<Book>()

  const isEditing = bookId !== 'create'

  const history = useNavigate()

  const [selectCategories, setSelectCategories] = useState<Category[]>([])

  const {
    register,
    handleSubmit,
    formState: { errors },
    setValue,
    control,
  } = useForm<Book>()

  useEffect(() => {

    requestBackend({ url: '/api/categories/v2' })
      .then((response) => {
        setSelectCategories(response.data)
      })

  }, [])

  const selectAuthorsAsync = debounce((searchValue: string, callback: any) => {

    requestBackend({ url: '/api/authors/v2/all' })
      .then((response) => {
        callback(response.data.filter((author: Author) => author.name.toLowerCase().includes(searchValue.toLowerCase())))
      })
      .catch((error) => {
        console.log(error)
      })

  }, 500)

  useEffect(() => {

    if (isEditing) {

      requestBackend({ url: `/api/books/v2/${bookId}` })

        .then((response) => {

          const book = response.data as Book

          setValue('name', book.name)
          setValue('releaseDate', book.releaseDate)
          setValue('author', book.author)
          setValue('imgUrl', book.imgUrl)
          setValue('categories', book.categories)
          setInitialData(book)

        })

    }

  }, [isEditing, bookId, setValue])

  const onSubmit = (formData: Book) => {

    const data = {

      ...formData,
      status: 'AVAILABLE'

    }

    const config: AxiosRequestConfig = {

      method: isEditing ? 'PUT' : 'POST',
      url: isEditing ? `/api/books/v2/${bookId}` : '/api/books/v2',
      data,
      withCredentials: true

    }

    requestBackend(config)
      .then(() => {
        toast.info(`Livro #${bookId} ${isEditing ? 'atualizado' : 'cadastrado'} com sucesso`, { autoClose: 3000 })
        history('/admin/books')
      })
      .catch((error) => {
        console.log(error)
        toast.error(`Falha ao cadastrar livro: \n ${error.response.data.message}`)
      })

  }

  const handleCancel = () => {
    history('/admin/books')
  }

  return (

    <div className='product-crud-container'>

      <div className='base-card product-crud-form-card'>
        <h1 className='product-crud-form-title'>DADOS DO LIVRO {isEditing && ` - código ${bookId}`}</h1>

        {isEditing &&
          <h2 className='product-crud-form-status'>{(initialData?.status === 'AVAILABLE' ? 'Disponível' : 'Reservado')}</h2>
        }

        <form onSubmit={handleSubmit(onSubmit)}>

          <div className='row product-crud-inputs-container'>

            <div className='col-lg-6 product-crud-inputs-left-container'>

              <div className='margin-bottom-30'>

                <input
                  {...register('name', {
                    required: 'Campo obrigatório',
                    pattern: {
                      value: /^[A-Za-z0-9'& -#]+$/,
                      message: 'Nome deve conter apenas letras e números',
                    },
                  })}
                  type='text'
                  className={`form-control base-input ${errors.name ? 'is-invalid' : ''
                    }`}
                  placeholder='Título do livro'
                  name='name'
                />

                <div className='invalid-feedback d-block'>
                  {errors.name?.message}
                </div>

              </div>

              <div className='margin-bottom-30 '>
                <Controller
                  name='categories'
                  rules={{ required: true }}
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      options={selectCategories}
                      isClearable
                      classNamePrefix='product-crud-select'
                      isMulti
                      onChange={(value) => setValue('categories', [...value])}
                      getOptionLabel={(category: Category) => category.name}
                      getOptionValue={(category: Category) => String(category.id)}
                      placeholder='Categoria'
                    />
                  )}
                />

                {errors.categories && (
                  <div className='invalid-feedback d-block'>
                    Campo obrigatório
                  </div>
                )}

              </div>

              <div className='margin-bottom-30 '>
                <Controller
                  name='author'
                  rules={{ required: true }}
                  control={control}
                  render={({ field }) => (
                    <AsyncSelect
                      {...field}
                      loadOptions={selectAuthorsAsync}
                      isClearable
                      isSearchable
                      classNamePrefix='product-crud-select'
                      onChange={(value) => setValue('author', value as Author)}
                      getOptionLabel={(author: Author) => author.name}
                      getOptionValue={(author: Author) => String(author.id)}
                      placeholder='Autor'
                    />
                  )}
                />

                {errors.author && (
                  <div className='invalid-feedback d-block'>
                    Campo obrigatório
                  </div>
                )}

              </div>

              <div className='margin-bottom-30'>
                <input
                  {...register('imgUrl', {
                    required: 'Campo obrigatório',
                    pattern: {
                      value: /^(https?|chrome):\/\/[^\s$.?#].[^\s]*$/gm,
                      message: 'Deve ser uma URL válida',
                    },
                  })}
                  type='text'
                  className={`form-control base-input ${errors.imgUrl ? 'is-invalid' : ''
                    }`}
                  placeholder='URL da imagem do livro'
                  name='imgUrl'
                />

                <div className='invalid-feedback d-block'>
                  {errors.imgUrl?.message}
                </div>

              </div>

              <div className='margin-bottom-30'>
                <input
                  {...register('releaseDate', {
                    required: 'Campo obrigatório'
                  })}
                  type='date'
                  className={`form-control base-input ${errors.releaseDate ? 'is-invalid' : ''
                    }`}
                  placeholder='Release date'
                  name='releaseDate'
                />

                <div className='invalid-feedback d-block'>
                  {errors.releaseDate?.message}
                </div>

              </div>

            </div>

          </div>

          <div className='product-crud-buttons-container'>

            <button
              className='btn btn-outline-danger product-crud-button'
              onClick={handleCancel}
            >
              CANCELAR
            </button>

            <button className='btn btn-primary product-crud-button text-white'>
              SALVAR
            </button>

          </div>

        </form>

      </div>

    </div>

  )

}

export default Form