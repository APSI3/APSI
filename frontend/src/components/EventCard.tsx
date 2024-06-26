import React from "react";
import {EventDTO} from "../api/DTOs";
import {AuthHelpers, UserTypes} from "../helpers/AuthHelpers";
import DetailsButton from "./EventCardButtons/DetailsButton";
import EditButton from "./EventCardButtons/EditButton";
import DeleteButton from "./EventCardButtons/DeleteButton";
import { Typography } from "@mui/material";

function getActionButtonsBasedOnRole(event: EventDTO, callback: (id: number) => void) {
    const role = AuthHelpers.getRole()
    switch (role) {
        case UserTypes.SUPERADMIN:
        case UserTypes.ORGANIZER:
            return [<DetailsButton id={event.id} />, <EditButton event={event} />, <DeleteButton event={event} callback={callback}/>]
        default:
            return [<DetailsButton id={event.id}/>]
    }
}

const EventCard: React.FC<{ event: EventDTO, deleteEvent: (id: number) => void}> = ({ event, deleteEvent }) => {
    const isAdmin = AuthHelpers.HasAnyRole([UserTypes.SUPERADMIN]);

    return (
        <div className="card mb-4">
            <div className="card-body">
                {event.canceled && <Typography variant="h6" color="red">WYDARZENIE ANULOWANE</Typography>}
                <h5 className="card-title">{event.name}</h5>
                <p className="card-text">{event.description}</p>
                <ul className="list-group list-group-flush">
                    <li className="list-group-item"><strong>Data początkowa:</strong> {new Date(event.startDate).toLocaleDateString()}</li>
                    <li className="list-group-item"><strong>Data końcowa:</strong> {new Date(event.endDate).toLocaleDateString()}</li>
                </ul>
                {isAdmin && <div>Id organizatora: {event.organizerId}</div>}
                <div className="d-flex justify-content-end gap-1">
                    {getActionButtonsBasedOnRole(event, deleteEvent).map((button, index) =>
                        <React.Fragment key={index}>
                            {button}
                        </React.Fragment>)
                    }
                </div>
            </div>
        </div>
    );
};


export default EventCard;