package fallenleafapps.com.tripplanner.ui.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.ui.activities.TripDialogActivity;
import fallenleafapps.com.tripplanner.utils.CustomTripDialog;
import fallenleafapps.com.tripplanner.utils.FeedBackActionsListeners;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class TripIntentService extends IntentService {
    private static final String LOG_TAG = "Trip Service";
    public TripIntentService() {
        super("TripIntentService");
       }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            String tripName = intent.getStringExtra("TRIP_NAME");
            Intent intentDialog = new Intent(this, TripDialogActivity.class);
            intentDialog.putExtra("TRIP_NAME",tripName);
            intentDialog.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intentDialog);
        }
    }

}
