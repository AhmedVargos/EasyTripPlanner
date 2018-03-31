package fallenleafapps.com.tripplanner.ui.activities;


import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.NoteModel;
import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.network.FirebaseHelper;
import fallenleafapps.com.tripplanner.ui.adapters.AddingNotesAdapter;
import fallenleafapps.com.tripplanner.utils.ConstantsVariables;
import fallenleafapps.com.tripplanner.utils.Functions;

import static fallenleafapps.com.tripplanner.R.color.colorPrimary;

public class AddTripActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.input_name)
    TextInputEditText inputName;
    @BindView(R.id.btn_add_trip)
    Button btnAddTrip;
    @BindView(R.id.dateBtn)
    Button dateBtn;
    @BindView(R.id.timeBtn)
    Button timeBtn;
    @BindView(R.id.noteText)
    TextInputEditText noteText;
    //@BindView(R.id.roundTrip)
    //CheckBox roundTrip;
    @BindView(R.id.addNoteBtn)
    Button addNoteBtn;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.include_toolbar)
    Toolbar includeToolbar;
    @BindView(R.id.btn_cancel_trip)
    AppCompatButton btnCancelTrip;
    @BindView(R.id.roundTrip)
    CheckBox roundTrip;
    @BindView(R.id.cardView)
    CardView cardView;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    Date dateOne;
    String date, tripName;
    String startLat, endLat;
    String startLong, endLong;
    String startLocationName;
    String endLocationName;


    int year1;
    int month1;
    int day1;
    boolean flag = false, isFlag = false,startPlaceSelected = false,endPlaceSelected = false,isDateChosen = false,isTimeChosen = false;
    boolean tripType;
    Long correctDate , correctTime;

    AddingNotesAdapter addingNotesAdapter;
    List<NoteModel> noteModels;
    NoteModel noteModel;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    LinearLayoutManager verticalLayout;
    TripModel tripModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);

        ///////////////////// UI ////////////////
        // set button underlined
        addNoteBtn.setPaintFlags(addNoteBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //include bar title
        includeToolbar.setTitle(R.string.addTrip);
        includeToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        includeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //////////////////////////////////////////


        noteModels = new ArrayList<>();
        recyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());


        ///////////////// date and time part
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
                dpd.setAccentColor(getResources().getColor(colorPrimary));


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
                tpd.setAccentColor(getResources().getColor(colorPrimary));

            }
        });
        ///////////////////////////// end of date and time

        ///////////////////////////// autoComplete Search by Google
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

                startLat = Double.toString(latLng.latitude);
                startLong = Double.toString(latLng.longitude);
                startLocationName = (String) place.getName();
                startPlaceSelected = true;

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
                LatLng latLng = place.getLatLng();

                Log.i("h", "Place: " + place.getName());
                endLat = Double.toString(latLng.latitude);
                endLong = Double.toString(latLng.longitude);
                endLocationName = (String) place.getName();
                endPlaceSelected = true;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("i", "An error occurred: " + status);
            }
        });
        /////////////////////////////////// end of autoComplete Section

        //////////////////////////////////// notes Section
        addNoteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String noteData = String.valueOf(noteText.getText());
                if (!noteData.isEmpty()) {

                    addNotesToRecyclerViewList(noteData);
                    isFlag = true;

                    addingNotesAdapter = new AddingNotesAdapter(AddTripActivity.this, noteModels);
                    verticalLayout = new LinearLayoutManager(AddTripActivity.this, LinearLayoutManager.VERTICAL, false);
                    recycleView.setLayoutManager(verticalLayout);

                    recycleView.setAdapter(addingNotesAdapter);
                    noteText.setText("");

                }
            }
        });
        btnAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tripName = inputName.getText().toString();
                Random r = new Random(); // make a random number of the trip id
                int min = 1, max = 62000;
                int trip1Id = r.nextInt(max - min + 1) + min; //id

                // validation
                if(!inputName.getText().toString().isEmpty() && startPlaceSelected && endPlaceSelected && isDateChosen && isTimeChosen ){


                    if(roundTrip.isChecked()){
                        tripType = true;
                    }else{
                        tripType = false;
                    }
                    tripModel = new TripModel(trip1Id,tripName,correctDate ,correctTime,startLat,startLong,startLocationName,endLat,endLong,endLocationName,tripType, ConstantsVariables.TRIP_UPCOMMING_STATE,noteModels);
                    FirebaseHelper.getInstance().addTrip(tripModel, FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Functions.scheduleAlarm(AddTripActivity.this, tripModel);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Fill all data please", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnCancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    ///////////// notes
    private void addNotesToRecyclerViewList(String noteData) {
        noteModel = new NoteModel();
        noteModel.setBody(noteData);
        noteModels.add(noteModel);

    }
    ////////////////////////

    ///////////////// date and time
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        year1 = year;
        month1 = monthOfYear;
        day1 = dayOfMonth;
        flag = true;
        isDateChosen = true;
        dateBtn.setText(date);

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String time = hourOfDay + ":" + minute + ":" + second;
        if (flag == true) {
            long currentTime = Calendar.getInstance().getTime().getTime();
            dateOne = getDate(year1,month1,day1,hourOfDay,minute,second);

            Log.e("Add trip", "onTimeSet: " + currentTime );
            Log.e("Add trip", "Selected date " + dateOne.getTime() );
            Log.e("Add trip", "diff : " +  (dateOne.getTime()-currentTime)  );

            if (currentTime < dateOne.getTime()) {
                //add to trip object
                correctDate = dateOne.getTime();
                correctTime = dateOne.getTime();
                isTimeChosen = true;

            } else {
                Toast.makeText(getApplicationContext(), "Choose valid date", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(), "Please Choose Date first", Toast.LENGTH_SHORT).show();
        }

        timeBtn.setText(time);

    }

    public static Date getDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    //////////////////////// end of date and time
}

