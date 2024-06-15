import {useEffect, useState} from "react";
import {CountryDTO, EventDTO} from "../api/DTOs";
import {Api} from "../api/Api";
import {useParams} from "react-router-dom";
import {Typography, Paper, Grid, IconButton, CardMedia} from '@mui/material';
import { ArrowBack } from '@mui/icons-material';
import TicketCard from "../components/TicketCard";
import { toastError } from "../helpers/ToastHelpers";
import { getExtendedLocationString } from "../helpers/FormHelpers";
import EditButton from "../components/EventCardButtons/EditButton";
import {AuthHelpers, UserTypes} from "../helpers/AuthHelpers";

export default function EventPage() {
    const { eventId } = useParams();
    const [ countries, setCountries ] = useState<CountryDTO[]>([]);
    const [ event, setEvent ] = useState<EventDTO | null>(null);
    const [ image, setImage ] = useState<string | null>(null);

    useEffect(() => {
        Api.GetCountries().then(res => {
            if (res.success && res.data) {
                setCountries(res.data);
            }
            else toastError("Nie udało się pobrać danych wydarzenia")
        })
    }, [])

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
            Api.GetEventImageByEventId(eventId!).then(res => {
                if (res) setImage(`data:image/png;base64,${res}`);
                else toastError("Nie udało się pobrać obrazów dla tego wydarzenia")
            })
        }
    }, [event, eventId, image])

    const country = countries.find(c => c.id === event?.location?.country_id);

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
                {/* Location */}
                {!!event.location && <Grid item xs={12} style={{ display: 'flex', justifyContent: 'center' }}>
                    <Typography variant="body1" gutterBottom>
                        {getExtendedLocationString(event.location)} <br/>
                        {!!country && country.full_name}
                    </Typography>
                </Grid>}
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
                <Grid container direction="column" alignItems="flex-center" gap={1}>
                    {event.ticketTypes.map(ticket => <TicketCard event={event} key={event.id} ticket={ticket}/>)}
                </Grid>
                {/*Edit button*/}
                {AuthHelpers.getRole() !== UserTypes.PERSON && <Grid container xs={12} style={{ justifyContent: 'right' }}>
                    <EditButton event={event} />
                </Grid>}
            </Grid>
        </Paper>
    </>
}