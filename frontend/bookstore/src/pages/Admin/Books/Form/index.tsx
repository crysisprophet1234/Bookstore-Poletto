import { AxiosRequestConfig } from 'axios';

import { useState } from 'react';
import { useEffect } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { useHistory, useParams } from 'react-router-dom';

import Select from 'react-select';

import { Category } from '../../../../types/category';
import { Book } from '../../../../types/book';
import { Author } from '../../../../types/author';

import { requestBackend } from '../../../../utils/requests';

import { ToastOptions, toast } from 'react-toastify';
import ToastMessage from "../../../../components/ToastMessage"

import './styles.css';


//import { Reservation } from '../../../../types/reservation';

type UrlParams = {
  bookId: string;
};

const Form = () => {

  const { bookId } = useParams<UrlParams>();

  const [initialData, setInitialData] = useState<Book>();

  const [reservations, setReservations] = useState<[]>();

  const isEditing = bookId !== 'create';

  const history = useHistory();

  const [selectCategories, setSelectCategories] = useState<Category[]>([]);

  const [selectAuthors, setSelectAuthors] = useState<Author[]>([]);

  const toastParameters : ToastOptions = {

    position: "bottom-center",
    autoClose: false,
    hideProgressBar: false,
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true,
    progress: undefined,
    theme: "light"

  }

  const {
    register,
    handleSubmit,
    formState: { errors },
    setValue,
    control,
  } = useForm<Book>();

  useEffect(() => {
    requestBackend({ url: '/api/categories/v2' }).then((response) => {
      setSelectCategories(response.data);
    });
  }, []);

  useEffect(() => {
    requestBackend({ url: '/api/authors/v2/all' }).then((response) => {
      setSelectAuthors(response.data);
    });
  }, []);

  useEffect(() => {
    if (isEditing) {
      requestBackend({ url: `/api/books/v2/${bookId}` }).then((response) => {
        const book = response.data as Book;

        setValue('name', book.name);
        setValue('releaseDate', book.releaseDate);
        setValue('author', book.author);
        setValue('imgUrl', book.imgUrl);
        setValue('categories', book.categories);
        setInitialData(book);
      });
    }
  }, [isEditing, bookId, setValue]);

  const onSubmit = (formData: Book) => {
    const data = {
      ...formData,
      status: 'AVAILABLE'
    };

    const config: AxiosRequestConfig = {
      method: isEditing ? 'PUT' : 'POST',
      url: isEditing ? `/api/books/v2/${bookId}` : '/api/books/v2',
      data,
      withCredentials: true,
    };

    requestBackend(config)
      .then(() => {
        toast.info('Produto cadastrado com sucesso');
        history.push('/admin/books');
      })
      .catch((error) => {
        toast.error(`Falha ao cadastrar livro: \n ${error.response.data.message}`, toastParameters);
      });
  };

  const handleCancel = () => {
    history.push('/admin/books');
  };

  const handleReturn = () => {

    const configGet: AxiosRequestConfig = {
      method: 'GET',
      url: "/api/reservations/v2",
      withCredentials: true,
    };

    requestBackend(configGet)
      .then((response) => {
        console.log(response.data)
        setReservations(response.data);
        console.log('reservations -> ' + reservations);
      })
      .catch((err) => {
        toast.error('Erro ao devolver produto');
        console.log(err);
      });

    const configPut: AxiosRequestConfig = {
      method: 'PUT',
      url: `/api/reservations/v2/return/${bookId}`,
      //data: initialData,
      withCredentials: true,
    };

    requestBackend(configPut)
      .then(() => {
        toast.info('Produto devolvido com sucesso');
        history.push('/admin/books');
      })
      .catch(() => {
        toast.error('Erro ao devolver produto');
      });

  }

  return (
    <div className="product-crud-container">

      <ToastMessage />

      <div className="base-card product-crud-form-card">
        <h1 className="product-crud-form-title">DADOS DO LIVRO {isEditing && ` - código ${bookId}`}</h1>

        {isEditing &&
          <h2 className="product-crud-form-status">{(initialData?.status === 'AVAILABLE' ? 'Disponível' : 'Reservado')}</h2>
        }

        <form onSubmit={handleSubmit(onSubmit)}>
          <div className="row product-crud-inputs-container">

            <div className="col-lg-6 product-crud-inputs-left-container">
              <div className="margin-bottom-30">
                <input
                  {...register('name', {
                    required: 'Campo obrigatório',
                  })}
                  type="text"
                  className={`form-control base-input ${errors.name ? 'is-invalid' : ''
                    }`}
                  placeholder="Título do livro"
                  name="name"
                />
                <div className="invalid-feedback d-block">
                  {errors.name?.message}
                </div>
              </div>

              <div className="margin-bottom-30 ">
                <Controller
                  name="categories"
                  rules={{ required: true }}
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      options={selectCategories}
                      isClearable
                      classNamePrefix="product-crud-select"
                      isMulti
                      onChange={(value) => setValue('categories', [...value])}
                      getOptionLabel={(category: Category) => category.name}
                      getOptionValue={(category: Category) => String(category.id)}
                      placeholder="Categoria"
                    />
                  )}
                />
                {errors.categories && (
                  <div className="invalid-feedback d-block">
                    Campo obrigatório
                  </div>
                )}
              </div>


              <div className="margin-bottom-30 ">
                <Controller
                  name="author"
                  rules={{ required: true }}
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      options={selectAuthors}
                      classNamePrefix="product-crud-select"
                      onChange={(value) => setValue('author', value as Author)}
                      getOptionLabel={(author: Author) => author.name}
                      getOptionValue={(author: Author) => String(author.id)}
                      placeholder="Autor"
                    />
                  )}
                />
                {errors.author && (
                  <div className="invalid-feedback d-block">
                    Campo obrigatório
                  </div>
                )}
              </div>


              <div className="margin-bottom-30">
                <input
                  {...register('imgUrl', {
                    required: 'Campo obrigatório',
                    pattern: {
                      value: /^(https?|chrome):\/\/[^\s$.?#].[^\s]*$/gm,
                      message: 'Deve ser uma URL válida',
                    },
                  })}
                  type="text"
                  className={`form-control base-input ${errors.imgUrl ? 'is-invalid' : ''
                    }`}
                  placeholder="URL da imagem do livro"
                  name="imgUrl"
                />
                <div className="invalid-feedback d-block">
                  {errors.imgUrl?.message}
                </div>
              </div>

              <div className="margin-bottom-30">
                <input
                  {...register('releaseDate', {
                    required: 'Campo obrigatório'
                  })}
                  type="date"
                  className={`form-control base-input ${errors.releaseDate ? 'is-invalid' : ''
                    }`}
                  placeholder="Release date"
                  name="releaseDate"
                />
                <div className="invalid-feedback d-block">
                  {errors.releaseDate?.message}
                </div>
              </div>

            </div>


          </div>
          <div className="product-crud-buttons-container">
            <button
              className="btn btn-outline-danger product-crud-button"
              onClick={handleCancel}
            >
              CANCELAR
            </button>
            <button className="btn btn-primary product-crud-button text-white">
              SALVAR
            </button>
            {initialData?.status === 'BOOKED' &&
              <button type="button"
                className="btn btn-info product-crud-button text-white"
                onClick={handleReturn}
              >
                DEVOLVER
              </button>
            }
          </div>
        </form>
      </div>
    </div>
  );
};

export default Form;