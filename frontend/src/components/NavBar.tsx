import {ReactElement} from 'react';
import {Link, NavLink} from "react-router-dom";
import timeIconWhite from '../assets/TimeIcon_white.svg'
import '../styles/style.css'

export function NavBar(): ReactElement {
    return (<>
        <nav className="navbar">
            <Link to="/">
            <div className="navbar-title">
                <div className="navbar-icon">
                    <img className="title-icon" src={timeIconWhite} alt="TimeIcon"/>
                </div>
                <div className="navbar-greeting">
                    Hallo USERNAME
                </div>
            </div>
            </Link>
            <ul className="navbar-items">
            <li className="navbar-item">
                    <NavLink to={"/analyse"}>Auswertung</NavLink>
                </li>
                <li className="navbar-item">
                    <NavLink to={"/login"}>login</NavLink>
                </li>
            </ul>
        </nav>
    </>)
}