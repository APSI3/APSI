import React, {useEffect, useState} from "react";
import {Fab} from "@mui/material";
import {Delete} from "@mui/icons-material";
import {EventDTO, TicketTypeDTO} from "../../api/DTOs";
import {Api} from "../../api/Api";
import {toastDefaultError, toastInfo} from "../../helpers/ToastHelpers";

const DeleteButton: React.FC<{ ticketType: TicketTypeDTO, event: EventDTO, onDelete: (id: number) => void }> = ({ ticketType, event, onDelete })  => {
    const shouldBeDisabled = (ev: EventDTO) => {
        // we don't allow to delete ticket types for the events that have already started (on the date of the start of the event) and if this is the only type of ticket
        const currentDate = new Date();
        const eventStartDate = new Date(ev.startDate);

        currentDate.setHours(0, 0, 0, 0);
        eventStartDate.setHours(0, 0, 0, 0);

        return eventStartDate <= currentDate || ev.ticketTypes.length <= 1
    }
    const [disabled, setDisabled] = useState(shouldBeDisabled(event));

    useEffect(() => {
        setDisabled(shouldBeDisabled(event));
    }, [event])

    const handleClick = async () => {
        Api.DeleteTicketType(ticketType.id).then(res => {
            if (res.success) {
                toastInfo("Udało się usunąć typ biletu.");
                onDelete(ticketType.id);
            } else {
                toastDefaultError()
            }
        })
    }

    return <Fab size="small" color="error" onClick={handleClick} disabled={disabled} style={{zIndex: 10}}><Delete /></Fab>
}

export default DeleteButton;