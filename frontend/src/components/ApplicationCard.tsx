import React from "react";
import { FormDTO } from "../api/DTOs";
import {Box, Paper, Typography} from "@mui/material";

const ApplicationCard: React.FC<{ application: FormDTO }> = ({ application }) => {
    return (
        <Paper style={{ alignItems: 'center', display: 'flex', justifyContent: 'space-evenly', margin: '0.5rem', background: '#dee2e6' }}  elevation={3} >
            <Box style={{
                display: "flex",
                width: "100%",
                justifyContent: "space-between",
                alignItems: "center"
            }}
                 padding={2} border={1} borderColor="grey.300" borderRadius={1}>
                <Typography variant="body1" style={{flex: 1}}>{application.login}</Typography>
                <Typography variant="body1" style={{flex: 1}}>{application.email}</Typography>
                <Typography variant="body1" style={{flex: 1}}>{application.status}</Typography>
            </Box>
        </Paper>
    );
}

export default ApplicationCard;