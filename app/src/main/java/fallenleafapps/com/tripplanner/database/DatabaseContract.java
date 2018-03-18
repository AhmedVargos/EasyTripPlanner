package fallenleafapps.com.tripplanner.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by mohamed hesham on 3/18/2018.
 */

public class DatabaseContract {

    public static final String CONTENT_AUTHORITY="fallenleafapps.com.tripplanner";
    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);

    public static class UserTable implements BaseColumns{

        public static String tableName="USERSX";
        public static String id=BaseColumns._ID;
        public static String name="name";
        public static String email="email";
        public static String password="password";
    }

    public static class TripTable implements BaseColumns{

        public static String tableName="Trips";
        public static String id=BaseColumns._ID;
        public static String name="name";
        public static String date="date";
        public static String time="time";
        public static String status="status";
        public static String type="type";
    }

}
