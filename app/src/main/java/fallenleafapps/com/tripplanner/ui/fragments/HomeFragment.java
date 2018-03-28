package fallenleafapps.com.tripplanner.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.ui.adapters.TabsAdapter;
import fallenleafapps.com.tripplanner.utils.SwipeLessFragmentPager;

public class HomeFragment extends Fragment {

    @BindView(R.id.fragment_home_tab_layout)
    TabLayout fragmentHomeTabLayout;
    @BindView(R.id.fragment_home_viewPager)
    SwipeLessFragmentPager fragmentHomeViewPager;
    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        setupViewPager();
        return view;
    }



    private void setupViewPager() {
        TabsAdapter tabsAdapter = new TabsAdapter(getChildFragmentManager());
        UpcomingTripsFragment upcomingTripsFragment = new UpcomingTripsFragment();
        PastTripsFragment pastTripsFragment = new PastTripsFragment();
        tabsAdapter.addFragment(upcomingTripsFragment, "Upcoming");
        tabsAdapter.addFragment(pastTripsFragment, "Past");

        fragmentHomeViewPager.setAdapter(tabsAdapter);
        fragmentHomeTabLayout.setupWithViewPager(fragmentHomeViewPager);
        fragmentHomeTabLayout.setBackgroundColor(getResources().getColor(R.color.gray_300));
        fragmentHomeViewPager.setCurrentItem(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
