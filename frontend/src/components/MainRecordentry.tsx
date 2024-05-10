import '../style.css'
import {useEffect, useState} from "react";
import axios from "axios";

function DateTimeNow() {
    const nowWithOutTimezone = new Date();
    const now = new Date(Date.now() - nowWithOutTimezone.getTimezoneOffset() * 60000);
    now.toISOString();
    console.log("now is " + now.toISOString());
    return now.toISOString().substring(0, 16);
}

function RecordTypeToString(recordType: string) {
    switch (recordType) {
        case "workstart": return "Arbeitsbeginn";
        case "workend": return "Arbeitsende";
        case "breakstart": return "Pausenbeginn";
        case "breakend": return "Pausenende";
        default: return "";
    }
}

export function MainRecordentry() {

    const [sugestedRecordType, setSugestedRecordType] = useState("workstart");
    const [entryMethod, setEntryMethod] = useState("instant");
    const [recordTime, setRecordTime] = useState(DateTimeNow());
    const [recordType, setRecordType] = useState("workstart")

    useEffect(() => {
        setSugestedRecordType("workstart");
    }, []);

    const onChangeEntryMethod = (event) => {
        setEntryMethod(event.target.value);
    }

    const onChangeRecordTime = (event) => {
        setRecordTime(event.target.value);
    }

    const onChangeRecordType = (event) => {
        setRecordType(event.target.value);
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        if (entryMethod === "instant") {
            alert("Submitting instant booking");
            axios.post("/api/timerecord/addnow/defaultUser",{
                "userid": "defaultUser",
                "recordType": recordType,
            }).then((response) => {
                console.log(response);
            }).catch((error)=> {
                console.log(error);
            })
        } else if (entryMethod === "edited") {
            alert("Submitting manual booking," + "\n" +
                "RecordTime is " + recordTime + "\n" +
                "RecordType is " + recordType + "\n" +
                "Now is " + DateTimeNow());
        } else {
            alert("Error: Unknown Entrytype " + entryMethod);
        }
    };

    return (<>
        <main className="main-recordentry">
            <form onSubmit={handleSubmit}>

                <fieldset className="recordtime-editfieldset">
                    <legend>Zeit buchen:</legend>

                    <div className="recordtime-radiocontainer">
                        <input className="recordtime-radio"
                               type="radio"
                               id="recordtime-instant"
                               name="recordtime-instant"
                               value="instant"
                               checked={entryMethod === "instant"}
                               onChange={onChangeEntryMethod}
                        / >
                        <label htmlFor="recordtime-instant" id="recordtime-instant-label">{RecordTypeToString(sugestedRecordType)} jetzt</label>
                    </div>

                    <div className="recordtime-radiocontainer">
                        <input className="recordtime-radio"
                               type="radio"
                               id="recordtime-edited"
                               name="recordtime-edited"
                               value="edited"
                               checked={entryMethod === "edited"}
                               onChange={onChangeEntryMethod}
                        / >
                        <label htmlFor="recordtime-edited">manuell:</label>
                        <select name="recordtime-bookingtype" id="recordtime-select"
                        onChange={onChangeRecordType}>
                            <option value="workstart">Arbeitsbeginn</option>
                            <option value="workend">Arbeitsende</option>
                        </select>
                        <div className="recordtime-edittime">
                            <label htmlFor="recordtime-date">Datum/Uhrzeit:</label>
                            <input type="datetime-local" id="recordtime-date" name="recordtime-date"
                                   value={recordTime}
                                   min="2024-05-01T00:00"
                                   max="2024-06-01T00:00"
                                   onChange={onChangeRecordTime}
                            />
                        </div>
                    </div>

                    <div className="recordtime-buttonsubmit">
                        <input type="submit" value="buchen"></input>
                    </div>
                </fieldset>
            </form>
        </main>
    </>);
}

