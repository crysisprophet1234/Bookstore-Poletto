import './styles.css';
import ProductCard from '../../components/ProductCard';
import Pagination from '../../components/Pagination';
import CardLoader from './cardLoader/index';
import { Link } from 'react-router-dom';
import { Books } from '../../types/books';
//import { BookPage } from '../../types/vendor/bookPage';

import { AxiosRequestHeaders } from 'axios';
import { useEffect, useState } from 'react';
import { requestBackend } from '../../utils/requests';

const Catalog = () => {

  const [page, setPage] = useState();

  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {

    const params = {

      method: 'GET',
      url: "/api/v1/books",
      headers: {} as AxiosRequestHeaders,
      withCredentials: true

    }

    setIsLoading(true);

    requestBackend(params)
      .then(response => {
        console.log(response.data)
        setPage(response.data);
      })
      .finally(() => {
        console.log(page)
        setIsLoading(false)
      })
  }, [])

  return (

    <div className="container my-4 catalog-container">

      <div className="row catalog-title-container">
        <h1>Catálogo de Produtos</h1>
      </div>

      <div className="row">

        {/*

        {isLoading || !page ? <CardLoader /> :

          (

            page?.content.map(product => {

              return (

                <div className="col-sm-6 col-lg-4 col-xl-3" key={product.id}>
                  <Link to={`products/${product.id}`}>
                    <ProductCard product={product} />
                  </Link>
                </div>

              );

            })
          )
        }

      */}

      </div>

      <div className="row">
        <Pagination />
      </div>

    </div>

  );

};

export default Catalog;