import {useLocation} from "react-router-dom";
import {Grid, Paper, Typography} from "@mui/material";
import { EventDTO, TicketDTO } from "../api/DTOs";
import { useEffect, useState } from "react";
import { Api } from "../api/Api";
import { toastError } from "../helpers/ToastHelpers";

export default function TicketSummaryPage() {
    const [event, setEvent] = useState<EventDTO | null>(null);
    const location = useLocation();
    const ticketInfo = location.state as TicketDTO;

    useEffect(() => {
        if (!!ticketInfo?.event.id) {
            Api.GetEventById(ticketInfo.event.id.toString()).then(res => {
                if (res.success && res.data)
                    setEvent(res.data);
                else
                    toastError("Nie udało się pobrać danych wydarzenia")
            })
        }
    }, [ticketInfo.event.id])

    if (!ticketInfo) {
        toastError("Brak danych o bilecie")
        return <>
        </>
    }

    const ticketType = event?.ticketTypes.find(tt => tt.id === ticketInfo.ticketType.id);
    const section = event?.sections.find(s => s.id === ticketInfo.sectionId);

    return <div className="d-flex justify-content-center">
        <Paper elevation={3} style={{ padding: '20px', margin: '20px', maxWidth: '500px' }}>
            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <Typography variant="h5">Podsumowanie zamówienia</Typography>
                </Grid>
                <Grid item xs={12}>
                    <img src={`data:image/png;base64,${ticketInfo.qrcode}`} alt="QR Code" style={{ maxWidth: '100%' }} />
                </Grid>
                <Grid item xs={12}>
                    <label className="form-label">Posiadacz:</label>
                    <span className="form-control">{ticketInfo.holderFirstName} {ticketInfo.holderLastName}</span>
                </Grid>
                <Grid item xs={12}>
                    <label className="form-label">Wydarzenie:</label>
                    <span className="form-control">{event?.name}</span>
                </Grid>
                {event != null && <Grid item xs={12}>
                    <label className="form-label">Czas trwania:</label>
                    <span className="form-control">
                        Od: {new Date(event.startDate).toLocaleDateString()} {event.startTime?.substring(0, 5)}
                        <br/>
                        Do: {new Date(event.endDate).toLocaleDateString()} {event.endTime?.substring(0, 5)}
                    </span>
                </Grid>}
                {event?.description && <Grid item xs={12}>
                    <label className="form-label">Opis:</label>
                    <span className="form-control">{event?.description}</span>
                </Grid>}
                <Grid item xs={12}>
                    <label className="form-label">Typ biletu:</label>
                    <span className="form-control">{ticketType?.name}</span>
                </Grid>
                {section?.name && <Grid item xs={12}>
                    <label className="form-label">Rodzaj miejsca:</label>
                    <span className="form-control">{section.name}</span>
                </Grid>}
            </Grid>
        </Paper>
    </div>
    
}