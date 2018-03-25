package fallenleafapps.com.tripplanner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import fallenleafapps.com.tripplanner.models.TripModel;
import fallenleafapps.com.tripplanner.models.UserModel;

/**
 * Created by mohamed hesham on 3/20/2018.
 */

public class DatabaseAdapter {

    private static DatabaseAdapter databaseAdapter;
    private DatabaseHelper databaseHelper;

    private DatabaseAdapter(Context context){
        databaseHelper=new DatabaseHelper(context);
    }

    public static DatabaseAdapter getInstance(Context context){

        if(databaseAdapter==null)
            databaseAdapter=new DatabaseAdapter(context);

        return databaseAdapter;
    }

    public void insertUser(UserModel user){

        ContentValues cv=new ContentValues();
        cv.put(DatabaseContract.UserTable.name,user.getUserName());
        cv.put(DatabaseContract.UserTable.password,user.getPassword());
        cv.put(DatabaseContract.UserTable.email,user.getEmail());

        databaseHelper.getWritableDatabase().insert(DatabaseContract.UserTable.tableName,null,cv);
    }

    public void insertTrip(TripModel trip, int userId){

        ContentValues cv=new ContentValues();
        cv.put(DatabaseContract.TripTable.name,trip.getTripName());
        cv.put(DatabaseContract.TripTable.date,trip.getTripDate().toString());
        cv.put(DatabaseContract.TripTable.status,trip.getTripStatus());

        if(trip.isTripType())
            cv.put(DatabaseContract.TripTable.type,1);
        else
            cv.put(DatabaseContract.TripTable.type,0);

        cv.put(DatabaseContract.TripTable.time,trip.getTripTime());
        cv.put(DatabaseContract.TripTable.userId,userId);

        databaseHelper.getWritableDatabase().insert(DatabaseContract.TripTable.tableName,null,cv);
    }

    public TripModel getTrip(String name){
        TripModel trip=null;

        String tripName;
        String date;
        String time;
        String start;
        String end;
        boolean type;
        int status;


        Cursor cursor=databaseHelper.getWritableDatabase().query(DatabaseContract.TripTable.tableName,null,DatabaseContract.TripTable.name+"=?",new String[]{name},null,null,null);
        if(cursor.moveToNext()){
            tripName=cursor.getString(cursor.getColumnIndex(DatabaseContract.TripTable.name));
            date=cursor.getString(cursor.getColumnIndex(DatabaseContract.TripTable.date));
            time=cursor.getString(cursor.getColumnIndex(DatabaseContract.TripTable.time));
            start=cursor.getString(cursor.getColumnIndex(DatabaseContract.TripTable.startPoint));
            end=cursor.getString(cursor.getColumnIndex(DatabaseContract.TripTable.endPoint));
            status=cursor.getInt(cursor.getColumnIndex(DatabaseContract.TripTable.status));

            if(cursor.getInt(cursor.getColumnIndex(DatabaseContract.TripTable.type))==0){
                type=false;
            }
            else{
                type=true;
            }
            trip=new TripModel(tripName,Long.parseLong(date), Long.parseLong(time), start, end, type, status);
        }

        return trip;
    }

    public UserModel getUser(String Email){

        UserModel user=null;

        int userId=-1;
        String name="";
        String email="";
        String password="";

        Cursor cursor=databaseHelper.getWritableDatabase()
                .query(DatabaseContract.UserTable.tableName,null,DatabaseContract.UserTable.email+"=?",new String[]{Email},null,null,null);


        if(cursor.moveToNext()){
            name=cursor.getString(cursor.getColumnIndex(DatabaseContract.UserTable.name));
            email=cursor.getString(cursor.getColumnIndex(DatabaseContract.UserTable.email));
            password=cursor.getString(cursor.getColumnIndex(DatabaseContract.UserTable.password));
            user=new UserModel(name,email,password);
        }

        return user;
    }

    public ArrayList<TripModel> getTrips(int userId){

        ArrayList<TripModel> result=null;
        TripModel trip=null;

        String tripName;
        String date;
        String time;
        String start;
        String end;
        boolean type;
        int status;

        Cursor cursor=databaseHelper.getWritableDatabase().query(DatabaseContract.TripTable.tableName,null,DatabaseContract.TripTable.userId+"=?",new String[]{String.valueOf(userId)},null,null,null);

        while(cursor.moveToNext())
        {
            tripName=cursor.getString(cursor.getColumnIndex(DatabaseContract.TripTable.name));
            date=cursor.getString(cursor.getColumnIndex(DatabaseContract.TripTable.date));
            time=cursor.getString(cursor.getColumnIndex(DatabaseContract.TripTable.time));
            start=cursor.getString(cursor.getColumnIndex(DatabaseContract.TripTable.startPoint));
            end=cursor.getString(cursor.getColumnIndex(DatabaseContract.TripTable.endPoint));
            status=cursor.getInt(cursor.getColumnIndex(DatabaseContract.TripTable.status));

            if(cursor.getInt(cursor.getColumnIndex(DatabaseContract.TripTable.type))==0){
                type=false;
            }
            else{
                type=true;
            }
            trip=new TripModel(tripName,Long.parseLong(date), Long.parseLong(time), start, end, type, status);
            result.add(trip);
        }
        return result;
    }

    public int getUserId(String Email){
        int id=-1;

        Cursor cursor=databaseHelper.getWritableDatabase().query(DatabaseContract.UserTable.tableName,new String[]{DatabaseContract.UserTable.id},DatabaseContract.UserTable.email+="=?",new String[]{Email},null,null,null);
        if(cursor.moveToNext())
        {
            id=cursor.getInt(cursor.getColumnIndex(DatabaseContract.UserTable.id));
        }
        return id;
    }
}
