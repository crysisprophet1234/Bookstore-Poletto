import { Link } from 'react-router-dom';
import Pagination from '../../components/Pagination';
import CardLoader from './cardLoader/index';

import { AxiosRequestConfig } from 'axios';
import { useCallback, useEffect, useState } from 'react';
import BookCard from '../../components/BookCard';
import { Book } from '../../types/book';
import { SpringPage } from '../../types/vendor/spring';
import { requestBackend } from '../../utils/requests';

import ProductFilter, { ProductFilterData } from '../../components/ProductFilter';
import './styles.css';

type ControlComponentsData = {
  activePage: number;
  filterData: ProductFilterData;
};

const Catalog = () => {

  const [page, setPage] = useState<SpringPage<Book>>();

  const [controlComponentsData, setControlComponentsData] =
    useState<ControlComponentsData>({
      activePage: 0,
      filterData: { name: '', category: null },
    });

  const handlePageChange = (pageNumber: number) => {
    setControlComponentsData({ activePage: pageNumber, filterData: controlComponentsData.filterData });
  };

  const handleSubmitFilter = (data: ProductFilterData) => {
    setControlComponentsData({ activePage: 0, filterData: data });
  };

  const getBooks = useCallback(() => {
    const config: AxiosRequestConfig = {
      method: 'GET',
      url: '/api/books/v2',
      params: {
        page: controlComponentsData.activePage,
        size: window.innerWidth > 768 ? 12 : 6,
        name: controlComponentsData.filterData.name,
        categoryId: controlComponentsData.filterData.category?.id
      },
    };

    requestBackend(config)
      .then((response) => {
        setPage(response.data);
      })
      .catch((err) => {
        console.log(err.response.data)
      })
  }, [controlComponentsData]);

  useEffect(() => {
    getBooks();
  }, [getBooks]);

  return (

    <div className="container my-4 catalog-container">

      <div className="row catalog-title-container">
        <h1>Catálogo de Produtos</h1>
      </div>

      <div className="product-crud-bar-container">

        <ProductFilter onSubmitFilter={handleSubmitFilter} />

      </div>

      <div className="row">

        {!page ? <CardLoader /> :

          (
            page.content.map(book => {

              return (

                <div className="col-sm-6 col-lg-4 col-xl-3" key={book.id}>
                  <Link to={`/books/${book.id}`}>
                    <BookCard book={book} />
                  </Link>
                </div>

              )

            })
          )

        }

      </div>

      <div className="row">
        <Pagination
          forcePage={page?.number}
          pageCount={page ? page.totalPages : 0}
          range={3}
          onChange={handlePageChange}
        />
      </div>

    </div>

  );

};

export default Catalog;