package fallenleafapps.com.tripplanner.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.NoteModel;
import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.ui.adapters.NotesAdapter;
import fallenleafapps.com.tripplanner.utils.ConstantsVariables;

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


    List<NoteModel> noteModels;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    NotesAdapter notesAdapter;
    LinearLayoutManager HorizontalLayout;
    View ChildView;
    int noteViewItemPosition;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private TripModel displayedTrip;

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

        String statusText;
        switch (displayedTrip.getTripStatus()){
            case ConstantsVariables.TRIP_UPCOMMING_STATE:
                statusText = ConstantsVariables.TRIP_UPCOMMING_TEXT;
                break;
                case ConstantsVariables.TRIP_DONE_STATE:
                statusText = ConstantsVariables.TRIP_DONE_TEXT;
                break;
            case ConstantsVariables.TRIP_CANCELD_STATE:
                statusText = ConstantsVariables.TRIP_CANCELD_TEXT;
                break;
            default:
                statusText = "Unknown";
        }
        tripstatus.setText(statusText);

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
                        break;
                    case 1:
                        //done trip
                        break;
                    case 2:
                        //edit trip
                        break;
                    case 3:
                        //delete trip
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

    }

}
