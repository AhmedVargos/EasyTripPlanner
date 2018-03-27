package fallenleafapps.com.tripplanner.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.util.Random;

import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.ui.broadcastReceiver.BootReceiver;
import fallenleafapps.com.tripplanner.ui.services.TripIntentService;

import static android.content.Context.ALARM_SERVICE;

public class Functions {

    //Used to change the main fragment in the activity.
    public static void changeMainFragment(FragmentActivity fragmentActivity, Fragment fragment){
        fragmentActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container,fragment)
                .commit();
    }




    //Check if there are internet connection any type.
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        // Here if condition check for wifi and mobile network is available or not.
        // If anyone of them is available or connected then it will return true,
        // otherwise false;

        if (wifi != null && wifi.isConnected()) {
            return true;
        } else if (mobile != null && mobile.isConnected()) {
            return true;
        }
        return false;
    }

    //Check if there are internet connection WIFI.
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifi != null && wifi.isConnected()) {
            return true;
        }

        return false;
    }

    //Check if there are internet connection Data.
    public static boolean isMobileDataConnected(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mobile != null && mobile.isConnected()) {
            return true;
        }

        return false;
    }
    //Used to scale a bitmap with code.
    public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }


    //schedule Alarm for the trip
    public static void scheduleAlarm(Context context, String tripName) {
        AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, TripIntentService.class);
        intent.putExtra("TRIP_NAME",tripName);
        Random r = new Random(); // make a random number of the request code
        int min = 1, max = 10000;
        int requestCode = r.nextInt(max - min + 1) + min;
        PendingIntent pendingIntent = PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //TODO Schedule it with the trip real time
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 5000,
                    pendingIntent);
        else
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + 5000,
                    pendingIntent);


        //Enable the receiver to start working
        ComponentName componentName = new ComponentName(context, BootReceiver.class);
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, // or  COMPONENT_ENABLED_STATE_DISABLED
                PackageManager.DONT_KILL_APP);

    }


    public void unschedulePendingIntent(Context myContext) {
        Intent myIntent = new Intent(myContext, TripIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(myContext, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) myContext.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
