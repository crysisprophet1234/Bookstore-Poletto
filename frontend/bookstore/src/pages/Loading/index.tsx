import { MoonLoader } from "react-spinners"


const Loading = () => {

    return (

        <div className="d-flex justify-content-center align-items-center" style={{ height: 'calc(100% - 70px)' }}>
            <MoonLoader color='#0044E0' speedMultiplier={0.65} />
        </div>

    )

}

export default Loading