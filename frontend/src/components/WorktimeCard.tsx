import '../style.css';

type WorktimeCardProps = {
    worktimeDate: Date,
    startId: string,
    endId: string,
    start: Date,
    end: Date,
    duration: number,
}

export function WorktimeCard(props: Readonly<WorktimeCardProps>) {
    return (<>
            <div className="worktime-card">
                Datum: {props.worktimeDate}
                Anfangszeit: {props.start}
                Endzeit: {props.end}
                Dauer: {props.duration}
            </div>
        </>);
}