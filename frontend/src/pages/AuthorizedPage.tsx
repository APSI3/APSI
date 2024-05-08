import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Paths } from "../App";
import { Api } from "../api/Api";
import {AuthHelpers, UserTypes} from "../helpers/AuthHelpers";
import { CustomToastContainer } from "../helpers/ToastHelpers";
import Header from "../components/Header";
import ActionButtons from "../components/ActionButtons";

type AuththorizedPageProps = {
    page: JSX.Element,
}

export function AuthorizedPage({ page }: AuththorizedPageProps) {
    const nav = useNavigate();

    useEffect(() => {
        async function checkIfLoggedIn() {
            const sessionFront = AuthHelpers.IsLoggedIn();
            const sessionBack = await Api.Session();

            if (!(sessionBack && sessionFront)) {
                AuthHelpers.ClearAllData();
                nav(Paths.login);
            }
        }

        checkIfLoggedIn();
    }, [nav])

    const isOrganizer = AuthHelpers.HasAnyRole([UserTypes.ORGANIZER, UserTypes.SUPERADMIN]);

    return <>
        <header>
            <Header />
        </header>
        <main className='app container'>
            <CustomToastContainer />
            {page}
            {isOrganizer && <ActionButtons />}
        </main>
    </>
}