import '../styles/style.css';
import '../styles/styles_analyse.css';
import {ReactElement, useEffect, useState} from "react";
import axios, {AxiosResponse} from "axios";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import '../styles/datepickerstyle.css';
import {workingDaysType} from "./WorkingTimeData.tsx";
import {WorkingDaysCard} from "./WorkingDaysCard.tsx";
import {User} from "./UserData.ts";
import {LoginAdviceCard} from "./LoginAdviceCard.tsx";

export type MainAnalyseProps = {
    user: User | null;
}

export function MainAnalyse(props: MainAnalyseProps): ReactElement {

    const [selectedMonth, setSelectedMonth] = useState<Date | null>(new Date());
    const [workingDays, setWorkingDays] = useState<workingDaysType | null>(null);

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        console.log("Submit clicked");
        updateMonthReport();
    }

    function updateMonthReport(){
        if (selectedMonth) {
            const year = selectedMonth.getFullYear() as number;
            const month = selectedMonth?.getMonth() + 1 as number;
            axios({
                    method: "post",
                    url: "/api/workingtime/formonth",
                    data: {
                        userId: props.user?.userid,
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

    useEffect(() => {updateMonthReport();}, [selectedMonth]);

    const renderMonthContent = (_month: number, shortMonth: string, longMonth: string) => {
        const fullYear = new Date().getFullYear();
        const tooltipText = `Tooltip for month: ${longMonth} ${fullYear}`;

        return <span title={tooltipText}>{shortMonth}</span>;
    };

    function mainAnalyse(): ReactElement {
        return <>
            <main className="main-analyse">
                <h2>Analyse/Report</h2>
                <form onSubmit={handleSubmit}>
                    <div className="analyse-select-container">
                        <div className="analyse-select-pickdate">
                            <label htmlFor="monthSelect">Monat auswählen: </label>
                            <DatePicker
                                id="analyse-monthSelect"
                                className="monthSelect"
                                selected={selectedMonth}
                                onChange={(date) => {
                                    setSelectedMonth(date);
                                }}
                                renderMonthContent={renderMonthContent}
                                showMonthYearPicker
                                showIcon
                                dateFormat="MM.yyyy"
                            />
                        </div>
                        <div className="analyse-buttonsubmit">
                            <input type="submit" className="submit" value="aktualisieren"></input>
                        </div>
                    </div>
                </form>
                <div className="analyse-display-container">
                    {workingDays
                        ? <WorkingDaysCard
                            allocated={workingDays.allocated}
                            worked={workingDays.worked}
                            difference={workingDays.difference}
                            workingDays={workingDays.workingDays}/>
                        : <></>
                    }
                </div>
            </main>
        </>
    }

    return (<>
        {(props.user !== undefined && props.user !== null)
            ? mainAnalyse()
            : <LoginAdviceCard />
        }
    </>)
}
