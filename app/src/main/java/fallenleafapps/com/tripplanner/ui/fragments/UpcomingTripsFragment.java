package fallenleafapps.com.tripplanner.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.NoteModel;
import fallenleafapps.com.tripplanner.network.FirebaseHelper;
import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.ui.activities.AddTripActivity;
import fallenleafapps.com.tripplanner.ui.activities.TripDialogActivity;
import fallenleafapps.com.tripplanner.ui.adapters.TripRecyclerAdapter;
import fallenleafapps.com.tripplanner.ui.listeners.TripCardListener;
import fallenleafapps.com.tripplanner.ui.services.FloatingWidgetService;
import fallenleafapps.com.tripplanner.utils.ConstantsVariables;
import fallenleafapps.com.tripplanner.utils.Functions;

import static android.app.Activity.RESULT_OK;

public class UpcomingTripsFragment extends Fragment implements TripCardListener {

    private static final String LOG_TAG = "HOME FRAGMENT";
    @BindView(R.id.fragment_home_recycler)
    RecyclerView fragmentHomeRecycler;
    Unbinder unbinder;
    @BindView(R.id.btn_add_trip)
    FloatingActionButton btnAddTrip;

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    private ArrayList<TripModel> tripsUpComming = new ArrayList<>();
    private boolean deleteFromDetail = false;
    private TripRecyclerAdapter tripRecyclerAdapter;
    private String userId;
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            TripModel trip = dataSnapshot.getValue(TripModel.class);
            if(trip.getTripStatus() == ConstantsVariables.TRIP_UPCOMMING_STATE || trip.getTripStatus() == ConstantsVariables.TRIP_STARTED_STATE){
                //tripsList.add(trip);
                tripRecyclerAdapter.addNewTrip(trip);
                tripsUpComming.add(trip);

            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Log.e(LOG_TAG, "onChildChanged: ");
            TripModel trip = dataSnapshot.getValue(TripModel.class);
            if (trip.getTripStatus() == ConstantsVariables.TRIP_UPCOMMING_STATE || trip.getTripStatus() == ConstantsVariables.TRIP_STARTED_STATE) {
                tripRecyclerAdapter.changeTrip(dataSnapshot.getValue(TripModel.class));
                if(trip.getTripStatus() == ConstantsVariables.TRIP_STARTED_STATE){
//                    deleteTripFromTheUpcomming(trip);
                }
            } else if (trip.getTripStatus() == ConstantsVariables.TRIP_CANCELD_STATE || trip.getTripStatus() == ConstantsVariables.TRIP_DONE_STATE) {
                tripRecyclerAdapter.removeTripWithoutPos(trip);
//                deleteTripFromTheUpcomming(trip);
            }
        }
        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            if(deleteFromDetail){

                TripModel trip = dataSnapshot.getValue(TripModel.class);
                if(trip.getTripStatus() == ConstantsVariables.TRIP_UPCOMMING_STATE || trip.getTripStatus() == ConstantsVariables.TRIP_STARTED_STATE){
                    //tripsList.add(trip);
                    tripRecyclerAdapter.removeTripWithoutPos(trip);
                    deleteTripFromTheUpcomming(trip);
                }
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private TripModel myStartedTrip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseHelper.getInstance().getFirebaseDatabase().child("trips").child(userId).addChildEventListener(childEventListener);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_up_comming_trips, container, false);
        unbinder = ButterKnife.bind(this, view);
        setupRecycler();

        btnAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  startActivity(new Intent(getActivity(), AddTripActivity.class));
            }
        });
        return view;
    }

    private void setupRecycler() {
        tripRecyclerAdapter = new TripRecyclerAdapter(getActivity(), this, 0);
        fragmentHomeRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        fragmentHomeRecycler.setAdapter(tripRecyclerAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        deleteFromDetail = false;
        //tripRecyclerAdapter.clearListOfTrips();
        }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void openTripDetails(TripModel trip) {
        deleteFromDetail = true;
        Intent intent = new Intent(getActivity(),fallenleafapps.com.tripplanner.ui.activities.TripDetails.class);
        intent.putExtra(ConstantsVariables.TRIP_OBJ, trip);
        startActivity(intent);
    }

    @Override
    public void deleteTrip(TripModel trip,int pos) {
        makeAnAlert(trip,pos); //ask for deletion
    }

    @Override
    public void startTrip(TripModel trip, MorphingButton morphingButton) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(500)
                .cornerRadius(112) // 56 dp
                .width(112) // 56 dp
                .height(112) // 56 dp
                .color(getResources().getColor(R.color.colorAccent)) // normal state color
                .colorPressed(getResources().getColor(R.color.colorPrimaryDark)) // pressed state color
                .icon(R.drawable.ic_navigation_black_24dp); // icon
        morphingButton.morph(circle);
        morphingButton.setEnabled(false);

        openTheStartTripActions(trip);
        Functions.unschedulePendingIntent(getContext(), trip);
    }


    private void makeAnAlert(final TripModel trip, final int pos){
        String title = "Delete";
        String message = "Are you sure you want to delete it?";

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseHelper.getInstance().deleteTrip(trip, userId);
                        //if(trip.getTripStatus() == ConstantsVariables.TRIP_UPCOMMING_STATE){ // FOR removing just the upcoming trips from schedule
                            Functions.unschedulePendingIntent(getContext(), trip);
                        //}

                        tripRecyclerAdapter.removeTrip(trip,pos);
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void deleteTripFromTheUpcomming(TripModel tripModel){
        int pos = 0;
        for (int i=0; i < tripsUpComming.size(); i++){
            if( tripsUpComming.get(i).getTripFirebaseId().equals(tripModel.getTripFirebaseId())){
                pos = i;
            }
        }
        tripsUpComming.remove(pos);
    }

    private void openTheStartTripActions(TripModel trip){
        myStartedTrip = trip;
        lunchMapDirectionToLocation(myStartedTrip);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getActivity())) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getActivity().getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            initializeView(myStartedTrip);
        }
    }

    public void lunchMapDirectionToLocation(TripModel tripStarted){

        String originLoc = tripStarted.getStartLat() + "," + tripStarted.getStartLang();
        String distLoc = tripStarted.getEndLat() + "," + tripStarted.getEndLang();

        String URL = "https://www.google.com/maps/dir/?api=1&origin=" + originLoc + "&destination=" + distLoc;
        //String URL = "https://www.google.com/maps/dir/?api=1&origin=31.200092,29.918739&destination=30.04442,31.235712";

        Uri location = Uri.parse(URL);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
        //change the trip state
        tripStarted.setTripStatus(ConstantsVariables.TRIP_STARTED_STATE);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseHelper.getInstance().getFirebaseDatabase().child("trips").child(userId).child(tripStarted.getTripFirebaseId()).setValue(tripStarted);

    }


    private void initializeView(TripModel trip) {

        Intent i=new Intent(getActivity(), FloatingWidgetService.class);
        i.putExtra(ConstantsVariables.TRIP_OBJ, trip);
        getActivity().startService(i);
        // finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView(myStartedTrip);
            } else { //Permission is not available
                Toast.makeText(getActivity(),
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseHelper.getInstance().getFirebaseDatabase().child("trips").child(userId).removeEventListener(childEventListener);
    }
}
