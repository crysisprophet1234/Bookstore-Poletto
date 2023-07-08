import ReactPaginate from 'react-paginate';
import { ReactComponent as ArrowIcon } from '../../assets/images/arrow.svg';
import './styles.css';

type Props = {

    forcePage?: number;
    pageCount: number;
    range: number;
    onChange?: (pageNumber : number) => void;

}

const Pagination = ({ forcePage, pageCount, range, onChange }: Props) => {

    return (

        <ReactPaginate
            forcePage={forcePage}
            pageCount={pageCount}
            pageRangeDisplayed={range}
            marginPagesDisplayed={1}
            containerClassName="pagination-container"
            pageLinkClassName="pagination-item"
            breakClassName="pagination-item"
            previousLabel={<div className="pagination-arrow-container"><ArrowIcon /></div>}
            previousClassName="arrow-previous"
            nextClassName="arrow-next"
            nextLabel={<div className="pagination-arrow-container"><ArrowIcon /></div>}
            activeLinkClassName="pagination-link-active"
            disabledClassName="arrow-inactive"
            onPageChange={(items) => (onChange) ? onChange(items.selected) : {}}
        />

    )

}

export default Pagination;