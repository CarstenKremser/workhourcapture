import '../style.css'

export function MainRecordentry() {
    return (<>
        <main className="main-recordentry">
            <fieldset className="recordtime-editfieldset">
                <legend>Zeit buchen:</legend>

                <div className="recordtime-radiocontainer">
                    <input className="recordtime-radio"
                           type="radio"
                           id="recordtime-instant"
                           name="recordtime-instant"
                           value="instant"
                           checked/>
                    <label htmlFor="recordtime-instant" id="recordtime-instant-label">jetzt</label>
                </div>

                <div className="recordtime-radiocontainer">
                    <input className="recordtime-radio"
                           type="radio"
                           id="recordtime-edited"
                           name="recordtime-edited"
                           value="edited"/>
                    <label htmlFor="recordtime-edited">manuell:</label>
                    <select name="recordtime-bookingtype" id="recordtime-select">
                        <option value="workstart">Arbeitsbeginn</option>
                        <option value="workend">Arbeitsende</option>
                        <option value="breakstart">Pausenbeginn</option>
                        <option value="breakend">Pausenende</option>
                    </select>
                    <div className="recordtime-edittime">
                        <label htmlFor="recordtime-date">Datum/Uhrzeit:</label>
                        <input type="datetime-local" id="recordtime-date" name="recordtime-date"
                               value="2024-05-07T12:00"
                               min="2024-05-01T00:00"
                               max="2024-06-01T00:00"/>
                    </div>
                </div>

                <div className="recordtime-buttonsubmit">
                    <input type="submit" value="buchen"></input>
                </div>
            </fieldset>

        </main>
    </>);
}
