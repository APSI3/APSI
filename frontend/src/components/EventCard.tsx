import React from "react";
import {EventDTO} from "../api/DTOs";
import {Fab} from "@mui/material";
import {Delete, Edit, ShoppingCart, Visibility} from "@mui/icons-material";

// todo: buttons should be displayed based on the active user
const userButtons = {
    person: [ <Visibility/>, <ShoppingCart /> ],
    organizer: [ <Visibility/>, <Edit/>, <Delete/> ],
    superadmin: [ <Visibility/>, <Edit/>, <Delete/> ],
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
                    {userButtons.organizer.map(button => <Fab size="small">{button}</Fab>)}
                </div>
            </div>
        </div>
    );
};


export default EventCard;