package fallenleafapps.com.tripplanner.ui.activities;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import fallenleafapps.com.tripplanner.ui.services.TripIntentService;
import fallenleafapps.com.tripplanner.utils.FeedBackActionsListeners;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.utils.CustomTripDialog;

public class TripDialogActivity extends AppCompatActivity {
    private static final String LOG_TAG = "HOME FRAGMENT";
    public static final int NOTIFICATION_ID = 201;
    NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_dialog);
        mNotificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        final String tripName = getIntent().getStringExtra("TRIP_NAME");

        //TODO handel the change of the trip status when start or cancel is pressed
        //Start the dialog for the trip
        CustomTripDialog mDialog = new CustomTripDialog(TripDialogActivity.this)
                .setBackgroundColor(R.color.colorPrimary)
                .setIcon(R.drawable.ic_navigation_black_24dp)
                .setIconColor(R.color.sampleColor)
                .setTitle(getString(R.string.str_start_trip) + " " +tripName)
                .setDescription(R.string.str_trip_desc)
                .setReviewQuestion(R.string.str_start_question)
                .setPositiveFeedbackText(R.string.str_yes)
                .setNegativeFeedbackText(R.string.str_no)
                .setAmbiguityFeedbackText(R.string.str_not_now)
                .setOnReviewClickListener(new FeedBackActionsListeners() {
                    @Override
                    public void onPositiveFeedback(CustomTripDialog dialog) {
                        Log.d(LOG_TAG,"positive feedback callback");
                        mNotificationManager.cancel(NOTIFICATION_ID);
                        dialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onNegativeFeedback(CustomTripDialog dialog) {
                        Log.d(LOG_TAG,"negative feedback callback");
                        mNotificationManager.cancel(NOTIFICATION_ID);
                        dialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onAmbiguityFeedback(CustomTripDialog dialog) {
                        Log.d(LOG_TAG,"ambiguity feedback callback");
                        makeNotification(TripDialogActivity.this,tripName);
                        dialog.dismiss();
                        finish();
                    }

                    @Override
                    public void onCancelListener(DialogInterface dialog) {
                        Log.d(LOG_TAG,"feedback dialog cancel listener callback");
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();  // Finally don't forget to call show()
    }


    private void makeNotification(Context context, String tripName) {
        Intent intent = new Intent(context, TripDialogActivity.class);
        intent.putExtra("TRIP_NAME",tripName);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("Notification Title")
                .setContentText("Sample Notification Content")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                ;
        Notification n;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            n = builder.build();
        } else {
            n = builder.getNotification();
        }

        n.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        mNotificationManager.notify(NOTIFICATION_ID, n);
    }

}
