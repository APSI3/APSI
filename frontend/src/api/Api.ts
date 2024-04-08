import axios from "axios";
import {ApiResponse, LinkResponse, LoginResponse} from "./Responses";
import {CreateEventRequest, LoginRequest} from "./Requests";
import { toastError } from "../helpers/ToastHelpers";
import { AuthHelpers } from "../helpers/AuthHelpers";
import { EventDTO } from "./DTOs";

axios.defaults.withCredentials = true;

async function getApiResponse<R, T>(method: string, url: string, request?: R): Promise<ApiResponse<T>> {
    let response: ApiResponse<T> = {
        data: undefined,
        errors: {},
        success: false
    };

    const authKey = AuthHelpers.GetAuthKey();
    await axios({
        method: method,
        url: url,
        data: request,
        headers: {
            "Content-Type": "application/json",
            "Authorization": authKey
        }
    }).then(res => {
        response = {
            data: res.data,
            errors: {},
            success: true,
        }
    }).catch(error => {
        if (!!error.response) {
            response = {
                data: undefined,
                errors: error.response.data.errors ?? {},
                success: false
            }

            if (error.response.status === 401) {
                AuthHelpers.ClearAllData();
                toastError("Należy zalogować się przed wykonaniem operacji");
            }

            if (error.response.status === 403)
                toastError("Brak uprawnień do wykonania operacji");
        }
        else console.error('Api Error: ', error.message)
    });

    return response;
}

export class Api {
    private static url = "http://localhost:8080";

    static async Login(request: LoginRequest) {
        return await getApiResponse<LoginRequest, LoginResponse>("post", this.url + "/user/login", request);
    }

    static async CreateEvent(request: CreateEventRequest) {
        return await getApiResponse<CreateEventRequest, EventDTO>("post", this.url + "/events", request);
    }

    static async GetEvents() {
        return await getApiResponse<undefined, LinkResponse<{ events: EventDTO[]}>>("get", this.url + "/events");
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
