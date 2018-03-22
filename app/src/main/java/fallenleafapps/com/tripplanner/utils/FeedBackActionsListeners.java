package fallenleafapps.com.tripplanner.utils;

/**
 * Created by Vargos on 21-Mar-18.
 */


import android.content.DialogInterface;


/**
 * Created by Shivasurya S on 13/01/18.
 */
public interface FeedBackActionsListeners
{
    void onPositiveFeedback(CustomTripDialog dialog);
    void onNegativeFeedback(CustomTripDialog dialog);
    void onAmbiguityFeedback(CustomTripDialog dialog);
    void onCancelListener(DialogInterface dialog);
}
