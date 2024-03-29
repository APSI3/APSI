import axios from "axios";
import { ApiResponse, LoginResponse } from "./Responses";
import { CreateEventRequest, LoginRequest } from "./Requests";
import { toastError } from "../helpers/ToastHelpers";
import { AuthHelpers } from "../helpers/AuthHelpers";

axios.defaults.withCredentials = true;

async function getApiResponse<R, T>(request: R, url: string): Promise<ApiResponse<T>> {
    let response: ApiResponse<T> = {
        data: undefined,
        errors: {},
        success: false
    };
    let statusCode = 400;

    const authKey = AuthHelpers.GetAuthKey();
    await axios.post(url, request ?? {}, {
        validateStatus: status => status <= 500,
        headers: {
            "Content-Type": "application/json",
            "Authorization": authKey
        }
    }).then(res => {
        statusCode = res.status;
        response = {
            data: res.data.data,
            errors: res.data.errors ?? {},
            success: res.data.success ?? false
        }
    }).catch(error => {
        if (error.response)
            response = {
                data: error.response.data.data,
                errors: error.response.data.errors ?? {},
                success: error.response.data.success ?? false
            }
        else
            console.error('Api Error: ', error.message)
    });

    if (statusCode === 401){
        AuthHelpers.ClearAllData();
        toastError("Należy zalogować się przed wykonaniem operacji");
    }

    if (statusCode === 403)
        toastError("Brak uprawnień do wykonania operacji");

    return response;
}

export class Api {
    private static url = "http://localhost:8080";

    static async Login(request: LoginRequest) {
        return await getApiResponse<LoginRequest, LoginResponse>(request, this.url + "/user/login");
    }

    static async CreateEvent(request: CreateEventRequest) {
        return await getApiResponse<CreateEventRequest, null>(request, this.url + "/event/create");
    }

    static async Session() {
        const authKey = AuthHelpers.GetAuthKey();
        const isLoggedIn = await axios.post(this.url + "/user/session", {}, {
            validateStatus: status => status <= 500,
            headers: {
                "Content-Type": "application/json",
                "Authorization": authKey
            }
        }).then(r => {
            return r.data as boolean;
        }).catch(e => {
            return false;
        })

        return isLoggedIn;
    }
}
