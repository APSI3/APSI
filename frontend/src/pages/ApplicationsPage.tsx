import {useCallback, useEffect, useState} from "react";
import {FormDTO} from "../api/DTOs";
import {Api} from "../api/Api";
import Pages from "../components/Pages";
import ApplicationCard from "../components/ApplicationCard";

export default function ApplicationsPage() {
    const [applications, setApplications] = useState<FormDTO[]>([]);
    const [currentIdx, setCurrentIdx] = useState(0);
    const [maxIdx, setMaxIdx] = useState(0);

    const getApplications = useCallback((currentIdx: number) => {
        Api.GetForms(currentIdx).then(res => {
            if (res.success && res.data) {
                console.log(res.data.items);
                setApplications(res.data.items ?? []);
                setMaxIdx(res.data.totalPages - 1);
            }
        })
    }, []);

    useEffect(() => {
        getApplications(currentIdx);
    }, [currentIdx, getApplications]);

    const handlePageChange = (index: number) => {
        setCurrentIdx(index);
        getApplications(index);
    }
    return <>
        <h2>Wnioski</h2>
        {applications.map(application => (
            <ApplicationCard application={application} key={`application-${application.id}`} />
        ))}
        <br />
        <Pages initialIndex={currentIdx} maxIndex={maxIdx} onPageChange={handlePageChange} />
    </>
}