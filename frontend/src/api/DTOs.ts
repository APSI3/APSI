export type UserDTO = {
    id: string;
    login: string;
}

export type LoggedUserDTO = UserDTO & {
    authHeader: string;
}

export interface EventDTO {
    id: number,
    name: string;
    startDate: Date;
    endDate: Date;
    description: string;
    organizerId: number;
}
