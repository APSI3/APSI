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

export type GetEventsRequest = {}