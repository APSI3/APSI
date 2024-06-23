import React, {useState} from "react";
import {Fab, IconButton } from "@mui/material";
import {Edit} from "@mui/icons-material";
import {EventDTO} from "../../api/DTOs";
import Modal from "@mui/material/Modal";
import Box from "@mui/material/Box";
import CloseIcon from "@mui/icons-material/Close";
import EventForm from "../EventForm";
import { modalStyle } from "../FormButton";
import { UpdateEventRequest } from "../../api/Requests";

const EditButton: React.FC<{ event: EventDTO }> = (
    { event }
) => {
    const [ open, setOpen ] = useState<boolean>(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => {
        setOpen(false)
        window.location.reload()
    };

    const initialValues: UpdateEventRequest = {
        id: event.id,
        name: event.name,
        description: event.description,
        startDate: new Date(event.startDate),
        endDate: new Date(event.endDate),
        ticketTypes: event.ticketTypes,
        startTime: event.startTime?.substring(0, 5),
        endTime: event.endTime?.substring(0, 5),
        location: event.location,
        sections: event.sections,
    }

    return <>
        <Fab
            size="small"
            onClick={handleOpen}
        >
            <Edit />
        </Fab>
        <Modal
            open={open}
            onClose={handleClose}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
        >
            <Box sx={modalStyle}>
                <IconButton aria-label="close" onClick={handleClose} style={{ position: 'absolute', top: 10, right: 10 }}>
                    <CloseIcon />
                </IconButton>
                {<EventForm
                    onClose={handleClose}
                    initialValues={initialValues}
                    hasImage={event.hasImage}
                    hasSectionMap={event.hasSectionMap}
                />}
            </Box>
        </Modal>
    </>
}

export default EditButton;