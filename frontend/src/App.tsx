import {Route, Routes} from "react-router-dom";
import './styles/style.css'
import {User} from "./components/UserData.ts"
import {NavBar} from "./components/NavBar.tsx";
import {HeaderBar} from "./components/HeaderBar.tsx";
import {MainRecordentry} from "./components/MainRecordentry.tsx";
import {MainAnalyse} from "./components/MainAnalyse.tsx";
import {MainLogin} from "./components/MainLogin.tsx";
import {useEffect, useState} from "react";
import {MainLogout} from "./components/MainLogout.tsx";

export default function App() {

    const [currentUser, setCurrentUser] = useState<User | null>(null);

    useEffect(() => {
        console.log("App - Current user is ", currentUser);
    }, [currentUser]);

    return (
        < >
            <HeaderBar />
            <NavBar user={currentUser} />
            <Routes>
                <Route path="/" element={<MainRecordentry user={currentUser}/>} />
                <Route path="/analyse" element={<MainAnalyse user={currentUser}/>} />
                <Route path="/login" element={<MainLogin setUser={setCurrentUser} />} />
                <Route path="/logout" element={<MainLogout setUser={setCurrentUser} />} />
            </Routes>
        </>
    )
}
