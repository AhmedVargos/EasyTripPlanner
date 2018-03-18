package fallenleafapps.com.tripplanner.models;

/**
 * Created by DR Gamal on 3/18/2018.
 */

public class TripClassModel {
   private String tripName;
   private Long tripDate;
   private Long  tripTime;
   private String startLocation;
   private String endLocation;
   private boolean tripType;
   private int tripStatus;

    public TripClassModel() {
    }
    public TripClassModel(String tripName, Long tripDate, Long tripTime, String startLocation, String endLocation, boolean tripType, int tripStatus) {
        this.tripName = tripName;
        this.tripDate = tripDate;
        this.tripTime = tripTime;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.tripType = tripType;
        this.tripStatus = tripStatus;
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

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
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
}
