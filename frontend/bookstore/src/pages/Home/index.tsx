import { ReactComponent as MainImage } from '../../assets/images/main-image.svg';
import { Link } from 'react-router-dom';
import ButtonIcon from '../../components/ButtonIcon';

import './styles.css';
import { Authority, hasAnyAuthorities } from '../../utils/auth';

const Home = () => {

	console.log(hasAnyAuthorities(['ROLE_CUSTOMER' , 'ROLE_ADMIN']))

	return (

		<div className="home-container">

			<div className="base-card home-card">
				<div className="home-content-container">
					<div>
						<h1>Conheça o melhor catálogo de livros</h1>
						<p>
							Ajudaremos você a encontrar o livro que mais combina com seus gostos.
						</p>
					</div>
					<div>
						<Link to="/products">
							<ButtonIcon text={"Inicie agora sua busca"} />
						</Link>
					</div>
				</div>
				<div className="home-image-container">
					<MainImage />
				</div>
			</div>
		</div>
	);
};

export default Home;