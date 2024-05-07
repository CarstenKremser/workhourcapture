import {NavLink} from "react-router-dom";
import '../style.css'

export function NavBar() {
    return (<>
        <nav className="navbar">
            <div className="navbar-title">
                Hallo USERNAME
            </div>
            <ul className="navbar-items">
                <li className="navbar-item">
                    <NavLink to={"/analyse"}>Auswertung</NavLink>
                </li>
                <li className="navbar-item">
                    login
                </li>
            </ul>
        </nav>
    </>)
}