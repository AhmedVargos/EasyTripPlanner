package fallenleafapps.com.tripplanner.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fallenleafapps.com.tripplanner.R;

public class HistoryFragment extends Fragment implements OnMapReadyCallback {


    @BindView(R.id.fragment_history_map)
    MapView fragmentHistoryMap;
    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);

        //TODO query the DB for all the past trips and make lines and markers of them
        fragmentHistoryMap.onCreate(savedInstanceState);
        fragmentHistoryMap.getMapAsync(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(34.088808,-118.40612), new LatLng(40.7, -74.0))
                .width(5)
                .color(Color.RED));

        googleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(25.767368,-80.18930), new LatLng(40.727093,-73.978639833333))
                .width(5)
                .color(Color.RED));


        googleMap.addMarker(new MarkerOptions().position(new LatLng(40.7, -74.0)).title("Source"));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(34.088808,-118.40612)).title("Destination"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.727093,-73.978639833333), 11));
        fragmentHistoryMap.onResume();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
}
