import '../styles/style.css';
import '../styles/styles_analyse.css';
import {ReactElement} from "react";
import {workingDaysType} from "./WorkingTimeData.tsx";
import {WorkingDayCard, addClassNegativeWhenDateIsLessThanZero} from "./WorkingDayCard.tsx";

export function WorkingDaysCard(props: Readonly<workingDaysType>): ReactElement {

    function renderAggregatedData(): ReactElement {
        const allocatedTime = props.allocated.toString();
        const workedTime = props.worked.toString();
        const differenceTime = props.difference.toString();
        return <>
            <div className="period-summary">
                <div className="period-label label-date">Datum</div>
                <div className="period-label label-allocated">Soll</div>
                <div className="period-label label-worked">Ist</div>
                <div className="period-label label-difference">Differenz</div>
                <hr className="period-seperator"/>
                <div className="period-data period-allocated">{allocatedTime}</div>
                <div className="period-data period-worked">{workedTime}</div>
                <div className={addClassNegativeWhenDateIsLessThanZero(
                    props.difference, "period-data period-difference"
                )}>{differenceTime}</div>
            </div>
        </>
    }

    function renderWorkingDaysCards(): ReactElement {
        const cards: ReactElement[] = [];
        props.workingDays.forEach(workingDay => {
            cards.push(<WorkingDayCard
                key={workingDay.date.toString()}
                date={workingDay.date}
                allocated={workingDay.allocated}
                worked={workingDay.worked}
                difference={workingDay.difference}
                workingTimes={workingDay.workingTimes}
            />);
        });
        return <>
            <div className="period-days">
                {cards}
            </div>
        </>;
    }

    return <>
        {renderAggregatedData()}
        {renderWorkingDaysCards()}
    </>
}