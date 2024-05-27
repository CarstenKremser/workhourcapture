import '../styles/style.css';
import '../styles/styles_login.css';
import {ReactElement, useState} from "react";


export function LoginCard(): ReactElement {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    function handleSubmit(event: React.FormEvent<HTMLInputElement>) {
        event.preventDefault();
        console.log("Login submit");
    }

    function loginForm(): ReactElement {
        return <>
            <form className="login-form">
                <h2> Login:</h2>
                <ul>
                    <li className="login-label">
                        <label htmlFor="login-username">Username:</label>
                    </li>
                    <li className="login-input">
                        <input
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
                            onSubmit={handleSubmit}
                            value="login"
                        />
                    </li>
                </ul>
            </form>
        </>;
    }

    return <>
        <div className="login-card">
            {loginForm()}
        </div>
    </>;
}