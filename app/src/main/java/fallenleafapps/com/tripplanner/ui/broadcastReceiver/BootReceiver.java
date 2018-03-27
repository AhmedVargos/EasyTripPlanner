package fallenleafapps.com.tripplanner.ui.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Vargos on 23-Mar-18.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //TODO LOOP through all the upcomming trips and set it's alarm
            // Set alarms
            Toast.makeText(context, "The broadcast is working", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context, "The broadcast is working", Toast.LENGTH_SHORT).show();

    }
}
