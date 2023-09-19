package de.lange.zeiterfassung.dataTransferObject;

public class TimesheetDto {

    private String arbeitszeit;
    private String pausenzeit;


    // Getter und Setter


    public String getArbeitszeit() {
        return arbeitszeit;
    }

    public void setArbeitszeit(String arbeitszeit) {
        this.arbeitszeit = arbeitszeit;
    }

    public String getPausenzeit() {
        return pausenzeit;
    }

    public void setPausenzeit(String pausenzeit) {
        this.pausenzeit = pausenzeit;
    }

}
