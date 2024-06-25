import React, {useEffect, useState} from "react";
import {Fab} from "@mui/material";
import {Delete} from "@mui/icons-material";
import {EventDTO, TicketTypeDTO} from "../../api/DTOs";
import {Api} from "../../api/Api";
import {toastDefaultError, toastInfo} from "../../helpers/ToastHelpers";
import DateHelper from "../../helpers/DateHelper";

export const isTTDeletionEnabledForEvent = (event: EventDTO) => isTTDeletionEnabled(event.startDate, event.ticketTypes.length);

export const isTTDeletionEnabled = (date: Date, ttLength: number) => {
    // we don't allow to delete ticket types for the events that have already started (on the date of the start of the event) and if this is the only type of ticket
    return DateHelper.isDateInThePast(date) || ttLength <= 1
}

const DeleteButton: React.FC<{ ticketType: TicketTypeDTO, event: EventDTO, onDelete: (id: number) => void }> = ({ ticketType, event, onDelete })  => {
    const [disabled, setDisabled] = useState(isTTDeletionEnabledForEvent(event));

    useEffect(() => {
        setDisabled(isTTDeletionEnabledForEvent(event));
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