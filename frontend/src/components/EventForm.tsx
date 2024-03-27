import { useForm } from 'react-hook-form';
import { EventFormProps, EventFormData } from "../model/Event";

export default function EventForm({ onSubmit }: EventFormProps) {
    const { register, handleSubmit } = useForm<EventFormData>();

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <label> Nazwa:
                <input type="text" {...register('name')}/>
            </label>
            <br />
            <label>
                Od: <input type="datetime-local" {...register('startDate', { valueAsDate: true })}/>
            </label>
            <br />
            <label>
                Do: <input type="datetime-local" {...register('endDate', { valueAsDate: true })}/>
            </label>
            <br />
            <label>
                Opis: <input type="text" {...register('description')}/>
            </label>
            <br />
            {/*TODO: after logging this should be replaced with current user*/}
            <label>
                Organizator: <input type="number" {...register('organizerId')}/>
            </label>

            <input type="submit"/>
        </form>
    )
}