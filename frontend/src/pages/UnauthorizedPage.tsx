import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Paths } from "../App";
import { CustomToastContainer } from "../helpers/ToastHelpers";
import { Api } from "../api/Api";
import { AuthHelpers } from "../helpers/AuthHelpers";

type UnAuththorizedPageProps = {
    page: JSX.Element
}

export function UnAuthorizedPage({ page }: UnAuththorizedPageProps) {
    const nav = useNavigate();

    useEffect(() => {
        async function checkIfLoggedIn() {
            const sessionFront = AuthHelpers.IsLoggedIn();
            const sessionBack = await Api.Session();

            if (sessionBack && sessionFront) {
                nav(Paths.main);
                return;
            }

            if (sessionBack && !sessionFront)
                Api.Logout();
        }

        checkIfLoggedIn();
    }, [nav])

    return <>
        <header>

        </header>
        <main className="app container">
            <CustomToastContainer />
            {page}
        </main>
    </>
}