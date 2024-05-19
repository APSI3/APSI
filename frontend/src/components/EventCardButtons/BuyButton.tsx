import React, { useState } from "react";
import {Box, Fab, IconButton, Modal} from "@mui/material";
import {ShoppingCart} from "@mui/icons-material";
import { ModalBoxStyle } from "../FormButton";
import CloseIcon from '@mui/icons-material/Close';
import { BuyTicketForm } from "../BuyTicketForm";
import { EventDTO, TicketTypeDTO } from "../../api/DTOs";

const BuyButton: React.FC<{ ticketType: TicketTypeDTO, event: EventDTO }> = ({ ticketType, event }) => {
    const [open, setOpen] = useState(false);
    const closeModal = () => setOpen(false);

    return <>
        <Fab
            size="small"
            onClick={() => setOpen(true)}
        >
            <ShoppingCart />
        </Fab>
        <Modal
            open={open}
            onClose={closeModal}
            aria-labelledby="modal-modal-title"
            aria-describedby="modal-modal-description"
        >
            <Box sx={ModalBoxStyle}>
                <IconButton aria-label="close" onClick={closeModal} style={{ position: 'absolute', top: 10, right: 10 }}>
                    <CloseIcon />
                </IconButton>
                <BuyTicketForm ticketType={ticketType} event={event}/>
            </Box>
        </Modal>
    </>
}

export default BuyButton;