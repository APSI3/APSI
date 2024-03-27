import { useForm } from 'react-hook-form';

interface EventFormProps {
    onSubmit: (data: EventFormData) => void;
}

interface EventFormData {
    name: string;
    startDate: Date;
    startTime: Date;
    endDate: Date;
    endTime: Date;
    description: string;
    organizerId: number;
}

function EventForm({ onSubmit }: EventFormProps) {
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

export default function CreateEventPage() {
    async function handleSubmit(data: EventFormData) {
        try {
            console.log(data);
            const response = await fetch('http://localhost:8080/event', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            })

            if (response.ok) {
                console.log('Form submitter successfully');
            }
            else {
                console.log('Failed to submit form');
            }
        } catch (error) {
            console.error('Error submitting: ', error);
        }
    }
    return <div>
        <EventForm onSubmit={handleSubmit} />
    </div>
}