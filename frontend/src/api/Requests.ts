export type LoginRequest = {
    login: string,
    password: string
}

export type CreateEventRequest = {
    name: string,
    description: string,
    startDate: Date,
    endDate: Date,
}

export type CreateLocationRequest = {
    country_id: number | null,
    capacity: number | null,
    description: string,
    city: string,
    street: string,
    building_nr: string,
    apartment_nr: string,
    zip_code: string,
}
