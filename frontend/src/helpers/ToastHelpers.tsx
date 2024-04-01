import { ToastContainer, toast } from "react-toastify";

export function CustomToastContainer() {
    return <div>
        <ToastContainer
            position="top-right"
            autoClose={false}
            newestOnTop
            closeOnClick
            draggable={false}
        />
    </div>
}

export function toastInfo(msg: string) {
    toast(msg, {
        type: 'info'
    })
}

export function toastError(msg: string) {
    toast(msg, {
        type: 'error'
    })
}

export function toastDefaultError() { 
    toastError("Coś poszło nie tak")
}