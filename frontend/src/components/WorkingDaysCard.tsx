import '../style.css';
import {workingDaysType} from "./MainAnalyse.tsx";

export function WorkingDaysCard(props: Readonly<workingDaysType>) {

    function renderAggregatedData() {
        if (props) {
            const allcocatedTime = props.allocated.toString();
            const workedTime = props.worked.toString();
            return (<>
                <div>Allocated: {allcocatedTime}</div>
                <div>Worked: {workedTime}</div>
            </>)
        }
        return (<></>);
    }

    return (<>
        {renderAggregatedData()}
    </>);
}