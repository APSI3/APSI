import {Api} from "../api/Api";
import {useEffect, useState} from "react";
import {AuthHelpers} from "../helpers/AuthHelpers";
import {TicketDTO} from "../api/DTOs";
import UserTicketCard from "../components/UserTicketCard";

export default function TicketsPage() {
    const [ tickets, setTickets ] = useState<TicketDTO[]>([]);
    useEffect(() => {
        const userData = AuthHelpers.GetUserData();
        Api.GetTicketsByHolderId(userData?.id).then(res => {
            if (res.success && res.data) {
                setTickets(res.data ?? null);
            }
        })
    }, []);
    return <>{tickets.map(ticket => <UserTicketCard ticket={ticket} key={ticket.id}/>)}</>
}