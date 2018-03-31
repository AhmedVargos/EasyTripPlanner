package fallenleafapps.com.tripplanner.ui.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.network.FirebaseHelper;
import fallenleafapps.com.tripplanner.utils.ConstantsVariables;
import fallenleafapps.com.tripplanner.utils.DirectionsJSONParser;
import fallenleafapps.com.tripplanner.utils.Functions;

public class HistoryFragment extends Fragment implements OnMapReadyCallback {


    @BindView(R.id.fragment_history_map)
    MapView fragmentHistoryMap;
    Unbinder unbinder;
    static volatile GoogleMap mMap;
    //ArrayList<TripModel> userTrips;
    @BindView(R.id.connection_container)
    RelativeLayout connectionContainer;
    private volatile DownloadTask downloadTask;
    private String userId;
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            TripModel trip = dataSnapshot.getValue(TripModel.class);
            if(trip.getTripStatus() == ConstantsVariables.TRIP_DONE_STATE ){
                getDirectionsOnMap(trip);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);

        if(Functions.isInternetConnected(getActivity())){

            fragmentHistoryMap.onCreate(savedInstanceState);
            fragmentHistoryMap.getMapAsync(this);
            //userTrips = new ArrayList<>();
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            FirebaseHelper.getInstance().getFirebaseDatabase().child("trips").child(userId).addChildEventListener(childEventListener);

        }else{

            connectionContainer.setVisibility(View.VISIBLE);
            fragmentHistoryMap.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseHelper.getInstance().getFirebaseDatabase().child("trips").child(userId).removeEventListener(childEventListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Disable Movement while loading
        //mMap.getUiSettings().setScrollGesturesEnabled(false);
        //mMap.getUiSettings().setZoomGesturesEnabled(false);
        /* mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(34.088808,-118.40612), new LatLng(40.7, -74.0))
                .width(5)
                .color(Color.RED));

        mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(25.767368,-80.18930), new LatLng(40.727093,-73.978639833333))
                .width(5)
                .color(Color.RED));
*/

        LatLng location = new LatLng(Double.parseDouble("30.044420"), Double.parseDouble("31.235712"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));

        //LatLng location = new LatLng(Double.parseDouble(userTrips.get(0).getStartLat()),Double.parseDouble(userTrips.get(0).getStartLat()));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(userTrips.get(0).getStartLat(),Double.parseDouble(userTrips.get(0).getStartLat()), 15));
        fragmentHistoryMap.onResume();
    }

    private void getDirectionsOnMap(TripModel tripModel) {
        //for (int i = 0; i < userTrips.size(); i++) {
            LatLng origin = new LatLng(Double.parseDouble(tripModel.getStartLat()), Double.parseDouble(tripModel.getStartLang()));
            LatLng dest = new LatLng(Double.parseDouble(tripModel.getEndLat()), Double.parseDouble(tripModel.getEndLang()));

            mMap.addMarker(new MarkerOptions().position(origin).title(tripModel.getStartLocationName()));
            mMap.addMarker(new MarkerOptions().position(dest).title(tripModel.getEndLocationName()));


            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
       // }
        /*
        mMap.addMarker(new MarkerOptions().position(new LatLng(40.7, -74.0)).title("Source"));
        mMap.addMarker(new MarkerOptions().position(new LatLng(34.088808,-118.40612)).title("Destination"));
*/
        //if (userTrips.size() > 0) {
            LatLng location = new LatLng(Double.parseDouble(tripModel.getStartLat()), Double.parseDouble(tripModel.getStartLang()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
        //} else {
            //Cairo Default location
//            LatLng location = new LatLng(Double.parseDouble("30.044420"), Double.parseDouble("31.235712"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
        //}
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


    private static class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private static class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                lineOptions.color(color);
                lineOptions.geodesic(true);

            }
            if (lineOptions != null) {

                // Drawing polyline in the Google Map for the i-th route
                mMap.addPolyline(lineOptions);

                //mMap.getUiSettings().setScrollGesturesEnabled(true);
                //mMap.getUiSettings().setZoomGesturesEnabled(true);

            }


        }
    }

    private static String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String keyVal = "AIzaSyDmHpyqImH3LGGsXog9vw3HAI10lY6SPNI"; //Google Directions Key
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&key=" + keyVal;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
