import React, {useEffect, useState} from "react";
import { TicketTypeDTO} from "../api/DTOs";
import { Grid, Paper, Typography} from "@mui/material";
import {Api} from "../api/Api";
import BuyButton from "./EventCardButtons/BuyButton";

const TicketCard: React.FC<{ ticket: TicketTypeDTO, skipApiCheck?: boolean }> = ({ ticket, skipApiCheck }) => {
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

    return (
        <Paper style={{ alignItems: 'center', display: 'flex', justifyContent: 'space-evenly' }}>
            <Grid item xs={6}>
                <Typography variant="h6" component="h2" >
                    {ticket.name}
                </Typography>
            </Grid>
            <Grid item container direction="row">
                <BuyButton ticketTypeId={ticket.id} />
                <Grid item container direction="column" alignItems="flex-end">
                    <Typography variant="body1" color="textSecondary" style={{marginRight: '1rem'}}>
                        <strong>{ticket.price.toFixed(2)} zł</strong>
                    </Typography>
                    <Typography variant="body2" color="textSecondary" style={{marginRight: '1rem'}}>
                        Dostępność: {ticket.quantityAvailable - soldCount}/{ticket.quantityAvailable}
                    </Typography>
                </Grid>
            </Grid>
        </Paper>
    );
}

export default TicketCard;