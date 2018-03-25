package fallenleafapps.com.tripplanner.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DR Gamal on 3/18/2018.
 */

public class TripModel implements Parcelable {
    private int tripId;
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

    public TripModel(int tripId, String tripName, Long tripDate, Long tripTime, String startLat, String startLang, String startLocationName, String endLat, String endLang, String endLocationName, boolean tripType, int tripStatus, List<NoteModel> notes) {
        this.tripId = tripId;
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

    protected TripModel(Parcel in) {
        tripId = in.readInt();
        tripName = in.readString();
        tripDate = in.readByte() == 0x00 ? null : in.readLong();
        tripTime = in.readByte() == 0x00 ? null : in.readLong();
        startLat = in.readString();
        startLang = in.readString();
        startLocationName = in.readString();
        endLat = in.readString();
        endLang = in.readString();
        endLocationName = in.readString();
        tripType = in.readByte() != 0x00;
        tripStatus = in.readInt();
        if (in.readByte() == 0x01) {
            notes = new ArrayList<NoteModel>();
            in.readList(notes, NoteModel.class.getClassLoader());
        } else {
            notes = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(tripId);
        dest.writeString(tripName);
        if (tripDate == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(tripDate);
        }
        if (tripTime == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(tripTime);
        }
        dest.writeString(startLat);
        dest.writeString(startLang);
        dest.writeString(startLocationName);
        dest.writeString(endLat);
        dest.writeString(endLang);
        dest.writeString(endLocationName);
        dest.writeByte((byte) (tripType ? 0x01 : 0x00));
        dest.writeInt(tripStatus);
        if (notes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(notes);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TripModel> CREATOR = new Parcelable.Creator<TripModel>() {
        @Override
        public TripModel createFromParcel(Parcel in) {
            return new TripModel(in);
        }

        @Override
        public TripModel[] newArray(int size) {
            return new TripModel[size];
        }
    };
}