import axios from "axios";
import { ApiResponse, LoginResponse } from "./Responses";
import { LoginRequest } from "./Requests";
import { toastError } from "../helpers/ToastHelpers";

axios.defaults.withCredentials = true;

async function getApiResponse<R, T>(request: R, url: string): Promise<ApiResponse<T>> {
    let response: ApiResponse<T> = {
        statusCode: 400,
        data: undefined,
        errors: {},
        success: false
    };

    await axios.post(url, request ?? {}, {
        validateStatus: status => status <= 500,
        headers: {
            "Content-Type": "application/json"
        }
    }).then(res => {
        response = {
            statusCode: res.status ?? 400,
            data: res.data.data,
            errors: res.data.errors ?? {},
            success: res.data.success ?? false
        }
    }).catch(error => {
        if (error.response)
            response = {
                statusCode: error.response.status ?? 400,
                data: error.response.data.data,
                errors: error.response.data.errors ?? {},
                success: error.response.data.success ?? false
            }
        else
            console.error('Api Error: ', error.message)
    });

    if (response.statusCode === 401)
        toastError("Brak uprawnień do wykonania operacji");

    return response;
}

export class Api {
    private static url = process.env.REACT_APP_BACKEND_URL;

    static async Login(request: LoginRequest) {
        return await getApiResponse<LoginRequest, LoginResponse>(request, this.url + "/user/login");
    }

    static async Logout() {
        return await getApiResponse<undefined, undefined>(undefined, this.url + "/user/logout");
    }

    static async Session() {
        const isLoggedIn = await axios.post(this.url + "/user/session", {}, {
            validateStatus: status => status <= 500,
            headers: {
                "Content-Type": "application/json"
            }
        }).then(r => {
            return r.data as boolean;
        }).catch(e => {
            return false;
        })

        return isLoggedIn;
    }
}
