package fallenleafapps.com.tripplanner.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidplot.pie.PieChart;
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
        SegmentFormatter formatter2 = new SegmentFormatter(getActivity(),R.xml.pie_char_font_style2);

        tripsPieChart.setTitle("Finished Trips");
        tripsPieChart.addSegment(segment, formatter);
        tripsPieChart.addSegment(segment2, formatter2);
        tripsPieChart.setBackgroundColor(Color.WHITE);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
