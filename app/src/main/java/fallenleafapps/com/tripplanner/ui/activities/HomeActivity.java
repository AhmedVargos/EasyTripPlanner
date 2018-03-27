package fallenleafapps.com.tripplanner.ui.activities;

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
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.ui.fragments.HistoryFragment;
import fallenleafapps.com.tripplanner.ui.fragments.HomeFragment;
import fallenleafapps.com.tripplanner.ui.fragments.StatisticsFragment;
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


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, includeToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


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
            HomeFragment homeFragment = new HomeFragment();
            Functions.changeMainFragment(this,homeFragment);
        } else if (id == R.id.nav_statistics) {

            StatisticsFragment statisticsFragment = new StatisticsFragment();
            Functions.changeMainFragment(this,statisticsFragment);

        } else if (id == R.id.nav_history) {
            HistoryFragment historyFragment = new HistoryFragment();
            Functions.changeMainFragment(this,historyFragment);
        }else if(id == R.id.nav_logout){
            Toast.makeText(this, "IS logged out", Toast.LENGTH_SHORT).show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
