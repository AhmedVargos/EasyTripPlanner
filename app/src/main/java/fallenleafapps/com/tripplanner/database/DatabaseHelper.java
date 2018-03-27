package fallenleafapps.com.tripplanner.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by mohamed hesham on 3/18/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    private static DatabaseHelper databaseHelper;
    private static final String DATABASE_NAME="trip.db";
    private static final int DATABASE_VERSION=1 ;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context){
        if(databaseHelper==null){
            databaseHelper=new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("hello","datbase created");

        String UserTableQuery="CREATE TABLE "+ DatabaseContract.UserTable.tableName+" ("+
                                DatabaseContract.UserTable.id+" INTEGER PRIMARY KEY AUTOINCREMENT , "+
                                DatabaseContract.UserTable.name+" TEXT , "+
                                DatabaseContract.UserTable.email+" TEXT UNIQUE , "+
                                DatabaseContract.UserTable.password+" TEXT);";
        sqLiteDatabase.execSQL(UserTableQuery);

        String TripTableQuery="CREATE TABLE "+ DatabaseContract.TripTable.tableName+" ("+
                DatabaseContract.TripTable.id+" INTEGER PRIMARY KEY AUTOINCREMENT , "+
                DatabaseContract.TripTable.name+" TEXT UNIQUE , "+
                DatabaseContract.TripTable.date+" TEXT , "+
                DatabaseContract.TripTable.time+" TEXT , "+
                DatabaseContract.TripTable.status+" INTEGER , "+
                DatabaseContract.TripTable.type+" INTEGER , " +
                DatabaseContract.TripTable.userId+" INTEGER );" ;
        sqLiteDatabase.execSQL(TripTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query1="DROP TABLE "+DatabaseContract.UserTable.tableName+";";
        String query2="DROP TABLE "+DatabaseContract.TripTable.tableName+";";

        sqLiteDatabase.execSQL(query1);
        sqLiteDatabase.execSQL(query2);
    }
}
