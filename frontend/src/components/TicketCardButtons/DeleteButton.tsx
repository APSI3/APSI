import React, {useEffect, useState} from "react";
import {Fab} from "@mui/material";
import {Delete} from "@mui/icons-material";
import {EventDTO, TicketTypeDTO} from "../../api/DTOs";
import {Api} from "../../api/Api";
import {toastDefaultError, toastInfo} from "../../helpers/ToastHelpers";

const DeleteButton: React.FC<{ ticketType: TicketTypeDTO, event: EventDTO, onDelete: (id: number) => void }> = ({ ticketType, event, onDelete })  => {
    const shouldBeDisabled = (ev: EventDTO) => {
        return new Date(ev.endDate).getTime() < Date.now() || ev.ticketTypes.length <= 1
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

    return <Fab size="small" color="error" onClick={handleClick} disabled={disabled}><Delete /></Fab>
}

export default DeleteButton;