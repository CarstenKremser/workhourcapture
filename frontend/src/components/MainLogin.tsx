import '../styles/style.css';
import '../styles/styles_login.css';
import React, {ReactElement, useState} from "react";
import {User} from './UserData.ts';
import {useNavigate} from "react-router-dom";

type LoginCardProps = {
    setUser: (user: User | null) => void;
}

export function MainLogin(props: LoginCardProps): ReactElement {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const navigate = useNavigate()

    function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        console.log("Login submit");
        const user: User = {
            id: "",
            username: username,
            password: password
        }
        console.log("LoginCard - User is: ",user);
        props.setUser(user);
        navigate("/");
    }

    function loginForm(): ReactElement {
        return <>
            <form className="login-form" onSubmit={handleSubmit}>
                <h2> Login:</h2>
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
                            onChange={event => setUsername(event.target.value)}
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
                            onChange={event => setPassword(event.target.value)}
                        />
                    </li>
                    <li>
                        <input
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