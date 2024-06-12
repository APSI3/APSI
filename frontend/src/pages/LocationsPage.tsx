import {useCallback, useEffect, useState} from "react";
import {LocationDTO} from "../api/DTOs";
import {Api} from "../api/Api";

export default function LocationsPage() {
    const [locations, setLocations] = useState<LocationDTO[]>([]);
    const [currentIdx, setCurrentIdx] = useState(0);
    const [maxIdx, setMaxIdx] = useState(0);

    const getLocations = useCallback((currentIdx: number) => {
        Api.GetLocationsPageable(currentIdx).then(res => {
            if (res.success && res.data) {
                console.log(res.data.items);
                setLocations(res.data.items ?? []);
                setMaxIdx(res.data.totalPages - 1);
            }
        })
    }, []);

    useEffect(() => {
        getLocations(currentIdx);
    }, [currentIdx]);

    const handlePageChange = (index: number) => {
        setCurrentIdx(index);
        getLocations(index);
    }
    return <>
        Work in progress
    </>
}