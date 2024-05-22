import '../style.css';
import {ReactElement} from 'react';
import {workingDayType} from "./WorkingTimeData.tsx";


export function WorkingDayCard(props: Readonly<workingDayType>): ReactElement {

    function renderWorkingTimes(): ReactElement {
        const times: ReactElement[] = [];
        props.workingTimes.forEach(workingTime => {

            times.push(
                <div className={"workingday-times"}>
                    <div>StartTime: {workingTime.startTime.toString()}</div>
                    <div>EndTime: {workingTime.endTime.toString()}</div>
                    <div>Duration: {workingTime.duration.toString()}</div>
                    <div>EndDate: {workingTime.endDate.toString()}</div>
                </div>
            );
        })
        return <>{times}</>
    }

    return <>
        <div className="workingday-card">
            <div className={"workingday-date"}>Date: {props.date.toString()}</div>
            <div className={"workingday-allocated"}>Allocated: {props.allocated.toString()}</div>
            <div className={"workingday-worked"}>Worked: {props.worked.toString()}</div>
            <div className={"workingday-times-list"}>{renderWorkingTimes()}</div>
        </div>
    </>;
}