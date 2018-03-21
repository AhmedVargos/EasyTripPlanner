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
import fallenleafapps.com.tripplanner.models.TripClassModel;
import fallenleafapps.com.tripplanner.ui.adapters.TripRecyclerAdapter;
import fallenleafapps.com.tripplanner.ui.listeners.TripCardListener;

public class PastTripsFragment extends Fragment implements TripCardListener {
    @BindView(R.id.fragment_past_trips_recycler)
    RecyclerView fragmentPastTripsRecycler;
    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void setupRecycler() {
        List<TripClassModel> tripClassModels = new ArrayList<>();
        tripClassModels.add(new TripClassModel("Go to cairo",(long)1521565022,(long)1522166222,"6 October","Cairo",true,1));

        TripRecyclerAdapter tripRecyclerAdapter = new TripRecyclerAdapter(getActivity(),tripClassModels,this,1);
        fragmentPastTripsRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        fragmentPastTripsRecycler.setAdapter(tripRecyclerAdapter);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void openTripDetails(TripClassModel trip) {
        Toast.makeText(getActivity(), trip.getTripName() + " Is clicked", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void deleteTrip(TripClassModel trip) {
        Toast.makeText(getActivity(), trip.getTripName() + " Is clicked", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void startTrip(TripClassModel trip, MorphingButton morphingButton) throws Exception {
        throw  new Exception("Not implemented for the past trips");
    }
}
