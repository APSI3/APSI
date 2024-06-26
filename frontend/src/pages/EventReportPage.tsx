import { useParams} from "react-router-dom";
import { EventReportDTO, SectionDTO, TicketDTO } from "../api/DTOs";
import React, { useEffect, useState } from "react";
import { Api } from "../api/Api";
import { toastError } from "../helpers/ToastHelpers";

export default function EventReportPage() {
    const { eventId } = useParams();
    const [report, setReport] = useState<EventReportDTO | undefined>();

    useEffect(() => {
        if (!!eventId) {
            Api.GetEventReport(eventId).then(res => {
                if (res.success && res.data) {
                    setReport(res.data);
                }
                else toastError("Nie udało się pobrać danych do raportu")
            })
        }
    }, [eventId]);

    if (!eventId) {
        return <>
            <h3>Nie udało się pobrać danych do raportu</h3>
        </>
    }

    const locationMax = report?.event.location?.capacity;
    const sectionMax = report?.event.sections.reduce((s, curr) => s + curr.capacity, 0);
    const ticketTypeMax = report?.event.ticketTypes.reduce((s, curr) => s + curr.quantityAvailable, 0);
    const totalBought = report?.tickets.length;
    const totalProfit = report?.tickets.reduce((s, curr) => s + curr.ticketType.price, 0);

    const countFunction = (tickets: TicketDTO[], section: SectionDTO, idx: number) => {
        const count = tickets.filter(t => t.sectionId === section.id).length;

        return <td style={{border: '1px solid'}} key={section.id + "c" + idx}>
            {count} szt.
        </td>;
    }

    const profitFunction = (tickets: TicketDTO[], section: SectionDTO, idx: number) => {
        const profit = tickets.filter(t => t.sectionId === section.id).reduce((s, curr) => s + curr.ticketType.price, 0);

        return <td style={{border: '1px solid'}} key={section.id + "p" + idx}>
            {profit} zł
        </td>;
    }

    return <div className="justify-content-center">
        <h2>Raport biletów dla wydarzenia {report?.event.name}</h2>
        <br/>
        <div className="card mb-4">
            <div className="card-body">
                <h5 className="card-title">Liczby sprzedanych biletów</h5>
                {generateTable(report, countFunction)}
                <div className="d-grid mb-5">
                    {!!locationMax && <label>
                        Ograniczenie ilości biletów lokalizacji: {locationMax}
                    </label>}
                    {!!sectionMax && <label>
                        Ograniczenie ilości biletów obecnej dystrybucji rodzajów miejsc: {sectionMax}
                    </label>}
                    {!!ticketTypeMax && <label>
                        Ograniczenie ilości biletów obecnej dystrybucji typów biletów: {ticketTypeMax}
                    </label>}
                    {!!totalBought && <label>
                        <strong>Całkowita liczba zakupionych biletów: {totalBought}</strong>
                    </label>}
                </div>
            </div>
        </div>
        <div className="card mb-4">
            <div className="card-body">
                <h5 className="card-title">Zysk ze sprzedanych biletów</h5>
                {generateTable(report, profitFunction)}
                <div className="d-grid mb-5">
                    {!!totalProfit && <label>
                        <strong>Całkowity zysk ze sprzedaży biletów: {totalProfit} zł</strong>
                    </label>}
                </div>
            </div>
        </div>
    </div>
}

function generateTable(
    report: EventReportDTO | undefined,
    tdFunction: (tickets: TicketDTO[], section: SectionDTO, idx: number) => React.JSX.Element
) {
    const colCount = !!report?.event.sections ? report?.event.sections.length + 2 : 2;

    return <table className="table mb-5" style={{borderCollapse: 'collapse', border: '2px solid'}}>
        <thead>
            <tr>
                <th style={{ backgroundColor: 'whitesmoke' }}></th>
                <th style={{ border: '2px solid', backgroundColor: 'whitesmoke' }} colSpan={colCount}>Rodzaj miejsca</th>
            </tr>
            <tr>
                <th style={{ border: '2px solid', backgroundColor: 'whitesmoke' }} rowSpan={1}>Typ biletu</th>
                {report?.event.sections.map(s => <th style={{ border: '1px solid' }} key={'sh' + s.id}>{s.name}</th>)}
            </tr>
        </thead>
        <tbody>
            {report?.event.ticketTypes.map(tt => {
                const ticketsForThisType = report.tickets.filter(t => t.ticketType.id === tt.id);

                return <tr key={tt.id + 'r'}>
                    <th style={{ border: '2px solid' }} key={'tth' + tt.id}>{tt.name}</th>
                    {report.event.sections.map((s, idx) => tdFunction(ticketsForThisType, s, idx))}
                </tr>;
            })}
        </tbody>
    </table>;
}
