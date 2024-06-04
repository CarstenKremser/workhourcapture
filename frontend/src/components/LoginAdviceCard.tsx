import '../styles/style.css';

import {ReactElement} from "react";

export function LoginAdviceCard(): ReactElement {
    return <>
        <main className={"main-loginadvice"}>
            <div className="login-advice-card">
                <h2>Login erforderlich</h2>
                <p>Um WorkHourCapture nutzen zu können, musst Du Dich einloggen.</p>
            </div>
        </main>
    </>
}