import {useCallback, useEffect, useState} from "react";
import {LocationDTO} from "../api/DTOs";
import {Api} from "../api/Api";
import LocationCard from "../components/LocationCard";
import Pages from "../components/Pages";

export default function LocationsPage() {
    const [locations, setLocations] = useState<LocationDTO[]>([]);
    const [currentIdx, setCurrentIdx] = useState(0);
    const [maxIdx, setMaxIdx] = useState(0);

    const getLocations = useCallback((currentIdx: number) => {
        Api.GetLocationsPageable(currentIdx).then(res => {
            if (res.success && res.data) {
                setLocations(res.data.items ?? []);
                setMaxIdx(res.data.totalPages - 1);
            }
        })
    }, []);

    useEffect(() => {
        getLocations(currentIdx);
    }, [currentIdx, getLocations]);

    const handlePageChange = (index: number) => {
        setCurrentIdx(index);
        getLocations(index);
    }
    return <>
        {locations.length !== 0 ? <h2>Lokalizacje</h2> : <h5>Brak lokalizacji</h5>}
        {locations.map(location => (
            <LocationCard location={location} key={`location-${location.id}`} />
        ))}
        <br />
        <Pages initialIndex={currentIdx} maxIndex={maxIdx} onPageChange={handlePageChange} />
    </>
}