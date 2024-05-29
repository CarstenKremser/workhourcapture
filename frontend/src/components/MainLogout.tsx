import {User} from "./UserData.ts";
import {useEffect} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

export type MainLogoutProps = {
    setUser: (user: User | null) => void;
}

export function MainLogout(props: MainLogoutProps) {

    const navigate = useNavigate();

    function doLogout():void {
        axios.post("api/user/logout")
            .then( () => {
            })
            .catch((error) => {
                console.log(error.response.status);
            });
        props.setUser(null);
        navigate("/");
    }

    useEffect(() => {
        doLogout();
    }, []);

    return <>
        <main className="main-logout">
            <p>Du wirst auf die Startseite weitergeleitet...</p>
        </main>
    </>;
}