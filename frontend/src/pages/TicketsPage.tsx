import {Api} from "../api/Api";
import {useEffect, useState} from "react";
import {AuthHelpers} from "../helpers/AuthHelpers";
import {ExtendedTicketDTO} from "../api/DTOs";
import UserTicketCard from "../components/UserTicketCard";
import DatePicker from "react-datepicker";
import {PaginationBar} from "../components/PaginationBar";

export default function TicketsPage() {
    const [ tickets, setTickets ] = useState<ExtendedTicketDTO[]>([]);
    const [from, setFrom] = useState<Date>(new Date())
    const date = new Date()
    date.setDate(date.getDate() + 7)
    const [to, setTo] = useState<Date>(date)
    const [currentIdx, setCurrentIdx] = useState(0)
    const [maxIds, setMaxIdx] = useState(0)

    useEffect(() => {
        const userData = AuthHelpers.GetUserData();
        Api.GetTicketsByHolderId(userData?.id, from, to, currentIdx).then(res => {
            if (res.success && res.data) {
                console.log(res.data.items)
                setTickets(res.data.items ?? null);
                setMaxIdx(res.data.totalPages - 1);
            }
        })
    }, [from, to, currentIdx]);

    // todo: move date picker code to separate component (wrapper)
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
        {tickets.map(ticket => <UserTicketCard ticket={ticket} key={ticket.id}/>)}
        <br/>
        {maxIds >= 1 && <PaginationBar currentIndex={currentIdx}
                                       onNext={i => setCurrentIdx(i)}
                                       onPrev={i => setCurrentIdx(i)}
                                       maxIndex={maxIds}
        />}
        </>
}