import { useNavigate } from "react-router-dom";
import { Paths } from "../App";
import { AuthHelpers } from "../helpers/AuthHelpers";

export default function Header() {
    const nav = useNavigate();
    const login = AuthHelpers.GetUserData()?.login;

    return <nav className="navbar navbar-expand-md fixed-top navbar-dark bg-dark">
        <a href={Paths.main} className="navbar-title">APSI</a>
        <div className="collapse navbar-collapse">
            <ul className="navbar-nav mr-auto">
                {/* TODO: tu powinno być sprawdzenie czy user ma rolę odpowiednią */}
                {<li className="nav-item">
                    <a className="nav-link" href={Paths.createEvent}>Dodaj wydarzenie</a>
                </li>}
                {<li className="nav-item">
                    <a className="nav-link" href={Paths.events}>Twoje wydarzenia</a>
                </li>}
            </ul>
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