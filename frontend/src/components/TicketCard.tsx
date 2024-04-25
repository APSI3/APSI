import React from "react";
import { TicketTypeDTO} from "../api/DTOs";
import { Grid, Paper, Typography} from "@mui/material";

const TicketCard: React.FC<{ ticket: TicketTypeDTO, soldCount: number }> = ({ ticket, soldCount }) => {
    return (
        <Paper style={{ alignItems: 'center', display: 'flex', justifyContent: 'space-evenly' }}>
            <Grid item xs={6}>
                <Typography variant="h6" component="h2" >
                    {ticket.name}
                </Typography>
            </Grid>
            <Grid item container direction="column" alignItems="flex-end">
                <Typography variant="body1" color="textSecondary" style={{marginRight: '1rem'}}>
                    <strong>{ticket.price.toFixed(2)} zł</strong>
                </Typography>
                <Typography variant="body2" color="textSecondary" style={{marginRight: '1rem'}}>
                    Dostępność: {ticket.quantityAvailable}
                </Typography>
            </Grid>
        </Paper>
    );
}

export default TicketCard;