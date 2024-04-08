import {Paths} from "../../App";


export default function PersonHeader() {
    return <div className="collapse navbar-collapse">
        <ul className="navbar-nav mr-auto">
            <li className="nav-item">
                <a className="nav-link" href={Paths.events}>Znajdź wydarzenie</a>
            </li>
            <li className="nav-item">
                <a className="nav-link" href={Paths.tickets}>Twoje bilety</a>
            </li>
        </ul>
    </div>
}