import React from "react";
import { ExtendedTicketDTO } from "../api/DTOs";
import {Grid, Link, Typography, Card, CardContent} from "@mui/material";


const UserTicketCard: React.FC<{ ticket: ExtendedTicketDTO }> = ({ ticket }) => {
    console.log(ticket)
    return (
        <Card style={{ margin: '1rem' }} elevation={3}>
            <CardContent>
                <Grid container spacing={1}>
                    <Grid item xs={12} sm={4} alignItems="center" justifyContent="center" display='inline-flex' flexDirection='column'>
                        <>
                            <Typography variant="h6" component="h3">
                                <Link href={`/event/${ticket.eventId}`} target="_self" rel="noopener">
                                    {ticket.eventName}
                                </Link>
                            </Typography>
                            <Typography color="textSecondary">
                                <Grid sx={{display: 'flex', flexDirection:'row', gap: '1rem'}} alignItems="center" justifyContent="center">
                                    <Grid container sx={{ display:'flex', flexDirection:'column'}}>
                                        <span>{ticket.eventStartTime?.substring(0, 5)}</span>
                                        <span>{new Date(ticket.eventStartDate).toLocaleDateString()}</span>
                                    </Grid>
                                    <Grid item>-</Grid>
                                    <Grid container sx={{ display:'flex', flexDirection:'column'}}>
                                        <span>{ticket.eventEndTime?.substring(0, 5)}</span>
                                        <span>{new Date(ticket.eventEndDate).toLocaleDateString()}</span>
                                    </Grid>
                                </Grid>
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
                                Typ: {ticket.ticketTypeName}
                            </Typography>
                            <Typography variant="body2" color="textSecondary">
                                Cena: {ticket.price}
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