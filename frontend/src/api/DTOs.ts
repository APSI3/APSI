export type UserDTO = {
    id: string;
    login: string;
}

export type LoggedUserDTO = UserDTO & {
    authHeader: string;
}

export interface EventFormData {
    name: string;
    startDate: Date;
    startTime: Date;
    endDate: Date;
    endTime: Date;
    description: string;
    organizerId: number;
}
