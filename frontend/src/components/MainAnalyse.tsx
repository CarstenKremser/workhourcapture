import '../styles/style.css';
import '../styles/styles_analyse.css';
import React, {ReactElement, useState} from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import '../styles/datepickerstyle.css';
import axios, {AxiosResponse} from "axios";
import {workingDaysType} from "./WorkingTimeData.tsx";
import {WorkingDaysCard} from "./WorkingDaysCard.tsx";


export function MainAnalyse(): ReactElement {

    const [selectedMonth, setSelectedMonth] = useState<Date | null>(new Date());
    const [workingDays, setWorkingDays] = useState<workingDaysType | null>(null);

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        console.log("Submit clicked");
        if (selectedMonth) {
            const year = selectedMonth.getFullYear() as number;
            const month = selectedMonth?.getMonth() + 1 as number;
            axios({
                    method: "post",
                    url: "/api/workingtime/formonth",
                    data: {
                        userId: 'defaultUser',
                        year: year,
                        month: month
                    }
                }
            ).then((response) => {
                console.log(response);
                setWorkingDays((response as AxiosResponse).data);
            }).catch((error) => {
                console.log(error);
                alert("Error: " + error.message);
            });
        }
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
                    {workingDays
                        ? <WorkingDaysCard
                            allocated={workingDays.allocated}
                            worked={workingDays.worked}
                            workingDays={workingDays.workingDays} />
                        : <></>
                    }
                </div>
            </div>
        </main>
    </>)
}
