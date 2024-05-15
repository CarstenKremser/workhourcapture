import '../style.css';
import React, {useState} from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import '../datepickerstyle.css';

export function MainAnalyse() {
    //require('react-month-picker-input/dist/react-month-picker-input.css');

    const [selectedMonth, setSelectedMonth] = useState<Date|null>(new Date());
    //const [startDate, setStartDate] = useState<Date|null>(new Date());

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
                <div>
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
                <div className="analyse-buttonsubmit">
                    <input type="submit" className="submit" value="aktualisieren"></input>

                </div>
            </form>
        </main>
    </>)
}
