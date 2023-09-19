package de.lange.zeiterfassung.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "arbeitszeiten")
public class ArbeitszeitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long z_id;

    private LocalDateTime arbeitBeginn;
    private LocalDateTime arbeitEnde;

    private LocalDateTime pauseBeginn;
    private LocalDateTime pauseEnde;

    private LocalDate arbeitsDatum;
    private String gesamtArbeitszeit;  // HH:MM:SS Format
    private String gesamtPausenzeit;   // HH:MM:SS Format
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")  // Name der Spalte in der Datenbank, die den Fremdschlüssel darstellt
    private UsersModel user;


    // Getter und Setter für alle Felder.

    public Long getZ_id() {
        return z_id;
    }

    public void setZ_id(Long z_id) {
        this.z_id = z_id;
    }

    public LocalDateTime getArbeitBeginn() {
        return arbeitBeginn;
    }

    public void setArbeitBeginn(LocalDateTime arbeitBeginn) {
        this.arbeitBeginn = arbeitBeginn;
    }

    public LocalDateTime getArbeitEnde() {
        return arbeitEnde;
    }

    public void setArbeitEnde(LocalDateTime arbeitEnde) {
        this.arbeitEnde = arbeitEnde;
    }

    public LocalDateTime getPauseBeginn() {
        return pauseBeginn;
    }

    public void setPauseBeginn(LocalDateTime pauseBeginn) {
        this.pauseBeginn = pauseBeginn;
    }

    public LocalDateTime getPauseEnde() {
        return pauseEnde;
    }

    public void setPauseEnde(LocalDateTime pauseEnde) {
        this.pauseEnde = pauseEnde;
    }

    public LocalDate getArbeitsDatum() {
        return arbeitsDatum;
    }

    public void setArbeitsDatum(LocalDate arbeitsDatum) {
        this.arbeitsDatum = arbeitsDatum;
    }

    public String getGesamtArbeitszeit() {
        return gesamtArbeitszeit;
    }

    public void setGesamtArbeitszeit(String gesamtArbeitszeit) {
        this.gesamtArbeitszeit = gesamtArbeitszeit;
    }

    public String getGesamtPausenzeit() {
        return gesamtPausenzeit;
    }

    public void setGesamtPausenzeit(String gesamtPausenzeit) {
        this.gesamtPausenzeit = gesamtPausenzeit;
    }

    public UsersModel getUser() {
        return user;
    }

    public void setUser(UsersModel user) {
        this.user = user;
    }
}
