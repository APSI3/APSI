import {LocationDTO} from "../api/DTOs";
import React from "react";

const LocationCard: React.FC<{ location: LocationDTO }> = ({ location }) => {
    return <div className="card mb-4">
        <div className="card-body">
            <h5 className="card-title">{location.city}</h5>
            {location.description && <p className="card-text">{location.description}</p>}
            <ul className="list-group list-group-flush">
                {location.street && <li className="list-group-item">{location.street}{location.building_nr ? ` ${location.building_nr}` : ''}{location.apartment_nr ? ` / ${location.apartment_nr}` : ''}</li>}
                {location.zip_code && <li className="list-group-item">{location.zip_code} {location.city}</li>}
                {location.capacity && <li className="list-group-item"><strong>Pojemność:</strong> {location.capacity}</li>}
            </ul>
        </div>
    </div>
}

export default LocationCard;