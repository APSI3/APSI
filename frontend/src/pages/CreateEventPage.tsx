import EventForm from "../components/EventForm";
import { EventFormData } from "../model/Event";

export default function CreateEventPage() {
    async function handleSubmit(data: EventFormData) {
        try {
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