import { UserDTO } from "./DTOs"

export type ApiResponse<T> = {
    statusCode: number,
    data?: T,
    errors: { [key: string]: string[] },
    success: boolean,
}

export type LoginResponse = {
    user: UserDTO
}