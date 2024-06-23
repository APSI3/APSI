import React, { useCallback, useEffect, useMemo, useState } from "react";
import { EventDTO } from "../api/DTOs";
import { Api } from "../api/Api";
import EventCard from "../components/EventCard";
import DateRangePicker from "../components/DateRangePicker";
import Pages from "../components/Pages";

const initialFrom = new Date();

export default function EventsPage() {
    const [events, setEvents] = useState<EventDTO[]>([]);

    const toDate = useMemo(() => {
        const date = new Date();
        date.setDate(date.getDate() + 7);
        return date;
    }, [])

    const [currentIdx, setCurrentIdx] = useState(0);
    const [maxIdx, setMaxIdx] = useState(0);

    const getEvents = useCallback((from: Date, to: Date, currentIndex: number) => {
        Api.GetEvents(from, to, currentIndex).then(res => {
            if (res.success && res.data) {
                setEvents(res.data.items ?? []);
                setMaxIdx(res.data.totalPages - 1);
            }
        });
    }, []);

    useEffect(() => {
        getEvents(initialFrom, toDate, currentIdx);
    }, [currentIdx, getEvents, toDate]);

    const handleDateChange = useCallback((from: Date, to: Date) => {
        setCurrentIdx(0); // Reset page index when dates change
        getEvents(from, to, 0);
    }, [getEvents]);

    const handlePageChange = (index: number) => {
        setCurrentIdx(index);
        getEvents(initialFrom, toDate, index);
    };

    return (
        <>
            <h2>Wydarzenia</h2>
            <DateRangePicker initialFrom={initialFrom} initialTo={toDate} onDateChange={handleDateChange} />
            <br />
            {events.map(event => (
                <EventCard key={`event-${event.id}`} event={event} />
            ))}
            <br />
            <Pages initialIndex={currentIdx} maxIndex={maxIdx} onPageChange={handlePageChange} />
        </>
    );
}