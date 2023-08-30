import axios from 'axios';
import { useNavigate } from 'react-router-dom';

axios.interceptors.request.use(

    function (config) {
        console.log(config)
        return config;
    },

    function (error) {
        return Promise.reject(error)
    }

)

axios.interceptors.response.use(

    function (response) {
        console.log(response)
        return response
    },

    function (error) {

        if (error.code === 'ERR_NETWORK') {
            return Promise.resolve(error)
        }

        console.log(error)

        const status = error.response.status

        const navigate = useNavigate()

        if (status === 401) {
            navigate('/admin/auth/login')
        }

        return Promise.reject(error)

    }

)
