import {Paths} from "../../App";


export default function PersonHeader() {
    return <div className="collapse navbar-collapse">
        <ul className="navbar-nav mr-auto">
            <li className="nav-item">
                <a className="nav-link" href={Paths.applications}>Wnioski</a>
            </li>
            <li className="nav-item">
                <a className="nav-link" href={Paths.users}>Użytkownicy</a>
            </li>
            <li className="nav-item">
                <a className="nav-link" href={Paths.events}>Wydarzenia</a>
            </li>
            <li className="nav-item">
                <a className="nav-link" href={Paths.locations}>Lokalizacje</a>
            </li>
        </ul>
    </div>
}