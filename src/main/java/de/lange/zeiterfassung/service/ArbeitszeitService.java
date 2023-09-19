package de.lange.zeiterfassung.service;

import de.lange.zeiterfassung.dataTransferObject.TimesheetDto;
import de.lange.zeiterfassung.model.ArbeitszeitEntity;
import de.lange.zeiterfassung.repository.ArbeitszeitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ArbeitszeitService {

    @Autowired
    private ArbeitszeitRepository arbeitszeitRepository;

    public ArbeitszeitEntity startArbeit(Long id) {
        ArbeitszeitEntity arbeitszeit = arbeitszeitRepository.findByUserId(id).orElse(new ArbeitszeitEntity());
        arbeitszeit.setZ_id(id);
        arbeitszeit.setArbeitsDatum(LocalDate.now()); // Setzen des heutigen Datums
        arbeitszeit.setArbeitBeginn(LocalDateTime.now());
        return arbeitszeitRepository.save(arbeitszeit);
    }

    public ArbeitszeitEntity endeArbeit(Long id) {
        ArbeitszeitEntity arbeitszeit = arbeitszeitRepository.findByUserId(id).orElse(null);
        if (arbeitszeit != null) {
            arbeitszeit.setArbeitEnde(LocalDateTime.now());

            Duration gesamtArbeitsDuration = Duration.between(arbeitszeit.getArbeitBeginn(), arbeitszeit.getArbeitEnde());

            Duration pauseDuration = Duration.between(arbeitszeit.getPauseBeginn(), arbeitszeit.getPauseEnde());

            // Arbeitszeit minus Pausenzeit
            Duration reineArbeitsDuration = gesamtArbeitsDuration.minus(pauseDuration);

            String arbeitszeitMenschenlesbar = formatDuration(reineArbeitsDuration.toString());
            arbeitszeit.setGesamtArbeitszeit(arbeitszeitMenschenlesbar);

            String pausenzeitMenschenlesbar = formatDuration(pauseDuration.toString());
            arbeitszeit.setGesamtPausenzeit(pausenzeitMenschenlesbar);

            return arbeitszeitRepository.save(arbeitszeit);
        }
        return null;
    }


    public ArbeitszeitEntity startPause(Long id) {
        ArbeitszeitEntity arbeitszeit = arbeitszeitRepository.findByUserId(id).orElse(null);
        if (arbeitszeit != null) {
            arbeitszeit.setPauseBeginn(LocalDateTime.now());
            return arbeitszeitRepository.save(arbeitszeit);
        }
        return null;
    }

    public ArbeitszeitEntity endePause(Long id) {
        ArbeitszeitEntity arbeitszeit = arbeitszeitRepository.findByUserId(id).orElse(null);
        if (arbeitszeit != null) {
            arbeitszeit.setPauseEnde(LocalDateTime.now());
            return arbeitszeitRepository.save(arbeitszeit);
        }
        return null;
    }
    public ArbeitszeitEntity saveArbeitszeitForUser(TimesheetDto timesheetDto) {
        ArbeitszeitEntity arbeitszeit = new ArbeitszeitEntity();
        arbeitszeit.setArbeitsDatum(LocalDate.now());
        arbeitszeit.setGesamtArbeitszeit(timesheetDto.getArbeitszeit());  // Setzen als String
        arbeitszeit.setGesamtPausenzeit(timesheetDto.getPausenzeit());   // Setzen als String
        return arbeitszeitRepository.save(arbeitszeit);
    }


/*    private String berechneZeitdauer(LocalDateTime start, LocalDateTime ende) {
        Duration duration = Duration.between(start, ende);
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        long seconds = duration.minusHours(hours).minusMinutes(minutes).getSeconds();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }*/

    public String formatDuration(String isoDuration) {
        Duration duration = Duration.parse(isoDuration);
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        double seconds = duration.minusHours(hours).minusMinutes(minutes).getSeconds() +
                duration.minusHours(hours).minusMinutes(minutes).getNano() / 1_000_000_000.0;

        return String.format("%02d:%02d:%02.3f", hours, minutes, seconds);
    }

}
