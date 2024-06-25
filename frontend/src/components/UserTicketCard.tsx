import React from "react";
import { TicketDTO } from "../api/DTOs";
import {Grid, Link, Typography, Card, CardContent} from "@mui/material";


const UserTicketCard: React.FC<{ ticket: TicketDTO }> = ({ ticket }) => {
    console.log(ticket)
    return (
        <Card style={{ margin: '1rem' }} elevation={3}>
            <CardContent>
                {ticket.event.canceled && <Typography variant="h6" color="red">WYDARZENIE ANULOWANE</Typography>}
                <Grid container spacing={1}>
                    <Grid item xs={12} sm={4} alignItems="center" justifyContent="center" display='inline-flex' flexDirection='column'>
                        <>
                            <Typography variant="h6" component="h3">
                                <Link href={`/event/${ticket.event.id}`} target="_self" rel="noopener">
                                    {ticket.event.name}
                                </Link>
                            </Typography>
                            <Typography color="textSecondary">
                                <Grid sx={{display: 'flex', flexDirection:'row', gap: '1rem'}} alignItems="center" justifyContent="center">
                                    <Grid container sx={{ display:'flex', flexDirection:'column'}}>
                                        <span>{ticket.event.startTime?.substring(0, 5)}</span>
                                        <span>{new Date(ticket.event.startDate).toLocaleDateString()}</span>
                                    </Grid>
                                    <Grid item>-</Grid>
                                    <Grid container sx={{ display:'flex', flexDirection:'column'}}>
                                        <span>{ticket.event.endTime?.substring(0, 5)}</span>
                                        <span>{new Date(ticket.event.endDate).toLocaleDateString()}</span>
                                    </Grid>
                                </Grid>
                            </Typography>
                        </>
                    </Grid>
                    <Grid item xs={12} sm={4} alignItems="center" justifyContent="center" display='inline-flex' flexDirection='column' >
                        <>
                            <Typography variant="h6" component="h3">
                                Typ: {ticket.ticketType.name}
                            </Typography>
                            <Typography variant="body2" color="textSecondary">
                                Cena: {ticket.ticketType.price}
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