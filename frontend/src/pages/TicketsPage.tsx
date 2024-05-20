import {Api} from "../api/Api";
import {useEffect, useState} from "react";
import {AuthHelpers} from "../helpers/AuthHelpers";
import {ExtendedTicketDTO} from "../api/DTOs";
import UserTicketCard from "../components/UserTicketCard";
import DateRangePicker from "../components/DateRangePicker";
import Pages from "../components/Pages";

export default function TicketsPage() {
    const [tickets, setTickets] = useState<ExtendedTicketDTO[]>([]);
    const initialFrom = new Date();
    const date = new Date();
    date.setDate(date.getDate() + 7);
    const initialTo = date;
    const [currentIdx, setCurrentIdx] = useState(0);
    const [maxIdx, setMaxIdx] = useState(0);

    useEffect(() => {
        const userData = AuthHelpers.GetUserData();
        Api.GetTicketsByHolderId(userData?.id, initialFrom, initialTo, currentIdx).then(res => {
            if (res.success && res.data) {
                setTickets(res.data.items ?? null);
                setMaxIdx(res.data.totalPages - 1);
            }
        });
    }, []);

    const handleDateChange = (from: Date, to: Date) => {
        const userData = AuthHelpers.GetUserData();
        Api.GetTicketsByHolderId(userData?.id, from, to, currentIdx).then(res => {
            if (res.success && res.data) {
                setTickets(res.data.items ?? null);
                setMaxIdx(res.data.totalPages - 1);
            }
        });
    };

    const handlePageChange = (index: number) => {
        setCurrentIdx(index);
        const userData = AuthHelpers.GetUserData();
        Api.GetTicketsByHolderId(userData?.id, initialFrom, initialTo, index).then(res => {
            if (res.success && res.data) {
                setTickets(res.data.items ?? null);
            }
        });
    };

    return <>
        <DateRangePicker initialFrom={initialFrom} initialTo={initialTo} onDateChange={handleDateChange} />
        {tickets.map(ticket => <UserTicketCard ticket={ticket} key={ticket.id}/>)}
        <br/>
        <Pages initialIndex={currentIdx} maxIndex={maxIdx} onPageChange={handlePageChange} />
        </>
}