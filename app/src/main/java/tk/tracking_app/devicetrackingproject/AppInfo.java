package tk.tracking_app.devicetrackingproject;

import android.location.Location;

/**
 * Created by asus on 12.11.2017.
 */

public class AppInfo {
    private static String response;
    private static Location location;
    private static int showMenuNumber;
    private static String showMenuOwner;

    public static String getShowMenuOwner() {
        return showMenuOwner;
    }

    public static void setShowMenuOwner(String showMenuOwner) {
        AppInfo.showMenuOwner = showMenuOwner;
    }

    public static int getShowMenuNumber() {
        return showMenuNumber;
    }

    public static void setShowMenuNumber(int showMenuNumber) {
        AppInfo.showMenuNumber = showMenuNumber;
    }

    public static Location getLocation() {

        return location;
    }

    public static void setLocation(Location location) {
        AppInfo.location = location;
    }

    public static String getResponse() {

        return response;
    }

    public static void setResponse(String response) {
        AppInfo.response = response;
    }
}
