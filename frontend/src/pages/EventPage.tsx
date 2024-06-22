import {useEffect, useState} from "react";
import {EventDTO} from "../api/DTOs";
import {Api} from "../api/Api";
import {useParams} from "react-router-dom";
import {Typography, Paper, Grid, IconButton, CardMedia} from '@mui/material';
import { ArrowBack } from '@mui/icons-material';
import TicketCard from "../components/TicketCard";
import { toastError } from "../helpers/ToastHelpers";
import EventSectionItem from "../components/EventSectionItem";

export default function EventPage() {
    const { eventId } = useParams();
    const [ event, setEvent ] = useState<EventDTO | null>(null);
    const [ image, setImage ] = useState<string | null>(null);
    const [ , setSectionMapImage ] = useState<string | null>(null);

    useEffect(() => {
        Api.GetEventById(eventId).then(res => {
            if (res.success && res.data) {
                setEvent(res.data ?? null);
            }
            else toastError("Nie udało się pobrać danych wydarzenia")
        })

    }, [eventId]);

    useEffect(() => {
        if (event != null && event.imageIds.length > 0 && image == null) {
            Api.GetEventImagesByEventId(eventId!).then(res => {
                if (res.success && res.data) {
                    const imgToShow = res.data.find(i => !i.sectionMap)?.image;
                    if (!!imgToShow)
                        setImage(`data:image/png;base64,${imgToShow}`);

                    const sectionMap = res.data.find(i => i.sectionMap)?.image;
                    if (!!sectionMap)
                        setSectionMapImage(`data:image/png;base64,${sectionMap}`);
                }
                else toastError("Nie udało się pobrać obrazów dla tego wydarzenia")
            })
        }
    }, [event, eventId, image])

    return event && <>
        <Paper style={{ padding: 20 }}>
            <Grid container rowSpacing={2}>
                {/* Back Button */}
                <Grid item xs={12}>
                    <IconButton onClick={() => window.history.back()} color="info">
                        <ArrowBack />
                    </IconButton>
                </Grid>
                {/* Title */}
                <Grid item xs={12}>
                    <Typography variant="h4" gutterBottom>
                        {event.name}
                    </Typography>
                </Grid>
                {/* Photo */}
                {!!image && <Grid item xs={12} style={{ display: 'flex', justifyContent: 'center' }}>
                    <CardMedia
                        component="img"
                        src={image}
                        alt="Event Image"
                        style={{ maxHeight: '15rem', width: 'auto' }}
                    />
                </Grid>}
                {/*TODO: add localization*/}
                {/* Date */}
                <Grid item xs={12}>
                    <Typography variant="body2" color="textSecondary">
                        Od: {new Date(event.startDate).toLocaleDateString()} {event.startTime?.substring(0, 5)}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                        Do: {new Date(event.endDate).toLocaleDateString()} {event.endTime?.substring(0, 5)}
                    </Typography>
                </Grid>
                {/* Description */}
                <Grid item xs={12}>
                    <Typography variant="body1">{event.description}</Typography>
                </Grid>
                {/* Ticket types */}
                <Grid container direction="column" alignItems="flex-center" gap={1}>
                    <Typography variant="h5" className="mt-5" gutterBottom>
                        Rodzaje biletów
                    </Typography>
                    {event.ticketTypes.map(ticket => <TicketCard key={ticket.id} ticket={ticket}/>)}
                </Grid>
                {/* Sections */}
                <Grid container direction="column" alignItems="flex-center" gap={1}>
                    <Typography variant="h5" className="mt-5" gutterBottom>
                        Rodzaje miejsc
                    </Typography>
                    {event.sections.map(s => <EventSectionItem key={s.id} section={s} />)}
                </Grid>
            </Grid>
        </Paper>
    </>
}