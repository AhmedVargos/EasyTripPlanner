package fallenleafapps.com.tripplanner.ui.fragments;

import android.os.Bundle;
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
import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.ui.listeners.TripCardListener;
import fallenleafapps.com.tripplanner.ui.adapters.TripRecyclerAdapter;
import fallenleafapps.com.tripplanner.utils.Functions;

public class UpcomingTripsFragment extends Fragment implements TripCardListener {

    private static final String LOG_TAG = "HOME FRAGMENT";
    @BindView(R.id.fragment_home_recycler)
    RecyclerView fragmentHomeRecycler;
    Unbinder unbinder;

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
        return view;
    }

    private void setupRecycler() {
        List<TripModel> tripModels = new ArrayList<>();
        tripModels.add(new TripModel("Go to cairo",(long)1521565022,(long)1522166222,"6 October","Cairo",true,1));
        tripModels.add(new TripModel("Go to Giza",(long)1521565022,(long)1522166222,"6 October","Cairo",true,1));

        TripRecyclerAdapter tripRecyclerAdapter = new TripRecyclerAdapter(getActivity(), tripModels,this,0);
        fragmentHomeRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
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
    }

    @Override
    public void deleteTrip(TripModel trip) {
        Toast.makeText(getActivity(), trip.getTripName() + " Is clicked", Toast.LENGTH_SHORT).show();

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
        Functions.scheduleAlarm(getContext(), trip.getTripName());
    }
}
