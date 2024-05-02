import React from "react";
import { Box } from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import AddLocationIcon from "@mui/icons-material/AddLocation";
import FormButton from "./FormButton";
import EventForm from "./EventForm";
import LocationForm from "./LocationForm";

const style = {
    position: 'fixed',
    bottom: 70,
    right: 10,
    display: 'flex',
    flexDirection: 'column',
    gap: 1,
    zIndex: 1000,
};

const ActionButtons: React.FC = () => {
    const actionButtons = [
        { title: "Dodaj wydarzenie", icon: <AddIcon />, form: <EventForm/> },
        { title: "Dodaj lokalizację", icon: <AddLocationIcon />, form: <LocationForm /> },
    ]

    return (
        <Box sx={style}>
            {actionButtons.map( (button, idx) => <
                FormButton
                    key={`form-button-${idx}`}
                    title={button.title}
                    icon={button.icon}
                    form={button.form}
            />)}
        </Box>
    );
};


export default ActionButtons;