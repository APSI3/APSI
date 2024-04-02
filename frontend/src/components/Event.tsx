import React from "react";
import {EventDTO} from "../api/DTOs";

const Event: React.FC<{ event: EventDTO }> = ({ event }) => {
    return (
        <div className="card mb-4">
            <div className="card-body">
                <h5 className="card-title">{event.name}</h5>
                <p className="card-text">{event.description}</p>
                <ul className="list-group list-group-flush">
                    <li className="list-group-item"><strong>Data początkowa:</strong> {new Date(event.startDate).toLocaleDateString()}</li>
                    <li className="list-group-item"><strong>Data końcowa:</strong> {new Date(event.endDate).toLocaleDateString()}</li>
                </ul>
            </div>
        </div>
    );
};


export default Event;