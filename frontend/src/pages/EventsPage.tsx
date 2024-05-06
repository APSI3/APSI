import {useCallback, useEffect, useState} from "react";
import {EventDTO} from "../api/DTOs";
import {Api} from "../api/Api";
import EventCard from "../components/EventCard";
import DatePicker from "react-datepicker";
import { PaginationBar } from "../components/PaginationBar";

// todo: [JIRA: APSI-16]

export default function EventsPage() {
    const [events, setEvents] = useState<EventDTO[]>([]);
    const [from, setFrom] = useState<Date>(new Date())
    const date = new Date()
    date.setDate(date.getDate() + 7)
    const [to, setTo] = useState<Date>(date)
    const [currentIdx, setCurrentIdx] = useState(0)
    const [maxIds, setMaxIdx] = useState(0)

    const getEvents = useCallback(() => {
        Api.GetEvents(from, to, currentIdx).then(res => {
            if (res.success && res.data) {
                setEvents(res.data.items ?? []);
                setMaxIdx(res.data.totalPages - 1)
            }
        })
    }, [from, to, currentIdx])

    useEffect(() => getEvents(), [getEvents])

    return <>
        <div>
            <h4>Data wydarzenia</h4>
            <span className="d-inline-flex">
                <label className="m-2">Od: </label>
                <div className="m-2">
                    <DatePicker className="datepicker"
                        dateFormat={"dd/MM/yyyy"}
                        selected={from}
                        onChange={e => setFrom(e ?? new Date())}
                    />
                </div>
                <label className="m-2">Do:</label>
                <div className="m-2">
                    <DatePicker className="datepicker"
                        dateFormat={"dd/MM/yyyy"}
                        selected={to}
                        onChange={e => setTo(e ?? new Date())}
                    />
                </div>
            </span>
        </div>
        <br/>
        {events.map(event => <EventCard key={`event-${event.id}`} event={event} />)}
        <br/>
        {maxIds >= 1 && <PaginationBar currentIndex={currentIdx}
            onNext={i => setCurrentIdx(i)}
            onPrev={i => setCurrentIdx(i)}
            maxIndex={maxIds}
        />}
    </>
}