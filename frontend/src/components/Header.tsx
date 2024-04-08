import { useNavigate } from "react-router-dom";
import { Paths } from "../App";
import { AuthHelpers, UserTypes } from "../helpers/AuthHelpers";
import SuperAdminHeader from "./headers/SuperAdminHeader"
import OrganizerHeader from "./headers/OrganizerHeader"
import PersonHeader from "./headers/PersonHeader"

function getHeaderBasedOnRole() {
    const role = AuthHelpers.getRole()
    switch (role) {
        case UserTypes.SUPERADMIN:
            return <SuperAdminHeader />
        case UserTypes.ORGANIZER:
            return <OrganizerHeader />
        default:
            return <PersonHeader />
    }
}

export default function Header() {
    const nav = useNavigate();
    const login = AuthHelpers.GetUserData()?.login;

    return <nav className="navbar navbar-expand-md fixed-top navbar-dark bg-dark">
        <a href={Paths.main} className="navbar-title">APSI</a>
        <div className="collapse navbar-collapse">
            {getHeaderBasedOnRole()}
        </div>
        <div className="navbar-login">
            <span className="username">
                {login ? "Zalogowano jako " + login : ""}
            </span>
            {" "}
            <span className="m-1">
                {AuthHelpers.IsLoggedIn() && <button type="button" className="btn btn-outline-light btn-sm"
                    onClick={() => {
                        AuthHelpers.ClearAllData();
                        nav(Paths.login)
                    }}
                >
                    Wyloguj
                </button>}
            </span>
        </div>
    </nav>
}