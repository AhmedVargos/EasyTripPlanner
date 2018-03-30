package fallenleafapps.com.tripplanner.ui.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.network.FirebaseHelper;
import fallenleafapps.com.tripplanner.utils.ConstantsVariables;
import fallenleafapps.com.tripplanner.utils.Functions;

/**
 * Created by Vargos on 23-Mar-18.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            //TODO LOOP through all the upcomming trips and set it's alarm
            // Set alarms

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            if(firebaseUser != null ){

                FirebaseHelper.getInstance().getFirebaseDatabase().child("trips").child(firebaseUser.getUid()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        TripModel trip = dataSnapshot.getValue(TripModel.class);
                        if(trip.getTripStatus() == ConstantsVariables.TRIP_UPCOMMING_STATE){
                            Functions.scheduleAlarm(context, trip);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        }


    }


}
