import {useEffect, useState} from "react";
import {EventDTO} from "../api/DTOs";
import {Api} from "../api/Api";
import {useParams} from "react-router-dom";
import {Typography, Paper, Grid, Button, IconButton, CardMedia} from '@mui/material';
import { ArrowBack } from '@mui/icons-material';
import {AuthHelpers, UserTypes} from "../helpers/AuthHelpers";

export default function EventPage() {
    // todo - probably role should be context
    const role = AuthHelpers.getRole();
    const { eventId } = useParams();
    const [ event, setEvent ] = useState<EventDTO | null>(null);

    useEffect(() => {
        Api.GetEventById(eventId).then(res => {
            if (res.success && res.data) {
                setEvent(res.data._embedded?.event ?? null);
                console.log(res.success)
            }
        })
    }, []);

    return <>
        <Paper style={{ padding: 20 }}>
            <Grid container spacing={2}>
                {/* Back Button */}
                <Grid item xs={12}>
                    <IconButton onClick={() => window.history.back()} color="primary">
                        <ArrowBack />
                    </IconButton>
                </Grid>
                {/* Title */}
                <Grid item xs={12}>
                    <Typography variant="h4" gutterBottom>
                        Event Title
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
                {/* Description */}
                <Grid item xs={12}>
                    <Typography variant="body1">
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla massa diam, tempus at imperdiet non, efficitur non libero.
                    </Typography>
                </Grid>
                {/* Date */}
                <Grid item xs={12}>
                    <Typography variant="body2" color="textSecondary">
                        Od: January 1, 2025
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                        Do: January 1, 2025
                    </Typography>
                </Grid>
                <Grid item xs={12}>
                    <Typography variant="body2" color="textSecondary">Pozostało biletów: 100</Typography>
                </Grid>
                <Grid item xs={12}>
                {role === UserTypes.PERSON &&
                    <Button variant="contained" color="primary">
                        Buy Ticket
                    </Button>
                }
                </Grid>
            </Grid>
        </Paper>
    </>
}