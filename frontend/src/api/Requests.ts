export type LoginRequest = {
    login: string,
    password: string
}

export type CreateEventRequest = {
    name: string,
    description?: string,
    startDate: Date,
    endDate: Date,
    startTime?: string,
    endTime?: string,
    location?: {
        id: number
    },
    ticketTypes: SimpleTicketTypeDTO[],
    image?: File
}

export type UpdateEventRequest = CreateEventRequest & {
    id: number
}

export type SimpleTicketTypeDTO = {
    id?: number, 
    name: string,
    price: number,
    quantityAvailable: number
}

export type CreateLocationRequest = {
    country_id: number,
    capacity: number | string,
    description: string,
    city: string,
    street: string,
    building_nr: string,
    apartment_nr: string,
    zip_code: string,
}

export type CreateTicketRequest = {
    ticketType: {
        id: number,
    },
    event: {
        id: number,
    }
    holderFirstName: string,
    holderLastName: string
}
