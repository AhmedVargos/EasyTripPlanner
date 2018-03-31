package fallenleafapps.com.tripplanner.ui.fragments;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.util.PixelUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.network.FirebaseHelper;
import fallenleafapps.com.tripplanner.utils.ConstantsVariables;


public class StatisticsFragment extends Fragment {

    @BindView(R.id.trips_pie_chart)
    PieChart tripsPieChart;
    Unbinder unbinder;
    @BindView(R.id.stats_trip_total)
    TextView statsTripTotal;
    @BindView(R.id.stats_trip_done)
    TextView statsTripDone;
    private int finishedTripsNum = 0;
    private int totalTripsNum = 0;
    private String userId;
    private Segment segment;
    private Segment segment2;

    private  ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            TripModel trip = dataSnapshot.getValue(TripModel.class);
            if (trip.getTripStatus() == ConstantsVariables.TRIP_DONE_STATE) {
                finishedTripsNum++;
            }
            totalTripsNum++;
            statsTripDone.setText("Done Trips: " + finishedTripsNum);
            statsTripTotal.setText("Total Trips: " + totalTripsNum);
            segment.setValue(finishedTripsNum);
            segment2.setValue(totalTripsNum-finishedTripsNum);

            if(finishedTripsNum == 0 ){
                segment.setTitle(null);
            }else{
                segment.setTitle("Finished");
            }
            if(totalTripsNum > 0){

                tripsPieChart.setTitle("Finished Trips");
                statsTripDone.setVisibility(View.VISIBLE);
                statsTripTotal.setVisibility(View.VISIBLE);
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
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        unbinder = ButterKnife.bind(this, view);


        final float padding = PixelUtils.dpToPix(30);
        //tripsPieChart.getPie().setPadding(padding, padding, padding, padding);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //FirebaseHelper.getInstance().getFirebaseDatabase()
        FirebaseHelper.getInstance().getFirebaseDatabase().child("trips").child(userId).addChildEventListener(childEventListener);

        segment = new Segment("Finished", finishedTripsNum);
        segment2 = new Segment("Unfinished", totalTripsNum);


        SegmentFormatter formatter = new SegmentFormatter(getActivity(), R.xml.pie_char_font_style);
        formatter.setOffset(32);
        SegmentFormatter formatter2 = new SegmentFormatter(getActivity(), R.xml.pie_char_font_style2);

        tripsPieChart.setTitle("Finished Trips");
        tripsPieChart.addSegment(segment2, formatter2);
        tripsPieChart.addSegment(segment, formatter);
        //segment.setValue(100);
        tripsPieChart.setBackgroundColor(Color.WHITE);

        if(totalTripsNum == 0){
            tripsPieChart.setTitle("No Trips Made");
            statsTripDone.setVisibility(View.GONE);
            statsTripTotal.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupIntroAnimation();
    }

    @Override
    public void onPause() {

        super.onPause();
        FirebaseHelper.getInstance().getFirebaseDatabase().child("trips").child(userId).removeEventListener(childEventListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected void setupIntroAnimation() {

        final PieRenderer renderer = tripsPieChart.getRenderer(PieRenderer.class);
        // start with a zero degrees pie:

        renderer.setExtentDegs(0);
        // animate a scale value from a starting val of 0 to a final value of 1:
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);

        // use an animation pattern that begins and ends slowly:
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float scale = valueAnimator.getAnimatedFraction();
                renderer.setExtentDegs(360 * scale);
                tripsPieChart.redraw();
            }
        });

        // the animation will run for 1.0 seconds:
        animator.setDuration(1000);
        animator.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
