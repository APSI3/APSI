import {useLocation} from "react-router-dom";
import {Grid, Paper, Typography} from "@mui/material";

export default function TicketSummaryPage() {
    const location = useLocation();
    const ticketInfo = location.state;

    return <Paper elevation={3} style={{ padding: '20px', margin: '20px', maxWidth: '600px' }}>
        <Grid container spacing={2}>
            <Grid item xs={12}>
                <Typography variant="h5">Podsumowanie zamówienia</Typography>
            </Grid>
            <Grid item xs={12}>
                <img src={`data:image/png;base64,${ticketInfo.qrcode}`} alt="QR Code" style={{ maxWidth: '100%' }} />
            </Grid>
        </Grid>
    </Paper>
}