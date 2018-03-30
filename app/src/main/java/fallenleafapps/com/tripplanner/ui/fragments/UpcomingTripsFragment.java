package fallenleafapps.com.tripplanner.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.NoteModel;
import fallenleafapps.com.tripplanner.network.FirebaseHelper;
import fallenleafapps.com.tripplanner.ui.activities.TripDetails;
import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.ui.adapters.TripRecyclerAdapter;
import fallenleafapps.com.tripplanner.ui.listeners.TripCardListener;
import fallenleafapps.com.tripplanner.utils.ConstantsVariables;
import fallenleafapps.com.tripplanner.utils.Functions;

public class UpcomingTripsFragment extends Fragment implements TripCardListener {

    private static final String LOG_TAG = "HOME FRAGMENT";
    @BindView(R.id.fragment_home_recycler)
    RecyclerView fragmentHomeRecycler;
    Unbinder unbinder;
    @BindView(R.id.btn_add_trip)
    FloatingActionButton btnAddTrip;

    private TripRecyclerAdapter tripRecyclerAdapter;
    private String userId;
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            TripModel trip = dataSnapshot.getValue(TripModel.class);
            if(trip.getTripStatus() == ConstantsVariables.TRIP_UPCOMMING_STATE){

                //tripsList.add(trip);
                tripRecyclerAdapter.addNewTrip(trip);

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
        View view = inflater.inflate(R.layout.fragment_up_comming_trips, container, false);
        unbinder = ButterKnife.bind(this, view);
        setupRecycler();

        btnAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Open Add New Trip", Toast.LENGTH_SHORT).show();
                Random r = new Random(); // make a random number of the trip id
                int min = 1, max = 62000;
                int trip1Id = r.nextInt(max - min + 1) + min;
                int trip2Id = r.nextInt(max - min + 1) + min;

                TripModel trip1 = new TripModel(trip1Id,"First Trip", new Long(12225), new Long(42556), "30.044420", "31.235712", "Cairo", "30.013056", "31.208853", "Giza", true, 1, new ArrayList<NoteModel>());
                TripModel trip2 = new TripModel(trip2Id,"Third Trip", new Long(12225), new Long(42556), "24.088938", "32.899829", "Asuan", "30.013056", "31.208853", "Giza", true, 2, new ArrayList<NoteModel>());

                trip1.addNote(new NoteModel(2,"hello this the first note",true));
                trip1.addNote(new NoteModel(3,"hello this the second note",true));
                trip2.addNote(new NoteModel(2,"hello this the first note",true));
                trip2.addNote(new NoteModel(3,"hello this the second note",true));
                FirebaseHelper.getInstance().addTrip(trip1,FirebaseAuth.getInstance().getCurrentUser().getUid());
                FirebaseHelper.getInstance().addTrip(trip2,FirebaseAuth.getInstance().getCurrentUser().getUid());

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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void openTripDetails(TripModel trip) {
        Toast.makeText(getActivity(), trip.getTripName() + " Is clicked", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(),fallenleafapps.com.tripplanner.ui.activities.TripDetails.class);
        intent.putExtra(ConstantsVariables.TRIP_OBJ,trip);
        startActivity(intent);
    }

    @Override
    public void deleteTrip(TripModel trip,int pos) {
        FirebaseHelper.getInstance().deleteTrip(trip, userId);
        Functions.unschedulePendingIntent(getContext(),trip);

        tripRecyclerAdapter.removeTrip(trip,pos);
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

        Functions.scheduleAlarm(getContext(), trip);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseHelper.getInstance().getFirebaseDatabase().child("trips").child(userId).removeEventListener(childEventListener);
    }
}
