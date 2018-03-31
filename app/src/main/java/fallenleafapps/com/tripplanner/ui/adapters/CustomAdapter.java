package fallenleafapps.com.tripplanner.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.NoteModel;
import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.network.FirebaseHelper;

/**
 * Created by mohamed hesham on 3/30/2018.
 */

public class CustomAdapter extends ArrayAdapter {


    TripModel trip;

    public CustomAdapter(@NonNull Context context, List<NoteModel> list, TripModel trip) {
        super(context, 0,list);
        this.trip=trip;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView=convertView;

        if (listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

       final  NoteModel n;
       n= (NoteModel) getItem(position);

        TextView txt=listItemView.findViewById(R.id.note_id);
        txt.setText(n.getBody());

        CheckBox checkBox=listItemView.findViewById(R.id.check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                   n.setFinished(b);
                   Log.i("hello",n.getBody());
                FirebaseHelper.getInstance().updateTrip(trip,FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        });

        return listItemView;
    }
}
