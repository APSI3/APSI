import { useParams} from "react-router-dom";
import { EventReportDTO } from "../api/DTOs";
import { useEffect, useState } from "react";
import { Api } from "../api/Api";
import { toastError } from "../helpers/ToastHelpers";

export default function EventReportPage() {
    const { eventId } = useParams();
    const [report, setReport] = useState<EventReportDTO | undefined>();

    useEffect(() => {
        if (!!eventId) {
            Api.GetEventReport(eventId).then(res => {
                if (res.success && res.data) {
                    setReport(res.data);
                }
                else toastError("Nie udało się pobrać danych do raportu")
            })
        }
    }, [eventId]);

    if (!eventId) {
        return <>
            <h3>Nie udało się pobrać danych do raportu</h3>
        </>
    }

    return <div className="d-flex justify-content-center">
        
    </div>
}