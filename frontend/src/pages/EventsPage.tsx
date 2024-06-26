import React, { useCallback, useEffect, useMemo, useState } from "react";
import { EventDTO, PaginatedList } from "../api/DTOs";
import { Api } from "../api/Api";
import EventCard from "../components/EventCard";
import DateRangePicker from "../components/DateRangePicker";
import Pages from "../components/Pages";
import { AuthHelpers, UserTypes } from "../helpers/AuthHelpers";
import { ApiResponse } from "../api/Responses";

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

    const handleEvents = (res: ApiResponse<PaginatedList<EventDTO>>) => {
        if (res.success && res.data) {
            setEvents(res.data.items ?? []);
            setMaxIdx(res.data.totalPages - 1);
        }
    }

    const getEvents = useCallback((from: Date, to: Date, currentIndex: number) => {
        if (AuthHelpers.HasAnyRole([UserTypes.ORGANIZER])){
            Api.GetOrganizerEvents(from, to, currentIndex).then(handleEvents);
        }
        else {
            Api.GetEvents(from, to, currentIndex).then(handleEvents);
        }
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
                <EventCard key={`event-${event.id}`} event={event} deleteEvent={(id: number) => { setEvents(events.filter(e => e.id !== id)) }}/>
            ))}
            <br />
            <Pages initialIndex={currentIdx} maxIndex={maxIdx} onPageChange={handlePageChange} />
        </>
    );
}