import {User} from "./UserData.ts";

export type LogoutCardProps = {
    setUser: (user: User | null) => void;
}

export function MainLogout(props: LogoutCardProps) {
    props.setUser(null);
    return <>
        <main className="main-logout">
            <p>Du wurdest abgemeldet!</p>
        </main>
    </>;
}