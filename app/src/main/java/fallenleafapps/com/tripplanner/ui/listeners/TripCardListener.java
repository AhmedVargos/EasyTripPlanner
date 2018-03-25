package fallenleafapps.com.tripplanner.ui.listeners;

import com.dd.morphingbutton.MorphingButton;

import fallenleafapps.com.tripplanner.models.TripClassModel;

/**
 * Created by Vargos on 20-Mar-18.
 */

public interface TripCardListener {

    void openTripDetails(TripClassModel trip);
    void deleteTrip(TripClassModel trip);
    void startTrip(TripClassModel trip, MorphingButton morphingButton) throws Exception;
}