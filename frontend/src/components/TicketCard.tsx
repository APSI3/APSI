import React, {useEffect, useState} from "react";
import { EventDTO, TicketTypeDTO} from "../api/DTOs";
import { Grid, Paper, Typography} from "@mui/material";
import {Api} from "../api/Api";
import BuyButton from "./TicketCardButtons/BuyButton";
import DeleteButton from "./TicketCardButtons/DeleteButton";
import { AuthHelpers, UserTypes } from "../helpers/AuthHelpers";

const TicketCard: React.FC<{ ticket: TicketTypeDTO, skipApiCheck?: boolean, event: EventDTO, onDelete: (id: number) => void }> = ({ ticket, skipApiCheck, event, onDelete }) => {
    const [soldCount, setSoldCount] = useState(0);
    
    useEffect(() => {
        if (!skipApiCheck) {
            Api.GetSoldTicketsCount(ticket.id).then(res => {
                if (res.success && res.data) {
                    setSoldCount(res.data ?? 0);
                }
            })
        }
    }, [ticket.id, skipApiCheck]);

    const canBuyTickets = AuthHelpers.HasAnyRole([ UserTypes.PERSON ]);
    const canDeleteTicket = AuthHelpers.HasAnyRole([ UserTypes.ORGANIZER ]);

    return (
        <Paper style={{ alignItems: 'center', display: 'flex', justifyContent: 'space-evenly', margin: '0.5rem', background: '#dee2e6' }}  elevation={3} >
            <Grid item xs={6} >
                <Typography variant="h6" component="h2" >
                    {ticket.name}
                </Typography>
            </Grid>
            <Grid item container direction="row" justifyContent="flex-end" padding='1rem' style={{background: '#ffffff', height: '100%'}}>
                {canBuyTickets && (ticket.quantityAvailable - soldCount) > 0 && <BuyButton ticketType={ticket} event={event}/>}
                <Grid item container direction="column" alignItems="flex-end">
                    <Typography variant="body1" color="textSecondary" style={{ marginTop: '1rem' }}>
                        <strong>{ticket.price.toFixed(2)} zł</strong>
                    </Typography>
                    <Typography variant="body2" color="textSecondary" >
                        Dostępność: {ticket.quantityAvailable - soldCount}/{ticket.quantityAvailable}
                    </Typography>
                </Grid>
                {canDeleteTicket && <Grid paddingTop='1rem'><DeleteButton ticketType={ticket} event={event} onDelete={onDelete} /></Grid>}
            </Grid>
        </Paper>
    );
}

export default TicketCard;