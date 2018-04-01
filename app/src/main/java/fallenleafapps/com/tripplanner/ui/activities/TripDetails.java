package fallenleafapps.com.tripplanner.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.NoteModel;
import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.network.FirebaseHelper;
import fallenleafapps.com.tripplanner.ui.adapters.NotesAdapter;
import fallenleafapps.com.tripplanner.ui.services.FloatingWidgetService;
import fallenleafapps.com.tripplanner.utils.ConstantsVariables;
import fallenleafapps.com.tripplanner.utils.Functions;

import static fallenleafapps.com.tripplanner.R.color.white;

public class TripDetails extends AppCompatActivity {


    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    Intent incomingintent;
    @BindView(R.id.tripname)
    TextView tripname;
    @BindView(R.id.tripstatus)
    TextView tripstatus;
    @BindView(R.id.tripdate)
    TextView tripdate;
    @BindView(R.id.triptime)
    TextView triptime;
    @BindView(R.id.startlocation)
    TextView startlocation;
    @BindView(R.id.endlocation)
    TextView endlocation;

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    List<NoteModel> noteModels;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    NotesAdapter notesAdapter;
    LinearLayoutManager HorizontalLayout;
    View ChildView;
    int noteViewItemPosition;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.trip_type)
    TextView tripType;
    private TripModel displayedTrip;
    private String userId;
    private ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            finish();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            finish();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        ButterKnife.bind(this);

        displayedTrip = getIntent().getParcelableExtra(ConstantsVariables.TRIP_OBJ);

        noteModels = displayedTrip.getNotes();
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);


        notesAdapter = new NotesAdapter(noteModels);
        HorizontalLayout = new LinearLayoutManager(TripDetails.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);

        recyclerView.setAdapter(notesAdapter);

        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(recyclerView);

        collapsingToolbar.setTitleEnabled(false);
        toolbar.setTitle(displayedTrip.getTripName() + " Details");
        setSupportActionBar(toolbar);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String statusText;
        switch (displayedTrip.getTripStatus()) {
            case ConstantsVariables.TRIP_UPCOMMING_STATE:
                statusText = ConstantsVariables.TRIP_UPCOMMING_TEXT;
                break;
            case ConstantsVariables.TRIP_DONE_STATE:
                statusText = ConstantsVariables.TRIP_DONE_TEXT;
                break;
            case ConstantsVariables.TRIP_CANCELD_STATE:
                statusText = ConstantsVariables.TRIP_CANCELD_TEXT;
                break;
            case ConstantsVariables.TRIP_STARTED_STATE:
                statusText = ConstantsVariables.TRIP_STARTED_TEXT;
                break;
            default:
                statusText = "Unknown";
        }
        tripstatus.setText(statusText);

        if(displayedTrip.isTripType()){
            tripType.setText(ConstantsVariables.TRIP_TYPE_ROUND_TRIP);
        }else{
            tripType.setText(ConstantsVariables.TRIP_TYPE_SINGLE_TYME);
        }

        tripname.setText(displayedTrip.getTripName());

        SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String dateString = formatterDate.format(new Date(displayedTrip.getTripDate()));

        SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String timeString = formatterTime.format(new Date(displayedTrip.getTripTime()));

        tripdate.setText(dateString);
        triptime.setText(timeString);

        startlocation.setText(displayedTrip.getStartLocationName());
        endlocation.setText(displayedTrip.getEndLocationName());

        //this line shows back button
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setn
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setTitleTextColor(getResources().getColor(white));
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.bottomtabfirsticon), R.drawable.ic_navigation_black_24dp);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.bottomtabfourthicon), R.drawable.ic_done_black_24dp);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.bottomtabsecondicon), R.drawable.ic_edit_black_24dp);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.bottomtabthirdicon), R.drawable.ic_delete_black_24dp);
        //add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

        //set colors
        // bottomNavigation.setAccentColor(R.color.cyan);
        // bottomNavigation.setInactiveColor(R.color.colorPrimary);

        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                switch (position) {

                    case 0:
                        //start trip
                        makeAnAlert(1);
                          break;
                    case 1:
                        //done trip
                        if(displayedTrip.isTripType()) {
                            startReturnTripAfterFinish(displayedTrip);
                        }else{
                            updateTheTripStatus(displayedTrip, ConstantsVariables.TRIP_DONE_STATE);
                        }
                        break;
                    case 2:
                        //edit trip
                        break;
                    case 3:
                        //delete trip
                        makeAnAlert(0);
                        break;


                }
                return true;
            }
        });
        bottomNavigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {
            @Override
            public void onPositionChange(int y) {
                // Manage the new y position
            }
        });

        if (displayedTrip.getTripStatus() == ConstantsVariables.TRIP_STARTED_STATE) {
            bottomNavigation.disableItemAtPosition(0);
        }else if(displayedTrip.getTripStatus() == ConstantsVariables.TRIP_DONE_STATE || displayedTrip.getTripStatus() == ConstantsVariables.TRIP_CANCELD_STATE){
            bottomNavigation.disableItemAtPosition(1);
        }

        FirebaseHelper.getInstance().getFirebaseDatabase().child("trips").child(userId).addChildEventListener(childEventListener);

    }

    private void makeAnAlert(final int action){
        String title;
        String message;
        switch (action){
            case 0:
                title = "Delete";
                message = "Are you sure you want to delete it?";
                break;
            case 1:
                title = "Start";
                message = "Are you sure you want to Start the trip?";
                break;
            default:
                title = "Delete";
                message = "Are you sure you want to delete it?";
        }

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(title);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(action == 0){
                            FirebaseHelper.getInstance().deleteTrip(displayedTrip, userId);
                            Functions.unschedulePendingIntent(TripDetails.this,displayedTrip);

                            //finish();
                        }else{
                            lunchMapDirectionToLocation(displayedTrip);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(TripDetails.this)) {
                                //If the draw over permission is not available open the settings screen
                                //to grant the permission.
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + TripDetails.this.getPackageName()));
                                startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
                            } else {
                                initializeView(displayedTrip);
                            }

                            updateTheTripStatus(displayedTrip,ConstantsVariables.TRIP_STARTED_STATE);
                            //finish();
                        }
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
    public void lunchMapDirectionToLocation(TripModel tripStarted){

        String originLoc = tripStarted.getStartLat() + "," + tripStarted.getStartLang();
        String distLoc = tripStarted.getEndLat() + "," + tripStarted.getEndLang();

        String URL = "https://www.google.com/maps/dir/?api=1&origin=" + originLoc + "&destination=" + distLoc;
        //String URL = "https://www.google.com/maps/dir/?api=1&origin=31.200092,29.918739&destination=30.04442,31.235712";

        Uri location = Uri.parse(URL);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);

    }

    private void initializeView(TripModel trip) {

        Intent i=new Intent(this, FloatingWidgetService.class);
        i.putExtra(ConstantsVariables.TRIP_OBJ, trip);
        startService(i);
        // finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (resultCode == RESULT_OK) {
                initializeView(displayedTrip);
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateTheTripStatus(TripModel trip , int state){
        trip.setTripStatus(state);
        FirebaseHelper.getInstance().getFirebaseDatabase().child("trips").child(userId).child(trip.getTripFirebaseId()).setValue(trip);
    }

    private void startReturnTripAfterFinish(TripModel trip){
        TripModel returnTrip = new TripModel();
        //setting the return trip
        returnTrip.setTripName(trip.getTripName() + " Return");

        returnTrip.setStartLocationName(trip.getEndLocationName());
        returnTrip.setStartLang(trip.getEndLang());
        returnTrip.setStartLat(trip.getEndLat());

        returnTrip.setEndLocationName(trip.getStartLocationName());
        returnTrip.setEndLang(trip.getStartLang());
        returnTrip.setEndLat(trip.getStartLat());

        returnTrip.setTripStatus(ConstantsVariables.TRIP_STARTED_STATE);
        returnTrip.setTripType(false);

        returnTrip.setNotes(trip.getNotes());
        Random r = new Random(); // make a random number of the trip id
        int min = 1, max = 62000;
        int trip1Id = r.nextInt(max - min + 1) + min; //id
        returnTrip.setTripId(trip1Id);

        returnTrip.setTripTime(Calendar.getInstance().getTime().getTime() + 1000);
        returnTrip.setTripDate(Calendar.getInstance().getTime().getTime() + 1000);

        //add new one and start it
        lunchMapDirectionToLocation(returnTrip);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(TripDetails.this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            displayedTrip = returnTrip;
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + TripDetails.this.getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            initializeView(returnTrip);
        }

        //Add new one
        FirebaseHelper.getInstance().addTrip(returnTrip, FirebaseAuth.getInstance().getCurrentUser().getUid());


        //remove old trip
        Functions.unschedulePendingIntent(TripDetails.this,trip);
        FirebaseHelper.getInstance().deleteTrip(trip, userId);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseHelper.getInstance().getFirebaseDatabase().child("trips").child(userId).removeEventListener(childEventListener);
    }
}
