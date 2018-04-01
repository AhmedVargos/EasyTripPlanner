package fallenleafapps.com.tripplanner.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dd.morphingbutton.MorphingButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.ui.listeners.TripCardListener;
import fallenleafapps.com.tripplanner.utils.ConstantsVariables;

/**
 * Created by Vargos on 20-Mar-18.
 */

public class TripRecyclerAdapter extends RecyclerView.Adapter<TripRecyclerAdapter.TripViewHolder>{
    Context context;
    List<TripModel> tripsList;
    TripCardListener tripCardListener;
    int type;
    int lastPosition = -1;


    public  TripRecyclerAdapter(Context context, List<TripModel> tripsList, TripCardListener tripCardListener, int type){
        this.context = context;
        this.tripsList = tripsList;
        this.tripCardListener = tripCardListener;
        this.type = type;
    }
    public  TripRecyclerAdapter(Context context, TripCardListener tripCardListener, int type){
        this.context = context;
        this.tripsList = new ArrayList<>();
        this.tripCardListener = tripCardListener;
        this.type = type;
    }
    public void addNewTrip(TripModel tripModel){
        tripsList.add(tripModel);
        notifyItemInserted(tripsList.size() - 1);
    }
    public void removeTrip(TripModel tripModel,int pos){
        tripsList.remove(tripModel);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos,tripsList.size());
    }
    public void removeTripWithoutPos(TripModel tripModel){
        int pos = -1;
        for (int i=0; i < tripsList.size(); i++){
           if( tripsList.get(i).getTripFirebaseId().equals(tripModel.getTripFirebaseId())){
               pos = i;
           }
        }

        if(pos != -1){
            tripsList.remove(pos);
        }
        notifyDataSetChanged();
    }
    public void clearListOfTrips(){
        tripsList.clear();
        notifyDataSetChanged();
    }

    public void changeTrip(TripModel tripModel){
        int pos = -1;
        for (int i=0; i < tripsList.size(); i++){
            if( tripsList.get(i).getTripFirebaseId().equals(tripModel.getTripFirebaseId())){
                pos = i;
            }
        }

        if(tripModel.getTripStatus() == ConstantsVariables.TRIP_DONE_STATE){

            tripsList.remove(pos);
        }else{
            if(pos != -1){ //To check if it's a past trip or upcoming one
                tripsList.set(pos,tripModel);
            }else{
                addNewTrip(tripModel);
            }
        }

        notifyDataSetChanged();
    }
    @Override
    public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trip_card, parent,false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TripViewHolder holder, final int position) {
        final TripModel tripModel = tripsList.get(position);
        if (type != 0){
            holder.startTrip.setVisibility(View.GONE);
            holder.startTrip.setEnabled(false);
        }
        holder.tripTitle.setText(tripModel.getTripName());
        SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
        String dateString = formatterDate.format(new Date(tripModel.getTripDate()));
        holder.tripDate.setText(dateString);

        Glide.with(context)
                .load(R.drawable.tripimage)
                .centerCrop()
                .into(holder.tripImage);

        holder.startTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    tripCardListener.startTrip(tripModel,holder.startTrip);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.deleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripCardListener.deleteTrip(tripModel,holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripCardListener.openTripDetails(tripModel);
            }
        });

        if(tripModel.getTripStatus() == ConstantsVariables.TRIP_STARTED_STATE){
            MorphingButton.Params circle = MorphingButton.Params.create()
                    .duration(500)
                    .cornerRadius(112) // 56 dp
                    .width(112) // 56 dp
                    .height(112) // 56 dp
                    .color(context.getResources().getColor(R.color.colorAccent)) // normal state color
                    .colorPressed(context.getResources().getColor(R.color.colorPrimaryDark)) // pressed state color
                    .icon(R.drawable.ic_navigation_black_24dp); // icon
            holder.startTrip.morph(circle);
            holder.startTrip.setEnabled(false);
        }else{
            MorphingButton.Params square = MorphingButton.Params.create()
                    .duration(10)
                    .cornerRadius(4)
                    .width(250)
                    .height(112)
                    .color(context.getResources().getColor(R.color.colorPrimary))
                    .colorPressed(context.getResources().getColor(R.color.colorPrimaryDark))
                    .text("Start");
            holder.startTrip.morph(square);
            holder.startTrip.setEnabled(true);

        }

        if(position >lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.up_from_bottom);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public void onViewDetachedFromWindow(TripViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }

    public class TripViewHolder extends RecyclerView.ViewHolder {
        ImageView tripImage;
        ImageButton deleteTrip;
        TextView tripTitle;
        TextView tripDate;
        MorphingButton startTrip;
        public TripViewHolder(View itemView) {
            super(itemView);

            tripImage = itemView.findViewById(R.id.item_trip_image_background);
            tripTitle = itemView.findViewById(R.id.item_trip_title);
            tripDate = itemView.findViewById(R.id.item_trip_date);
            startTrip = itemView.findViewById(R.id.item_trip_start);
            deleteTrip = itemView.findViewById(R.id.item_trip_delete);

        }

        public void deleteTrip(){
            tripsList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(),tripsList.size());

        }

    }
}
