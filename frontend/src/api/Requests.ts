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
    sections: SimpleSectionDTO[],
    image?: File,
    sectionMap?: File,
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

export type SimpleSectionDTO = {
    id?: number, 
    name: string,
    capacity: number
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
    ticketTypeId: number,
    sectionId: number,
    holderFirstName: string,
    holderLastName: string,
}

export type CreateUserRequest = {
    login: string,
    email: string,
    password: string,
}

export type CreateFormRequest = {
    login: string,
    email: string,
    password: string
}

export type RejectionRequest = {
    id: number,
    cause: string,
}