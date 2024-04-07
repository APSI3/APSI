import {useEffect, useState} from "react";
import {EventDTO} from "../api/DTOs";
import {Api} from "../api/Api";
import Event from "../components/Event";

export default function CreateEventPage() {
    const [ events, setEvents ] = useState<EventDTO[]>([]);

    useEffect(() => {
        Api.GetEvents().then(res => { 
            if (res.success && res.data) {
                setEvents(res.data._embedded?.events ?? []);
            }
        })
    }, [])

    return <>
        {events.map(event => <Event key={`event-${event.id}`} event={event} />)}
    </>
}