package fallenleafapps.com.tripplanner.ui.activities;


import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.NoteModel;
import fallenleafapps.com.tripplanner.ui.adapters.AddingNotesAdapter;

import static fallenleafapps.com.tripplanner.R.color.colorAccent;

public class AddTripActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.input_name)
    TextInputEditText inputName;
    @BindView(R.id.btn_add_trip)
    Button btnAddTrip;
    @BindView(R.id.date_label)
    TextView dateLabel;
    @BindView(R.id.dateBtn)
    Button dateBtn;
    @BindView(R.id.timeBtn)
    Button timeBtn;
    @BindView(R.id.time_label)
    TextView timeLabel;
    @BindView(R.id.trip_name)
    TextInputLayout tripName;
    @BindView(R.id.noteText)
    TextInputEditText noteText;
    @BindView(R.id.roundTrip)
    CheckBox roundTrip;
    @BindView(R.id.addNoteBtn)
    Button addNoteBtn;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;

    Date dateOne;
    String date;
    int year1;
    int month1;
    int day1;
    boolean flag = false;
    RecyclerView recyclerView;
    AddingNotesAdapter addingNotesAdapter;
    List<NoteModel> noteModels;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    LinearLayoutManager verticalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);

        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);



        // set button underlined
        addNoteBtn.setPaintFlags(addNoteBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //// date and time part
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddTripActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                dpd.setVersion(DatePickerDialog.Version.VERSION_2);
                dpd.setAccentColor(getResources().getColor(colorAccent));


            }


        });
        timeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        AddTripActivity.this,
                        now.get(Calendar.HOUR),
                        now.get(Calendar.MINUTE),
                        now.get(Calendar.SECOND),
                        false
                );
                tpd.show(getFragmentManager(), "Timepickerdialog");
                tpd.setAccentColor(getResources().getColor(colorAccent));

            }
        });
        //// end of date and time

        /// autoComplete Search by Google
        PlaceAutocompleteFragment startAutoCompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.startPlace_autocomplete_fragment);
        startAutoCompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                LatLng latLng = place.getLatLng();
                Log.i("latitude", Double.toString(latLng.latitude));
                Log.i("longtitude", Double.toString(latLng.longitude));
                Log.i("h", "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("i", "An error occurred: " + status);
            }
        });
        PlaceAutocompleteFragment endAutoCompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.endPlace_autocomplete_fragment);
        endAutoCompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("h", "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("i", "An error occurred: " + status);
            }
        });
        //// end of autoComplete Section
        /// notes Section
        addNoteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String noteData = String.valueOf(noteText.getText());
                if (noteData != null) {

                    addNotesToRecyclerViewList(noteData);


                }
            }
        });
    }

    private void addNotesToRecyclerViewList(String noteData) {

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        year1 = year;
        month1 = monthOfYear;
        day1 = dayOfMonth;
        flag = true;
        dateLabel.setText(date);

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String time = hourOfDay + "h" + minute + "m" + second;
        if (flag == true) {
            dateOne = new Date(year1, month1, day1, hourOfDay, minute, second);
        } else {
            Toast.makeText(getApplicationContext(), "Please Choose Date first", Toast.LENGTH_SHORT).show();
        }
        timeLabel.setText(time);

    }

}
