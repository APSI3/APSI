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
}

export interface TicketTypeDTO {
    id: number,
    eventId: number,
    name: string,
    price: number,
    quantityAvailable: number,
}