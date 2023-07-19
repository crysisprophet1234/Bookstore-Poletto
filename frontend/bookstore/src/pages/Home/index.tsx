import { ReactComponent as MainImage } from '../../assets/images/main-image.svg';
import { Link } from 'react-router-dom';
import ButtonIcon from '../../components/ButtonIcon';

import './styles.css';

const Home = () => {

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
						<Link to="/books">
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