import '../styles/style.css';
import '../styles/styles_login.css';
import React, {ReactElement, useState} from "react";
import {User} from './UserData.ts';
import {useNavigate} from "react-router-dom";
import axios from "axios";

type LoginCardProps = {
    setUser: (user: User | null) => void;
}

export function MainLogin(props: LoginCardProps): ReactElement {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [statustext, setStatustext] = useState("");

    const navigate = useNavigate()

    function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        console.log("Login submit");
        axios("/api/user/login", {
                method: "post",
                auth: {
                    username: username,
                    password: password
                }
            }
        ).then((response) => {
            props.setUser(response.data);
            console.log(response.data);
        }).then(() => navigate("/")
        ).catch((error) => {
                //console.log(error);
                if (error.response.status === 401) {
                    setStatustext("Benutzer/Kennwort unbekannt");
                } else {
                    setStatustext("Login fehlgeschlagen");
                }
        });
    }

    function loginForm(): ReactElement {
        return <>
            <form className="login-form" onSubmit={handleSubmit}>
                <h2> Login</h2>
                <ul>
                    <li className="login-label">
                        <label htmlFor="login-username">Username:</label>
                    </li>
                    <li className="login-input">
                        <input
                            autoFocus
                            id={"login-username"}
                            type={"text"}
                            value={username}
                            onChange={event => {
                                setUsername(event.target.value);
                                setStatustext("");
                            }}
                        />
                    </li>
                    <li className="login-label">
                        <label htmlFor="login-password">Passwort:</label>
                    </li>
                    <li className="login-input">
                        <input
                            id={"login-password"}
                            type={"password"}
                            value={password}
                            onChange={event => {
                                setPassword(event.target.value);
                                setStatustext("");
                            }}
                        />
                    </li>
                    <li className="login-statustext">
                        <p>{statustext}</p>
                    </li>
                    <li>
                        <input
                            className="buttonsubmit"
                            id={"login-submit"}
                            type={"submit"}
                            value="login"
                        />
                    </li>
                </ul>
            </form>
        </>;
    }

    return <>
        <main className="main-login">
            {loginForm()}
        </main>
    </>;
}