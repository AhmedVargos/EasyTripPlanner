package fallenleafapps.com.tripplanner.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import fallenleafapps.com.tripplanner.R;

import static fallenleafapps.com.tripplanner.R.color.white;

public class TripDetails extends AppCompatActivity {


    @BindView(R.id.bottom_navigation)
    AHBottomNavigation bottomNavigation;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        ButterKnife.bind(this);
        collapsingToolbar.setTitleEnabled(false);
        toolbar.setTitle("Trip Details");
        setSupportActionBar(toolbar);

        //this line shows back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(white));
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.bottomtabfirsticon), R.drawable.ic_navigation_black_24dp);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.bottomtabsecondicon), R.drawable.ic_done_black_24dp);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.bottomtabthirdicon), R.drawable.ic_edit_black_24dp);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.bottomtabfourthicon), R.drawable.ic_delete_black_24dp);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
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
