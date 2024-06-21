import React, {  } from "react";
import { SectionDTO } from "../api/DTOs";
import { Grid, Paper, Typography } from "@mui/material";

const EventSectionItem: React.FC<{ section: SectionDTO }> = ({ section }) => {
    return (
        <Paper style={{ alignItems: 'center', display: 'flex', justifyContent: 'space-evenly', margin: '0.5rem', background: '#dee2e6' }} elevation={3} >
            <Grid item xs={6} >
                <Typography variant="h6" component="h2" >
                    {section.name}
                </Typography>
            </Grid>
            <Grid item container direction="row" justifyContent="flex-end" padding='1rem' style={{ background: '#ffffff' }}>
                <Grid item container direction="column" alignItems="flex-end">
                    <Typography variant="body2" color="textSecondary" >
                        Dostępność: {section.capacity - section.boughtTickets}/{section.capacity}
                    </Typography>
                </Grid>
            </Grid>
        </Paper>
    );
}

export default EventSectionItem;