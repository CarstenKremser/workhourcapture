import '../styles/style.css';
import '../styles/styles_analyse.css';
import {ReactElement} from 'react';
import {workingDayType} from "./WorkingTimeData.tsx";

export function addClassNegativeWhenDateIsLessThanZero(theDate: Date, classNames: string): string {
    if (theDate.toString().startsWith("-")) {
        return classNames + " negative";
    }
    return classNames;
}

export function WorkingDayCard(props: Readonly<workingDayType>): ReactElement {

    function renderDaySummary(): ReactElement {

        return <>
            <div className="day-data day-date">{props.date.toString()}</div>
            <div className="day-data day-allocated">{props.allocated.toString()}</div>
            <div className="day-data day-worked">{props.worked.toString()}</div>
            <div className={addClassNegativeWhenDateIsLessThanZero(
                props.difference, "day-data day-difference")
            }>{props.difference.toString()}</div>
        </>
    }

    function renderWorkingTimes(): ReactElement {
        const times: ReactElement[] = [];
        props.workingTimes.forEach(workingTime => {

            times.push(
                <div className={"interval"}>
                    <div className={"interval-data interval-starttime"}>{workingTime.startTime.toString()}</div>
                    <div className={"interval-data interval-endtime"}>{workingTime.endTime.toString()}</div>
                    <div className={"interval-data interval-worked"}>{workingTime.duration.toString()}</div>
                    {props.date !== workingTime.endDate && workingTime.endDate.toString() !== ""
                        ? <div className={"interval-data interval-enddate"}>{workingTime.endDate.toString()}</div>
                        : <></>}
                </div>
            );
        });
        return <>{times}</>;
    }

    return (<>
        <div className="day-card">
            {renderDaySummary()}
            <div className={"day-intervals"}>{renderWorkingTimes()}</div>
        </div>
    </>);
}