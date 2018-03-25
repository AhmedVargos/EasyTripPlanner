package fallenleafapps.com.tripplanner.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DR Gamal on 3/18/2018.
 */

public class TripModel {
    private String tripName;
    private Long tripDate;
    private Long tripTime;
    private String startLat;
    private String startLang;
    private String startLocationName;
    private String endLat;
    private String endLang;
    private String endLocationName;
    private boolean tripType;
    private int tripStatus;
    private List<NoteModel> notes = new ArrayList();

    public TripModel() {
    }

    public TripModel(String tripName, Long tripDate, Long tripTime, String startLocationName, String endLocationName, boolean tripType, int tripStatus) {
        this.tripName = tripName;
        this.tripDate = tripDate;
        this.tripTime = tripTime;
        this.startLocationName = startLocationName;
        this.endLocationName = endLocationName;
        this.tripType = tripType;
        this.tripStatus = tripStatus;
    }

    public TripModel(String tripName, Long tripDate, Long tripTime, String startLat, String startLang, String startLocationName, String endLat, String endLang, String endLocationName, boolean tripType, int tripStatus, List<NoteModel> notes) {
        this.tripName = tripName;
        this.tripDate = tripDate;
        this.tripTime = tripTime;
        this.startLat = startLat;
        this.startLang = startLang;
        this.startLocationName = startLocationName;
        this.endLat = endLat;
        this.endLang = endLang;
        this.endLocationName = endLocationName;
        this.tripType = tripType;
        this.tripStatus = tripStatus;
        this.notes = notes;
    }
    public void addNote(NoteModel noteModel){
        notes.add(noteModel);
    }

    public void removeNote(NoteModel noteModel){
        notes.remove(noteModel);
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public Long getTripDate() {
        return tripDate;
    }

    public void setTripDate(Long tripDate) {
        this.tripDate = tripDate;
    }

    public Long getTripTime() {
        return tripTime;
    }

    public void setTripTime(Long tripTime) {
        this.tripTime = tripTime;
    }

    public String getStartLat() {
        return startLat;
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getStartLang() {
        return startLang;
    }

    public void setStartLang(String startLang) {
        this.startLang = startLang;
    }

    public String getStartLocationName() {
        return startLocationName;
    }

    public void setStartLocationName(String startLocationName) {
        this.startLocationName = startLocationName;
    }

    public String getEndLat() {
        return endLat;
    }

    public void setEndLat(String endLat) {
        this.endLat = endLat;
    }

    public String getEndLang() {
        return endLang;
    }

    public void setEndLang(String endLang) {
        this.endLang = endLang;
    }

    public String getEndLocationName() {
        return endLocationName;
    }

    public void setEndLocationName(String endLocationName) {
        this.endLocationName = endLocationName;
    }

    public boolean isTripType() {
        return tripType;
    }

    public void setTripType(boolean tripType) {
        this.tripType = tripType;
    }

    public int getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(int tripStatus) {
        this.tripStatus = tripStatus;
    }

    public List<NoteModel> getNotes() {
        return notes;
    }

    public void setNotes(List<NoteModel> notes) {
        this.notes = notes;
    }
}
