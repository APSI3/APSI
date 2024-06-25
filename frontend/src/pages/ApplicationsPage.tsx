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

    const sortApplications = (apps: FormDTO[]) => {
        const statusOrder: { [key: string]: number } = {
            PENDING: 1,
            ACCEPTED: 2,
            REJECTED: 3,
        };

        return apps.sort((a, b) => statusOrder[a.status] - statusOrder[b.status]);
    };

    return <>
        <h2>Wnioski</h2>
        {sortApplications(applications).map(application => (
            <ApplicationCard application={application} key={`application-${application.id}`}/>
        ))}
        <br />
        <Pages initialIndex={currentIdx} maxIndex={maxIdx} onPageChange={handlePageChange} />
    </>
}