export type ApiResponse<T> = {
    data?: T,
    errors: { [key: string]: string },
    success: boolean,
}