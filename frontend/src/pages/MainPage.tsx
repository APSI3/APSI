import {AuthHelpers, UserTypes} from "../helpers/AuthHelpers";
import { useNavigate} from "react-router-dom";
import {Paths} from "../App";
import {useEffect} from "react";


export default function MainPage() {
    const navigate = useNavigate();
    useEffect(() => {
        switch (AuthHelpers.getRole()) {
            case UserTypes.PERSON:
                return navigate(Paths.tickets);
            case UserTypes.ORGANIZER:
                return navigate(Paths.events);
            case UserTypes.SUPERADMIN:
                return navigate(Paths.applications);
        }
    }, [navigate]);
    return <></>
}
