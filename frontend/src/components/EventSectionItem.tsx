import React, {  } from "react";
import { SectionDTO } from "../api/DTOs";
import { Grid, Paper, Typography } from "@mui/material";

const EventSectionItem: React.FC<{ section: SectionDTO }> = ({ section }) => {
    return (
        <Paper style={{ alignItems: 'center', display: 'flex', justifyContent: 'space-evenly', background: '#dee2e6' }} elevation={2} >
            <Grid item xs={20} >
                <Typography variant="h6">
                    {section.name}
                </Typography>
            </Grid>
            <Grid item container direction="row" justifyContent="flex-end" padding='0.5rem'>
                <Grid item container direction="column" alignItems="flex-end">
                    <Typography variant="inherit" color="textSecondary" >
                        Dostępność: {section.capacity - section.boughtTickets}/{section.capacity}
                    </Typography>
                </Grid>
            </Grid>
        </Paper>
    );
}

export default EventSectionItem;