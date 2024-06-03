import {ReactElement} from 'react';
import {Link, NavLink} from "react-router-dom";
import timeIconWhite from '../assets/TimeIcon_white.svg'
import '../styles/style.css'
import {User} from "./UserData.ts";

export type NavBarProps = {
    user: User | null;
}

export function NavBar(props: NavBarProps): ReactElement {

    function getDisplayName() {
        if (props.user) {
            return props.user.firstname;
        }
        return "Gast";
    }

    function loginLogoutLink(): ReactElement {
        if (props.user) {
            return <NavLink to={"/logout"}>logout</NavLink>
        } else {
            return <NavLink to={"/login"}>login</NavLink>

        }
    }

    return (<>
        <nav className="navbar">
            <Link to="/">
                <div className="navbar-title">
                    <div className="navbar-icon">
                        <img className="title-icon" src={timeIconWhite} alt="TimeIcon"/>
                    </div>
                    <div className="navbar-greeting">
                        Hallo {getDisplayName()}
                    </div>
                </div>
            </Link>
            <ul className="navbar-items">
                <li className="navbar-item">
                    <NavLink to={"/entry"}>Erfassung</NavLink>
                </li>
                <li className="navbar-item">
                    <NavLink to={"/analyse"}>Auswertung</NavLink>
                </li>
                <li className="navbar-item">
                    {loginLogoutLink()}
                </li>
            </ul>
        </nav>
    </>)
}