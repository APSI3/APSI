import {useEffect, useState} from "react";
import {EventDTO} from "../api/DTOs";
import {Api} from "../api/Api";
import EventCard from "../components/EventCard";

// todo: [JIRA: APSI-16]

export default function EventsPage() {
    const [ events, setEvents ] = useState<EventDTO[]>([]);
    useEffect(() => {
        Api.GetEvents({}).then(res => { if (res.success && res.data) {
            setEvents(res.data.events);
        } })
    }, [])
    return <>
        {events.map(event => <EventCard key={`event-${event.id}`} event={event} />)}
    </>
}