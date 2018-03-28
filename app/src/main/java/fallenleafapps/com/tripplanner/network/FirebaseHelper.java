package fallenleafapps.com.tripplanner.network;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.models.UserModel;

/**
 * Created by mohamed hesham on 3/27/2018.
 */

public class FirebaseHelper {

    private static FirebaseHelper firebaseHelper;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private FirebaseHelper(String userId){

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");


        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel user=dataSnapshot.getValue(UserModel.class);
                List<TripModel> trips= (List<TripModel>) dataSnapshot.child("trips").getValue(TripModel.class);
                if(user!=null)
                    Log.i("hello","xxxxx");
               // Log.i("hello",user.getUserName());
                Log.i("hello",user.getEmail());

                if(trips!=null)
                    Log.i("hello","xssx");
                Log.i("hello",trips.get(0).getStartLocationName());
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

    public void addUser(UserModel user,String userId){
       mFirebaseDatabase.child(userId).setValue(user);
    }
    public void addTrip(TripModel trip,String userId){
        String tripId=mFirebaseDatabase.push().getKey();
        mFirebaseDatabase.child("trips").child(tripId).setValue(trip);
    }

}
