import '../style.css';
import React, {useState} from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import '../datepickerstyle.css';

export type workingTime = {

}


export function MainAnalyse() {

    const [selectedMonth, setSelectedMonth] = useState<Date | null>(new Date());

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        console.log("Submit clicked");
    }

    const renderMonthContent = (_month: number, shortMonth: string, longMonth: string) => {
        const fullYear = new Date().getFullYear();
        const tooltipText = `Tooltip for month: ${longMonth} ${fullYear}`;

        return <span title={tooltipText}>{shortMonth}</span>;
    };

    return (<>
        <main className="main-analyse">
            <form onSubmit={handleSubmit}>
                <h2>Analyse/Report-Container</h2>
                <div className="analyse-select-container">
                    <div className="analyse-select-pickdate">
                        <label htmlFor="monthSelect">Monat auswählen: </label>
                        <DatePicker
                            id="analyse-monthSelect"
                            className="monthSelect"
                            selected={selectedMonth}
                            onChange={(date) => setSelectedMonth(date)}
                            renderMonthContent={renderMonthContent}
                            showMonthYearPicker
                            showIcon

                            dateFormat="MM.yyyy"
                        />
                    </div>
                </div>
                <div className="analyse-buttonsubmit">
                    <input type="submit" className="submit" value="aktualisieren"></input>

                </div>
            </form>
            <div className="analyse-display-container">
                <div className="analyse-display-worktimes">

                </div>
            </div>
        </main>
    </>)
}
