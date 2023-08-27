import { AxiosRequestConfig } from 'axios';
import { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Pagination from '../../../../components/Pagination';
import ProductCrudCard from '../../../../components/ProductCrudCard';
import ProductFilter, { ProductFilterData } from '../../../../components/ProductFilter';
import { Book } from '../../../../types/book';
import { SpringPage } from '../../../../types/vendor/spring';
import { requestBackend } from '../../../../utils/requests';

import './styles.css';

type ControlComponentsData = {
  activePage: number;
  filterData: ProductFilterData;
};

const List = () => {

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
        size: 3,
        name: controlComponentsData.filterData.name,
        categoryId: controlComponentsData.filterData.category?.id
      },
    };

    requestBackend(config).then((response) => {
      setPage(response.data);
    });
  }, [controlComponentsData]);

  useEffect(() => {
    getBooks();
  }, [getBooks]);

  return (
    <div className="product-crud-container">

      <div className="product-crud-bar-container">

        <Link to="/admin/books/create">
          <button className="btn btn-primary text-white btn-crud-add">
            ADICIONAR
          </button>
        </Link>

        <ProductFilter onSubmitFilter={handleSubmitFilter} />

      </div>

      <div className="row">

        {page?.empty ? <h2>Nenhum livro registrado</h2> :

          (
            page?.content?.map((book) => (
              <div key={book.id} className="col-sm-6 col-md-12">
                <ProductCrudCard book={book} showEdit={true} />
              </div>

            ))

          )}

      </div>

      <Pagination
        forcePage={page?.number}
        pageCount={page ? page.totalPages : 0}
        range={3}
        onChange={handlePageChange}
      />

    </div>
  );
};

export default List;
