package fallenleafapps.com.tripplanner.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mohamed hesham on 3/18/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    private DatabaseHelper databaseHelper;
    private static final String DATABASE_NAME="trip.db";
    private static final int DATABASE_VERSION=1;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    public DatabaseHelper getInstance(Context context){
        if(databaseHelper==null){
            databaseHelper=new DatabaseHelper(context);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String UserTableQuery="CREATE TABLE"+DatbaseContract.UserTable.tableName+" ("+
                                DatbaseContract.UserTable.id+" INTEGER PRIMARY KEY AUTOINCREMENT "+
                                DatbaseContract.UserTable.name+" TEXT , "+
                                DatbaseContract.UserTable.email+" TEXT , "+
                                DatbaseContract.UserTable.password+" TEXT);";
        sqLiteDatabase.execSQL(UserTableQuery);

        String TripTableQuery="CREATE TABLE"+ DatbaseContract.TripTable.tableName+" ("+
                DatbaseContract.TripTable.id+" INTEGER PRIMARY KEY AUTOINCREMENT "+
                DatbaseContract.TripTable.name+" TEXT , "+
                DatbaseContract.TripTable.date+" TEXT , "+
                DatbaseContract.TripTable.time+" TEXT , "+
                DatbaseContract.TripTable.status+" INTEGER , "+
                DatbaseContract.TripTable.type+" INTEGER , ";
        sqLiteDatabase.execSQL(UserTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
