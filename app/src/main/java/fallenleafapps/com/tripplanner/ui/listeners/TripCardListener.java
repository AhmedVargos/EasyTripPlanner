package fallenleafapps.com.tripplanner.ui.listeners;

import com.dd.morphingbutton.MorphingButton;

import fallenleafapps.com.tripplanner.models.TripModel;

/**
 * Created by Vargos on 20-Mar-18.
 */

public interface TripCardListener {

    void openTripDetails(TripModel trip);
    void deleteTrip(TripModel trip);
    void startTrip(TripModel trip, MorphingButton morphingButton) throws Exception;
}
