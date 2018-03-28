package fallenleafapps.com.tripplanner.ui.fragments;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.PieRenderer;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.util.PixelUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fallenleafapps.com.tripplanner.R;


public class StatisticsFragment extends Fragment {

    @BindView(R.id.trips_pie_chart)
    PieChart tripsPieChart;
    Unbinder unbinder;

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
        tripsPieChart.getPie().setPadding(padding, padding, padding, padding);

        int finishedTripsNum = 10; //TODO GET THE REAL NUMBER FROM DB
        int totalTripsNum = 45; //TODO GET THE REAL NUMBER FROM DB
        Segment segment = new Segment("Finished", finishedTripsNum);
        Segment segment2 = new Segment("Total", totalTripsNum);

        SegmentFormatter formatter = new SegmentFormatter(getActivity(),R.xml.pie_char_font_style);
        formatter.setOffset(32);
        SegmentFormatter formatter2 = new SegmentFormatter(getActivity(),R.xml.pie_char_font_style2);

        tripsPieChart.setTitle("Finished Trips");
        tripsPieChart.addSegment(segment, formatter);
        tripsPieChart.addSegment(segment2, formatter2);
        tripsPieChart.setBackgroundColor(Color.WHITE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupIntroAnimation();
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
