package fallenleafapps.com.tripplanner.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.network.FirebaseHelper;
import fallenleafapps.com.tripplanner.ui.adapters.TripRecyclerAdapter;
import fallenleafapps.com.tripplanner.ui.listeners.TripCardListener;
import fallenleafapps.com.tripplanner.utils.ConstantsVariables;
import fallenleafapps.com.tripplanner.utils.Functions;

public class PastTripsFragment extends Fragment implements TripCardListener {
    @BindView(R.id.fragment_past_trips_recycler)
    RecyclerView fragmentPastTripsRecycler;
    Unbinder unbinder;

    private boolean deleteFromDetail = false;
    private String userId;
    private TripRecyclerAdapter tripRecyclerAdapter;
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            TripModel trip = dataSnapshot.getValue(TripModel.class);
            if(trip.getTripStatus() == ConstantsVariables.TRIP_CANCELD_STATE || trip.getTripStatus() == ConstantsVariables.TRIP_DONE_STATE ){

                tripRecyclerAdapter.addNewTrip(trip);


            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            TripModel trip = dataSnapshot.getValue(TripModel.class);

            if(trip.getTripStatus() == ConstantsVariables.TRIP_CANCELD_STATE || trip.getTripStatus() == ConstantsVariables.TRIP_DONE_STATE ) {
                tripRecyclerAdapter.addNewTrip(dataSnapshot.getValue(TripModel.class));
            }else if (trip.getTripStatus() == ConstantsVariables.TRIP_UPCOMMING_STATE) {
                tripRecyclerAdapter.removeTripWithoutPos(trip);
//                deleteTripFromTheUpcomming(trip);
            }else if(trip.getTripStatus() == ConstantsVariables.TRIP_STARTED_STATE){
                tripRecyclerAdapter.removeTripWithoutPos(trip);
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            if(deleteFromDetail){
                TripModel trip = dataSnapshot.getValue(TripModel.class);
                if(trip.getTripStatus() == ConstantsVariables.TRIP_CANCELD_STATE || trip.getTripStatus() == ConstantsVariables.TRIP_DONE_STATE ){
                    //tripsList.add(trip);
                    tripRecyclerAdapter.removeTripWithoutPos(trip);
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
        View view = inflater.inflate(R.layout.fragment_past_trips, container, false);
        unbinder = ButterKnife.bind(this, view);
        setupRecycler();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        deleteFromDetail = false;

    }

    private void setupRecycler() {

        tripRecyclerAdapter = new TripRecyclerAdapter(getActivity(),this,1);
        fragmentPastTripsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        fragmentPastTripsRecycler.setAdapter(tripRecyclerAdapter);

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
        intent.putExtra(ConstantsVariables.TRIP_OBJ,trip);
        startActivity(intent);
    }

    @Override
    public void deleteTrip(TripModel trip,int pos) {
        makeAnAlert(trip,pos); //ask for deletion
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

    @Override
    public void startTrip(TripModel trip, MorphingButton morphingButton) throws Exception {
        throw  new Exception("Not implemented for the past trips");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseHelper.getInstance().getFirebaseDatabase().child("trips").child(userId).removeEventListener(childEventListener);
    }
}
