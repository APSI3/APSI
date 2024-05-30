import axios from "axios";
import { ApiResponse } from "./Responses";
import { CreateEventRequest, CreateLocationRequest, LoginRequest, CreateTicketRequest, UpdateEventRequest } from "./Requests";
import { toastError } from "../helpers/ToastHelpers";
import { AuthHelpers } from "../helpers/AuthHelpers";
import {CountryDTO, EventDTO, LocationDTO, LoggedUserDTO, TicketTypeDTO, PaginatedList, TicketDTO} from "./DTOs";

axios.defaults.withCredentials = true;

async function getApiResponse<R, T>(method: string, url: string, request?: R, multipart?: boolean): Promise<ApiResponse<T>> {
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
            "Content-Type": !!multipart ? "multipart/form-data" : "application/json",
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
        return await getApiResponse<LoginRequest, LoggedUserDTO>("post", this.url + "/user/login", request);
    }

    static async CreateEvent(request: CreateEventRequest) {
        const eventPart = { ...request, image: undefined }
        const body = {
            event: JSON.stringify(eventPart),
            image: request.image
        }
        return await getApiResponse<object, EventDTO>("post", this.url + "/events", body, true);
    }

    static async UpdateEvent(request: UpdateEventRequest) {
        const eventPart = { ...request, image: undefined }
        const body = {
            event: JSON.stringify(eventPart),
            image: request.image
        }
        return await getApiResponse<object, EventDTO>("put", this.url + `/events/${request.id}`, body, true);
    }

    static async GetEvents(from: Date, to: Date, pageIndex: number) {
        return await getApiResponse<undefined, PaginatedList<EventDTO>>("get", 
            this.url + `/events?from=${from.toISOString()}&to=${to.toISOString()}&pageIndex=${pageIndex}`);
    }

    static async GetEventById(id: string | number | undefined) {
        return await getApiResponse<undefined, EventDTO>("get", this.url + `/events/${id}`);
    }

    static async GetTicketsByHolderId(id: string | undefined, from: Date, to: Date, pageIndex: number) {
        return await getApiResponse<undefined, PaginatedList<TicketDTO>>("get",
            this.url + `/tickets/user/${id}?from=${from.toISOString()}&to=${to.toISOString()}&pageIndex=${pageIndex}`);
    }

    static async GetTicketTypesByEvent(id: string | undefined) {
        return await getApiResponse<undefined, TicketTypeDTO[]>("get", this.url + `/ticket_types/event/${id}`);
    }

    static async GetTicketTypeById(id: number | undefined) {
        return await getApiResponse<undefined, TicketTypeDTO>("get", this.url + `/ticket_types/${id}`);
    }

    static async GetSoldTicketsCount(id: number | undefined) {
        return await getApiResponse<undefined, number>("get", this.url + `/ticket_types/${id}/count`);
    }

    static async CreateTicket(request: CreateTicketRequest) {
        return await getApiResponse<CreateTicketRequest, TicketDTO>("post", this.url + "/tickets", request);
    }

    static async GetCountries() {
        return await getApiResponse<undefined, CountryDTO[]>("get", this.url + "/countries");
    }

    static async CreateLocation(request: CreateLocationRequest) {
        return await getApiResponse<CreateLocationRequest, LocationDTO>("post", this.url + "/locations", request);
    }

    static async GetLocations() {
        return await getApiResponse<undefined, LocationDTO[]>("get", this.url + "/locations");
    }

    static async GetEventImageByEventId(id: string) {
        const authKey = AuthHelpers.GetAuthKey();
        const data = await axios.get(this.url + "/events/images/" + id, {
            validateStatus: status => status <= 500,
            responseType: "text",
            headers: {
                "Authorization": authKey
            }
        }).then(r => r.data)

        return data;
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
