class TimerService {
    static arbeitszeitCounter = 0;
    static pausenzeitCounter = 0;
    static arbeitszeitTimer;
    static pausenzeitTimer;
    static inPause = false;
    static inArbeit = false;

    static initializeDate() {
        const currentDate = new Date();
        const formattedDate = `Datum: ${currentDate.getDate().toString().padStart(2, '0')}.${(currentDate.getMonth() + 1).toString().padStart(2, '0')}.${currentDate.getFullYear()}`;
        document.getElementById("dateDisplay").textContent = formattedDate;
    }

    static toggleArbeit() {
        if (TimerService.inPause) {
            TimerService.setArbeitButtonState(true, "Feierabend innerhalb einer Pause?");
            return;
        }
        if (TimerService.inArbeit) {
            TimerService.endeArbeit();
        } else {
            TimerService.startArbeit();
        }
    }

    static setArbeitButtonState(isDisabled, titleText) {
        const arbeitButton = document.getElementById("arbeitButton");
        arbeitButton.disabled = isDisabled;
        if (isDisabled && titleText) {
            arbeitButton.title = titleText;
        } else {
            arbeitButton.removeAttribute('title');
        }
    }

    static startArbeit() {
        clearInterval(TimerService.pausenzeitTimer);
        TimerService.arbeitszeitTimer = TimerService.startTimer("arbeitszeit", TimerService.arbeitszeitCounter);
        document.getElementById("arbeitButton").textContent = "Feierabend!";
        TimerService.inArbeit = true;
        TimerService.setPauseButtonState(false);
        TimerService.setResetButtonState(true);
        TimerService.setArbeitButtonState(false);

        TimerService.startZeitArbeit = new Date().toISOString();
        const userId = localStorage.getItem('userId');
        fetch('/api/arbeitszeit/startArbeit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({id: userId})
        }).catch(error => console.error('Fehler beim Starten der Arbeit:', error));
    }

    static endeArbeit() {
        clearInterval(TimerService.arbeitszeitTimer);
        document.getElementById("arbeitButton").textContent = "Arbeit aufnehmen";
        TimerService.inArbeit = false;
        TimerService.setPauseButtonState(true);
        TimerService.setResetButtonState(false);
        TimerService.setArbeitButtonState(false);

        TimerService.endeZeitArbeit = new Date().toISOString();
        const arbeitszeitString = TimerService.formatTime(TimerService.arbeitszeitCounter);
        const pausenzeitString = TimerService.formatTime(TimerService.pausenzeitCounter);

        // Daten für den POST-Request
        const data = {
            userId: localStorage.getItem('userId'),
            endeZeitArbeit: TimerService.endeZeitArbeit,
            arbeitszeit: arbeitszeitString,
            pausenzeit: pausenzeitString
        };

        // POST-Request an den Server senden
        fetch('/api/arbeitszeit/endeArbeit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(data => {
                console.log('Erfolg beim Beenden der Arbeit:', data);
            })
            .catch(error => {
                console.error('Fehler beim Beenden der Arbeit:', error);
            });
    }


    static togglePause() {
        if (TimerService.inPause) {
            TimerService.endePause();
            const userId = localStorage.getItem('userId');
            fetch('/api/arbeitszeit/endePause', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({userId: userId})
            }).catch(error => console.error('Fehler beim Beenden der Pause:', error));
        } else {
            TimerService.startPause();
            const userId = localStorage.getItem('userId');
            fetch('/api/arbeitszeit/startPause', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({userId: userId})
            }).catch(error => console.error('Fehler beim Starten der Pause:', error));
            console.log('arbeitszeitString:', arbeitszeitString);
            console.log('pausenzeitString:', pausenzeitString);
        }
    }

    static startPause() {
        clearInterval(TimerService.arbeitszeitTimer);
        TimerService.pausenzeitTimer = TimerService.startTimer("pausenzeit", TimerService.pausenzeitCounter);
        document.getElementById("pauseButton").textContent = "Pause beenden";
        TimerService.inPause = true;
        TimerService.setResetButtonState(true);
        TimerService.setArbeitButtonState(true, "Feierabend machen innerhalb einer Pause ist nicht zulässig.");

        const userId = localStorage.getItem('user');
        const starPauseTimestamp = new Date().toISOString();  // Aktuellen Zeitstempel erfassen

        fetch('/api/arbeitszeit/startPause', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: userId,
                starPause: starPauseTimestamp
            })
        }).catch(error => console.error('Fehler beim Starten der Pause:', error));
    }


    static endePause() {
        clearInterval(TimerService.pausenzeitTimer);
        TimerService.arbeitszeitTimer = TimerService.startTimer("arbeitszeit", TimerService.arbeitszeitCounter);
        document.getElementById("pauseButton").textContent = "Pause starten";
        TimerService.inPause = false;
        TimerService.setArbeitButtonState(false);

        const userId = localStorage.getItem('userId');
        const endePauseTimestamp = new Date().toISOString();

        fetch('/api/arbeitszeit/endePause', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: userId,
                endePause: endePauseTimestamp
            })
        }).catch(error => console.error('Fehler beim Beenden der Pause:', error));
    }


    static startTimer(elementId, counter) {
        const element = document.getElementById(elementId);
        return setInterval(() => {
            counter++;
            element.textContent = TimerService.formatTime(counter);
            if (elementId === "arbeitszeit") {
                TimerService.arbeitszeitCounter = counter;
            } else {
                TimerService.pausenzeitCounter = counter;
            }
        }, 1000);
    }

    static formatTime(seconds) {
        const h = Math.floor(seconds / 3600).toString().padStart(2, '0');
        const m = Math.floor((seconds % 3600) / 60).toString().padStart(2, '0');
        const s = (seconds % 60).toString().padStart(2, '0');
        return `${h}:${m}:${s}`;
    }

    static resetTimers() {
        TimerService.arbeitszeitCounter = 0;
        TimerService.pausenzeitCounter = 0;
        document.getElementById("arbeitszeit").textContent = "00:00:00";
        document.getElementById("pausenzeit").textContent = "00:00:00";
        clearInterval(TimerService.arbeitszeitTimer);
        clearInterval(TimerService.pausenzeitTimer);
        TimerService.arbeitszeitTimer = null;
        TimerService.pausenzeitTimer = null;
        TimerService.setPauseButtonState(false);
        TimerService.setResetButtonState(false);
    }

    static setPauseButtonState(isDisabled) {
        const pauseButton = document.getElementById("pauseButton");
        pauseButton.disabled = isDisabled;
        if (isDisabled) {
            pauseButton.title = "Sie haben Feierabend! Pausen sind dort nicht möglich. Nur wer arbeitet kann Pause machen.";
        } else {
            pauseButton.removeAttribute('title');
        }
    }

    static setResetButtonState(isDisabled) {
        const resetButton = document.getElementById("resetButton");
        resetButton.disabled = isDisabled;
        if (isDisabled) {
            resetButton.title = "Arbeitszeiterfassung läuft... Reset ist erst nach Beendigung möglich.";
        } else {
            resetButton.removeAttribute('title');
        }
    }

    /* static saveTimesToServer() {
         const arbeitszeitString = TimerService.formatTime(TimerService.arbeitszeitCounter);
         const pausenzeitString = TimerService.formatTime(TimerService.pausenzeitCounter);

         const timesheet = {
             arbeitszeit: arbeitszeitString,
             pausenzeit: pausenzeitString
         };

         fetch('/api/timesheet', {
             method: 'POST',
             headers: {
                 'Content-Type': 'application/json'
             },
             body: JSON.stringify(timesheet)
         }).then(response => {
             if (response.ok) {
                 return response.json();
             }
             throw new Error('Fehler beim Speichern der Zeiten.');
         }).then(data => {
             console.log('Arbeitszeiten erfolgreich gespeichert:', data);
         }).catch(error => {
             console.error('Fehler:', error);
         });
     }*/
}

document.addEventListener("DOMContentLoaded", () => {
    TimerService.initializeDate();
});
