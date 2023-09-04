import './styles.css'

import { ReactComponent as ArrowIcon } from '../../assets/images/arrow.svg'

type Props = {

  text: String

}

const ButtonIcon = ({ text }: Props) => {

  return (

    <div className='btn-container'>

      <button className='btn btn-primary'>
        <h2>{text}</h2>
      </button>

      <div className='btn-icon-container'>
        <ArrowIcon />
      </div>

    </div>

  )

}

export default ButtonIcon
