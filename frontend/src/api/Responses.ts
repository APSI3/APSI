import { EventDTO, LoggedUserDTO } from "./DTOs"

export type ApiResponse<T> = {
    data?: T,
    errors: { [key: string]: string },
    success: boolean,
}

export type LoginResponse = {
    user: LoggedUserDTO
}

export type CreateEventResponse = {
    event: EventDTO
}