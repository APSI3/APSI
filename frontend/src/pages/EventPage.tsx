import {useEffect, useState} from "react";
import {EventDTO, TicketTypeDTO} from "../api/DTOs";
import {Api} from "../api/Api";
import {useParams} from "react-router-dom";
import {Typography, Paper, Grid, IconButton, CardMedia} from '@mui/material';
import { ArrowBack } from '@mui/icons-material';
import {AuthHelpers} from "../helpers/AuthHelpers";
import TicketCard from "../components/TicketCard";

export default function EventPage() {
    const { eventId } = useParams();
    const [ event, setEvent ] = useState<EventDTO | null>(null);
    const [ ticketTypes, setTicketTypes ] = useState<TicketTypeDTO[]>([]);

    useEffect(() => {
        Api.GetEventById(eventId).then(res => {
            if (res.success && res.data) {
                setEvent(res.data._embedded?.event ?? null);
            }
        })
        Api.GetTicketTypesByEvent(eventId).then(res => {
            if (res.success && res.data) {
                setTicketTypes(res.data._embedded?.ticket_types ?? []);
                console.log(res.data._embedded?.ticket_types)
            }
        })
    }, []);

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
                <Grid item xs={12} style={{ display: 'flex', justifyContent: 'center' }}>
                    <CardMedia
                        component="img"
                        image={(require('../statics/event.png'))}
                        alt="Event Image"
                        style={{ maxHeight: '15rem', width: 'auto' }}
                    />
                </Grid>
                {/*TODO: add localization*/}
                {/* Date */}
                <Grid item xs={12}>
                    <Typography variant="body2" color="textSecondary">
                        Od: {new Date(event.startDate).toLocaleDateString()}
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                        Do: {new Date(event.endDate).toLocaleDateString()}
                    </Typography>
                </Grid>
                {/* Description */}
                <Grid item xs={12}>
                    <Typography variant="body1">{event.description}</Typography>
                </Grid>
                <Grid container direction="column" alignItems="flex-center" gap={1}>
                    {ticketTypes.map(ticket => <TicketCard ticket={ticket}/>)}
                </Grid>
            </Grid>
        </Paper>
    </>
}