import '../style.css';
import {ReactElement} from "react";
import {workingDaysType} from "./WorkingTimeData.tsx";
import {WorkingDayCard} from "./WorkingDayCard.tsx";

export function WorkingDaysCard(props: Readonly<workingDaysType>): ReactElement {

    function renderAggregatedData(): ReactElement {
        const allocatedTime = props.allocated.toString();
        const workedTime = props.worked.toString();
        return (<>
            <div>Allocated: {allocatedTime}</div>
            <div>Worked: {workedTime}</div>
        </>)
    }

    function renderWorkingDaysCards(): ReactElement {
        const cards: ReactElement[] = [];
        props.workingDays.forEach(workingDay => {
            cards.push(<WorkingDayCard
                key={workingDay.date.toString()}
                date={workingDay.date}
                allocated={workingDay.allocated}
                worked={workingDay.worked}
                workingTimes={workingDay.workingTimes}
            />);
        });
        return <>{cards}</>;
    }

    return <>
        {renderAggregatedData()}
        {renderWorkingDaysCards()}
    </>;
}