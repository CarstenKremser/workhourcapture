import '../styles/style.css';
import React, {ReactElement, useEffect, useState} from "react";
import axios from "axios";
import {User} from "./UserData.ts";
import {LoginAdviceCard} from "./LoginAdviceCard.tsx";

function DateTimeNow() {
    const nowWithOutTimezone = new Date();
    const now = new Date(Date.now() - nowWithOutTimezone.getTimezoneOffset() * 60000);
    now.toISOString();
    console.log("now is " + now.toISOString());
    return now.toISOString().substring(0, 16);
}

function TimeWithoutTimezoneOffset(timeStringWithOffset: string) {
    const time = new Date(timeStringWithOffset);
    return time.toISOString();
}

function TimezoneOffset() {
    const now = new Date();
    return now.getTimezoneOffset();
}

function TimezoneName() {
    return Intl.DateTimeFormat().resolvedOptions().timeZone;
}

function RecordTypeToString(recordType: string) {
    switch (recordType) {
        case "workstart":
            return "Arbeitsbeginn";
        case "workend":
            return "Arbeitsende";
        case "breakstart":
            return "Pausenbeginn";
        case "breakend":
            return "Pausenende";
        default:
            return "";
    }
}

export type MainRecordentryProps = {
    user: User | null;
}

export function MainRecordentry(props: MainRecordentryProps): ReactElement {

    const [sugestedRecordType, setSugestedRecordType] = useState("workstart");
    const [entryMethod, setEntryMethod] = useState("instant");
    const [recordTime, setRecordTime] = useState<string>(DateTimeNow());
    const [recordType, setRecordType] = useState("workstart")

    useEffect(() => {
        setSugestedRecordType("workstart");
    }, []);

    const onChangeEntryMethod = (event: React.ChangeEvent<HTMLInputElement>) => {
        setEntryMethod(event.target.value);
    }

    const onChangeRecordTime = (event: React.ChangeEvent<HTMLInputElement>) => {
        setRecordTime(event.target.value);
        setEntryMethod("edited");
    }

    const onChangeRecordType = (event: React.ChangeEvent<HTMLSelectElement>) => {
        setRecordType(event.target.value);
        setEntryMethod("edited");
    }

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        console.log("Entry Method " + entryMethod);
        if (entryMethod === "instant") {
            axios.post("/api/timerecord/add", {
                "userId": props.user?.userid,
                "recordType": sugestedRecordType,
                "timezoneOffset": TimezoneOffset(),
                "timezoneName": TimezoneName(),
            }).then((response) => {
                console.log(response);
                alert("Buchung erfolgreich");
            }).catch((error) => {
                console.log(error);
                alert("Buchungsfehler: " + error.message);
            })
        } else if (entryMethod === "edited") {
            axios.post("/api/timerecord/add", {
                "userId": props.user?.userid,
                "recordType": recordType,
                "recordTimestamp": TimeWithoutTimezoneOffset(recordTime),
                "timezoneOffset": TimezoneOffset(),
                "timezoneName": TimezoneName(),
            }).then((response) => {
                console.log(response);
                alert("Buchung erfolgreich");
            }).catch((error) => {
                console.log(error);
                alert("Buchungsfehler: " + error.message);
            })
        } else {
            alert("Error: Unknown Entrytype " + entryMethod);
        }
    };

    function recordEntryForm(): ReactElement {
        return <>
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
                               onChange={onChangeEntryMethod}/>
                        <label htmlFor="recordtime-instant"
                               id="recordtime-instant-label">{RecordTypeToString(sugestedRecordType)} jetzt</label>
                    </div>

                    <div className="recordtime-radiocontainer">
                        <input className="recordtime-radio"
                               type="radio"
                               id="recordtime-edited"
                               name="recordtime-edited"
                               value="edited"
                               checked={entryMethod === "edited"}
                               onChange={onChangeEntryMethod}/>
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
                                   onChange={onChangeRecordTime}/>
                        </div>
                    </div>

                    <div className="recordtime-buttonsubmit">
                        <input type="submit" value="buchen"></input>
                    </div>
                </fieldset>
            </form>
        </>
    }

    return (<>
        <main className="main-recordentry">
            {(props.user !== undefined && props.user !== null)
                ? recordEntryForm()
                : <LoginAdviceCard />
            }
        </main>
    </>);
}

