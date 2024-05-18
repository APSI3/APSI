export type UserDTO = {
    id: string;
    login: string;
}

export type LoggedUserDTO = UserDTO & {
    authHeader: string;
    type: string
}

export interface EventDTO {
    id: number,
    name: string;
    startDate: Date;
    endDate: Date;
    description: string;
    organizerId: number;
    startTime?: string
    endTime?: string
    location?: LocationDTO
    ticketTypes: TicketTypeDTO[]
    imageIds: number[]
}

export interface TicketTypeDTO {
    id: number,
    eventId: number,
    name: string,
    price: number,
    quantityAvailable: number,
}

export interface CountryDTO {
    id: number,
    code: string,
    full_name: string,
}

export interface LocationDTO {
    id: number,
    country_id: number,
    capacity?: number,
    description?: string,
    city: string,
    street?: string,
    building_nr?: string,
    apartment_nr?: string,
    zip_code?: string,
    creator_id: number,
}

export interface TicketDTO {
    id: number,
    ticketTypeId: number,
    holderId: number,
    purchaseDate: Date,
}

export interface PaginatedList<T>{
    items: T[],
    pageIndex: number,
    totalItems: number,
    totalPages: number,
}
