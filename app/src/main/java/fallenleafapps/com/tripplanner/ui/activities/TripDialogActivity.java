package fallenleafapps.com.tripplanner.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import fallenleafapps.com.tripplanner.utils.FeedBackActionsListeners;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.utils.CustomTripDialog;

public class TripDialogActivity extends AppCompatActivity {
    private static final String LOG_TAG = "HOME FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_dialog);

        String tripName = getIntent().getStringExtra("TRIP_NAME");

        //Start the dialog for the trip
        CustomTripDialog mDialog = new CustomTripDialog(this)
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
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegativeFeedback(CustomTripDialog dialog) {
                        Log.d(LOG_TAG,"negative feedback callback");
                        dialog.dismiss();
                    }

                    @Override
                    public void onAmbiguityFeedback(CustomTripDialog dialog) {
                        Log.d(LOG_TAG,"ambiguity feedback callback");
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelListener(DialogInterface dialog) {
                        Log.d(LOG_TAG,"feedback dialog cancel listener callback");
                        dialog.dismiss();
                    }
                })
                .show();  // Finally don't forget to call show()

  /*      FeedBackDialog mDialog = new FeedBackDialog(this)
                .setBackgroundColor(R.color.colorPrimary)
                .setIcon(R.drawable.ic_navigation_black_24dp)
                .setIconColor(R.color.sampleColor)
                .setTitle(R.string.str_start_trip)
                .setDescription(R.string.str_trip_desc)
                .setReviewQuestion(R.string.str_start_question)
                .setPositiveFeedbackText(R.string.str_yes)
                .setNegativeFeedbackText(R.string.str_no)
                .setAmbiguityFeedbackText(R.string.str_not_now)
                .setOnReviewClickListener(new FeedBackActionsListeners() {
                    @Override
                    public void onPositiveFeedback(FeedBackDialog dialog) {
                        Log.d(LOG_TAG,"positive feedback callback");
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegativeFeedback(FeedBackDialog dialog) {
                        Log.d(LOG_TAG,"negative feedback callback");
                        dialog.dismiss();
                    }

                    @Override
                    public void onAmbiguityFeedback(FeedBackDialog dialog) {
                        Log.d(LOG_TAG,"ambiguity feedback callback");
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelListener(DialogInterface dialog) {
                        Log.d(LOG_TAG,"feedback dialog cancel listener callback");
                        dialog.dismiss();
                    }
                })
                .show();  // Finally don't forget to call show()
*/
    }

}
