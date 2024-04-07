import { LoggedUserDTO } from "./DTOs"

export type ApiResponse<T> = {
    data?: T,
    errors: { [key: string]: string },
    success: boolean,
}

export type LoginResponse = {
    user: LoggedUserDTO
}

export type LinkResponse<T> = {
    _embedded? : T,
    _links: { 
        [key: string]: {
            [nestedKey: string]: string
        }
    }
}