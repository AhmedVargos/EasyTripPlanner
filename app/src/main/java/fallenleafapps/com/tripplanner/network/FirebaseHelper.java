package fallenleafapps.com.tripplanner.network;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import fallenleafapps.com.tripplanner.models.NoteModel;
import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.models.UserModel;

/**
 * Created by mohamed hesham on 3/27/2018.
 */

public class FirebaseHelper {

    private static FirebaseHelper firebaseHelper;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private ArrayList<TripModel> tripsList;

    private FirebaseHelper(String userId){

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("data");

        tripsList=new ArrayList<>();

        mFirebaseDatabase.child("trips").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {

                    TripModel trip = dataSnapshot1.getValue(TripModel.class);
                    tripsList.add(trip);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("hello","failed");
            }
        });
    }

    public static FirebaseHelper getInstance(String userId){
        if(firebaseHelper==null){
            firebaseHelper=new FirebaseHelper(userId);
        }
        return firebaseHelper;
    }

    public DatabaseReference getFirebaseDatabase(){
        return mFirebaseDatabase;
    }

    public void addUser(UserModel user,String userId){
       mFirebaseDatabase.child("users").child(userId).setValue(user);
    }

    public void addTrip(TripModel trip,String userId){
        String tripId=mFirebaseDatabase.push().getKey();
        mFirebaseDatabase.child("trips").child(userId).child(tripId).setValue(trip);
    }
    public void addTrips(ArrayList<TripModel> trips,String userId){

        for(TripModel t:trips){
            addTrip(t,userId);
        }
    }

}
