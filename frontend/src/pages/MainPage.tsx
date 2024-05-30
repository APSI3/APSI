import {AuthHelpers, UserTypes} from "../helpers/AuthHelpers";
import { useNavigate} from "react-router-dom";
import {Paths} from "../App";
import {useEffect} from "react";


export default function MainPage() {
    const navigate = useNavigate();
    useEffect(() => {
        const isPerson = AuthHelpers.getRole() === UserTypes.PERSON;
        if (isPerson) {
            return navigate(Paths.tickets);
        } else {
            return navigate(Paths.events)
        }
    }, []);
    return <></>
}
