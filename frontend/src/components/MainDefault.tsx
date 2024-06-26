import '../styles/style.css';
import {User} from "./UserData.ts";
import {LoginAdviceCard} from "./LoginAdviceCard.tsx";
import {ReactElement, useEffect} from "react";
import {useNavigate} from "react-router-dom";

type MainDefaultProps = {
    user: User | null;
}

export function MainDefault(props: MainDefaultProps): ReactElement {

    const navigate = useNavigate();

    useEffect(() => {
            if (props.user !== undefined && props.user !== null) {
                navigate("/entry");
            }
        },
        []);

    function forwardToEntry():ReactElement {
        return <>
            <p>Weiterleitung zur Eingabeseite...</p>
        </>

    }

    return (<>
        <main className="main-default">
            {(props.user !== undefined && props.user !== null)
                ? forwardToEntry()
                : <LoginAdviceCard />
            }
        </main>
    </>);

}