import {Api} from "../api/Api";
import {useCallback, useEffect, useMemo, useState} from "react";
import {AuthHelpers} from "../helpers/AuthHelpers";
import UserTicketCard from "../components/UserTicketCard";
import DateRangePicker from "../components/DateRangePicker";
import Pages from "../components/Pages";
import {TicketDTO} from "../api/DTOs";

export default function TicketsPage() {
    const [tickets, setTickets] = useState<TicketDTO[]>([]);

    const fromDate = useMemo(() => {
        return new Date()
    }, [])

    const toDate = useMemo(() => {
        const date = new Date();
        date.setDate(date.getDate() + 7);
        return date;
    }, [])
    
    const [currentIdx, setCurrentIdx] = useState(0);
    const [maxIdx, setMaxIdx] = useState(0);

    const handleDateChange = useCallback((from: Date, to: Date) => {
        const userData = AuthHelpers.GetUserData();
        Api.GetTicketsByHolderId(userData?.id, from, to, currentIdx).then(res => {
            if (res.success && res.data) {
                setTickets(res.data.items ?? null);
                setMaxIdx(res.data.totalPages - 1);
            }
        });
    }, [currentIdx]);

    useEffect(() => {
        handleDateChange(fromDate, toDate)
    }, [fromDate, handleDateChange, toDate]);

    const handlePageChange = useCallback((index: number) => {
        setCurrentIdx(index);
    }, []);

    return <>
        <h2>Twoje bilety</h2>
        <DateRangePicker initialFrom={fromDate} initialTo={toDate} onDateChange={handleDateChange} />
        {tickets.map(ticket => <UserTicketCard ticket={ticket} key={ticket.id}/>)}
        <br/>
        <Pages initialIndex={currentIdx} maxIndex={maxIdx} onPageChange={handlePageChange} />
        </>
}