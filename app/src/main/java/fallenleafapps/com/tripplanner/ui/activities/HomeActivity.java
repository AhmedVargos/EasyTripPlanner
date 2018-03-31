package fallenleafapps.com.tripplanner.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.models.UserModel;
import fallenleafapps.com.tripplanner.network.FirebaseHelper;
import fallenleafapps.com.tripplanner.ui.fragments.HistoryFragment;
import fallenleafapps.com.tripplanner.ui.fragments.HomeFragment;
import fallenleafapps.com.tripplanner.ui.fragments.StatisticsFragment;
import fallenleafapps.com.tripplanner.utils.ConstantsVariables;
import fallenleafapps.com.tripplanner.utils.Functions;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.include_toolbar)
    Toolbar includeToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setSupportActionBar(includeToolbar);
        getSupportActionBar().setTitle("Home");

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //rescheduleAlerts();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, includeToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header=navigationView.getHeaderView(0);
        final TextView headerName = (TextView)header.findViewById(R.id.header_user_name);
        TextView headerMail = (TextView)header.findViewById(R.id.header_user_email);

        headerMail.setText(firebaseUser.getEmail());
        if(firebaseUser.getDisplayName() != null){
            headerName.setText(firebaseUser.getDisplayName());
        }else{

            /*
            FirebaseHelper.getInstance().getFirebaseDatabase().child("users").child(firebaseUser.getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    UserModel loggedUser = dataSnapshot.getValue(UserModel.class);
                    headerName.setText(loggedUser.getUserName());
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
            });

*/
            FirebaseHelper.getInstance().getFirebaseDatabase().child("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserModel loggedUser = dataSnapshot.getValue(UserModel.class);
                    headerName.setText(loggedUser.getUserName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        HomeFragment homeFragment = new HomeFragment();
        Functions.changeMainFragment(this,homeFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //use amvMenu here
        inflater.inflate(R.menu.main_toolbar_items, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.syncData){
            Toast.makeText(this, "Will Sync Data Now", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            drawerLayout.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle("Home");
            HomeFragment homeFragment = new HomeFragment();
            Functions.changeMainFragment(this,homeFragment);
        } else if (id == R.id.nav_statistics) {
            drawerLayout.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle("Statistics");
            StatisticsFragment statisticsFragment = new StatisticsFragment();
            Functions.changeMainFragment(this,statisticsFragment);

        } else if (id == R.id.nav_history) {
            drawerLayout.closeDrawer(GravityCompat.START);
            getSupportActionBar().setTitle("History");
            HistoryFragment historyFragment = new HistoryFragment();
            Functions.changeMainFragment(this,historyFragment);
        }else if(id == R.id.nav_logout){
            removeAllAlarms();
            drawerLayout.closeDrawer(GravityCompat.START);
            Toast.makeText(this, "IS logged out", Toast.LENGTH_SHORT).show();
            //removeAllAlarms();
            //FirebaseHelper.getInstance().getFirebaseDatabase().
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            finish();
        }
        //drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
    private void removeAllAlarms() {
      //TODO some how get all the upcoming trips from the list and remove it!!
        FirebaseHelper.getInstance().getFirebaseDatabase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TripModel trip = dataSnapshot.getValue(TripModel.class);
                if(trip.getTripStatus() == ConstantsVariables.TRIP_UPCOMMING_STATE || trip.getTripStatus() == ConstantsVariables.TRIP_STARTED_STATE){
                    Functions.unschedulePendingIntent(HomeActivity.this,trip);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void rescheduleAlerts(){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser != null ){

            FirebaseHelper.getInstance().getFirebaseDatabase().child("trips").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TripModel trip = dataSnapshot.getValue(TripModel.class);
                    if(trip.getTripStatus() == ConstantsVariables.TRIP_UPCOMMING_STATE){
                        Functions.scheduleAlarm(HomeActivity.this, trip);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
