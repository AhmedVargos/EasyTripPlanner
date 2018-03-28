package fallenleafapps.com.tripplanner.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dd.morphingbutton.MorphingButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.NoteModel;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            }
        });
        return view;
    }

    private void setupRecycler() {
        ArrayList<NoteModel> noteModels = new ArrayList<>();
        NoteModel noteModel = new NoteModel(0, "This is a note to test", false);
        NoteModel noteModel1 = new NoteModel(1, "This is a note", false);
        NoteModel noteModel2 = new NoteModel(2, "This is a long note so we can see the limit", false);
        NoteModel noteModel3 = new NoteModel(2, "test test", false);

        noteModels.add(noteModel);
        noteModels.add(noteModel1);

        noteModels.add(noteModel2);
        noteModels.add(noteModel3);


        List<TripModel> tripModels = new ArrayList<>();
        TripModel trip1 = new TripModel("Go to cairo", (long) 1521565022, (long) 1522166222, "6 October", "Cairo", true, 1);
        trip1.setTripId(1);
        trip1.setNotes(noteModels);
        TripModel trip2 = new TripModel("Go to Giza", (long) 1521565022, (long) 1522166222, "6 October", "Cairo", true, 1);
        trip2.setTripId(2);
        trip2.setNotes(noteModels);

        tripModels.add(trip1);
        tripModels.add(trip2);

        TripRecyclerAdapter tripRecyclerAdapter = new TripRecyclerAdapter(getActivity(), tripModels, this, 0);
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
    public void deleteTrip(TripModel trip) {
        Toast.makeText(getActivity(), trip.getTripName() + " Is clicked", Toast.LENGTH_SHORT).show();
        Functions.unschedulePendingIntent(getContext(),trip);
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
}
