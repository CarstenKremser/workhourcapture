import '../style.css';
import {ReactElement} from 'react';

type WorktimeCardProps = {
    worktimeDate: Date,
    startId: string,
    endId: string,
    start: Date,
    end: Date,
    duration: number,
}

export function WorktimeCard(props: Readonly<WorktimeCardProps>): ReactElement {
    return (<>
        <div className="worktime-card">
            Datum: {props.worktimeDate.toString()}
            Anfangszeit: {props.start.toString()}
            Endzeit: {props.end.toString()}
            Dauer: {props.duration.toString()}
        </div>
    </>);
}