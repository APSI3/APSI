import React, { useCallback, useEffect, useState } from "react";
import { EventDTO } from "../api/DTOs";
import { Api } from "../api/Api";
import EventCard from "../components/EventCard";
import DateRangePicker from "../components/DateRangePicker";
import Pages from "../components/Pages";

export default function EventsPage() {
    const [events, setEvents] = useState<EventDTO[]>([]);
    const initialFrom = new Date();
    const date = new Date();
    date.setDate(date.getDate() + 7);
    const initialTo = date;
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
        getEvents(initialFrom, initialTo, currentIdx);
    }, []);

    const handleDateChange = (from: Date, to: Date) => {
        setCurrentIdx(0); // Reset page index when dates change
        getEvents(from, to, 0);
    };

    const handlePageChange = (index: number) => {
        setCurrentIdx(index);
        getEvents(initialFrom, initialTo, index);
    };

    return (
        <>
            <DateRangePicker initialFrom={initialFrom} initialTo={initialTo} onDateChange={handleDateChange} />
            <br />
            {events.map(event => (
                <EventCard key={`event-${event.id}`} event={event} />
            ))}
            <br />
            <Pages initialIndex={currentIdx} maxIndex={maxIdx} onPageChange={handlePageChange} />
        </>
    );
}