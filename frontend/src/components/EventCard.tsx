import React from "react";
import {EventDTO} from "../api/DTOs";

import {AuthHelpers, UserTypes} from "../helpers/AuthHelpers";
import DetailsButton from "./EventCardButtons/DetailsButton";
import EditButton from "./EventCardButtons/EditButton";
import DeleteButton from "./EventCardButtons/DeleteButton";

function getActionButtonsBasedOnRole(id: number) {
    const role = AuthHelpers.getRole()
    switch (role) {
        case UserTypes.SUPERADMIN:
        case UserTypes.ORGANIZER:
            return [<DetailsButton id={id}/>, <EditButton/>, <DeleteButton/>]
        default:
            return [<DetailsButton id={id}/>]
    }
}

const EventCard: React.FC<{ event: EventDTO }> = ({ event }) => {
    return (
        <div className="card mb-4">
            <div className="card-body">
                <h5 className="card-title">{event.name}</h5>
                <p className="card-text">{event.description}</p>
                <ul className="list-group list-group-flush">
                    <li className="list-group-item"><strong>Data początkowa:</strong> {new Date(event.startDate).toLocaleDateString()}</li>
                    <li className="list-group-item"><strong>Data końcowa:</strong> {new Date(event.endDate).toLocaleDateString()}</li>
                </ul>
                <div className="d-flex justify-content-end gap-1">
                    {getActionButtonsBasedOnRole(event.id).map((button, index) =>
                        <React.Fragment key={index}>
                            {button}
                        </React.Fragment>)}
                </div>
            </div>
        </div>
    );
};


export default EventCard;