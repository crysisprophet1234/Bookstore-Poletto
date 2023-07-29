import { ToastContainer } from 'react-toastify';

import 'react-toastify/dist/ReactToastify.css';


const ToastMessage = () => {

    return (

        <ToastContainer
            position="bottom-center"
            autoClose={5000}
            hideProgressBar={false}
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
            style={{ width: "80vw", alignSelf: 'center', maxWidth: "800px" }}
            theme="light"
        />

    )

}

export default ToastMessage;