import React, {useEffect, useState} from "react";
import {EventDTO, TicketDTO, TicketTypeDTO} from "../api/DTOs";
import {Grid, Link, Typography, Card, CardContent} from "@mui/material";
import {Api} from "../api/Api";


const UserTicketCard: React.FC<{ ticket: TicketDTO }> = ({ ticket }) => {
    const [ ticketType, setTicketType ] = useState<TicketTypeDTO>();
    const [ event, setEvent ] = useState<EventDTO>();

    useEffect(() => {
        Api.GetTicketTypeById(ticket?.ticketTypeId)
            .then(res => {
                let tt = res.data;
                if (res.success && tt) {
                    setTicketType(tt);
                }
                Api.GetEventById(tt?.eventId).then(res2 => {
                    if (res2.success && res2.data) {
                        setEvent(res2.data);
                    }
                })
            })
    }, [])

    return (
        <Card style={{ margin: '1rem' }} elevation={3}>
            <CardContent>
                <Grid container spacing={1}>
                    <Grid item xs={12} sm={4} alignItems="center" justifyContent="center" display='inline-flex' flexDirection='column'>
                        <>
                            <Typography variant="h6" component="h3">
                                <Link href={`/event/${event?.id}`} target="_blank" rel="noopener">
                                    {event?.name}
                                </Link>
                            </Typography>
                            <Typography color="textSecondary">
                                <>{event?.startDate} - {event?.endDate}</>
                            </Typography>
                            {/*todo location*/}
                            {/*<Typography variant="body2" component="p">*/}
                            {/*    Location: {location}*/}
                            {/*</Typography>*/}
                        </>
                    </Grid>
                    <Grid item xs={12} sm={4} alignItems="center" justifyContent="center" display='inline-flex' flexDirection='column' >
                        <>
                            <Typography variant="h6" component="h3">
                                Typ: {ticketType?.name}
                            </Typography>
                            <Typography variant="body2" color="textSecondary">
                                Cena: {ticketType?.price}
                            </Typography>
                        </>
                    </Grid>
                    <Grid item xs={12} sm={4} style={{ textAlign: 'center' }}>
                        <img src={`data:image/png;base64,${ticket?.qrcode}`} alt="QR Code" style={{ maxWidth: '80%' }} />
                    </Grid>
                </Grid>
            </CardContent>
        </Card>
    );
}

export default UserTicketCard;