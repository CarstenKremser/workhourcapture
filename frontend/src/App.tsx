import {Route, Routes} from "react-router-dom";
import './style.css'
import {NavBar} from "./components/NavBar.tsx";
import {HeaderBar} from "./components/HeaderBar.tsx";
import {MainRecordentry} from "./components/MainRecordentry.tsx";
import {MainAnalyse} from "./components/MainAnalyse.tsx";

export default function App() {

    return (
        < >
            <HeaderBar />
            <NavBar />
            <Routes>
                <Route path="/" element={<MainRecordentry />} />
                <Route path="/analyse" element={<MainAnalyse />} />
            </Routes>
        </>
    )
}
