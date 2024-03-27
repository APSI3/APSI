
export interface EventFormProps {
    onSubmit: (data: EventFormData) => void;
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
