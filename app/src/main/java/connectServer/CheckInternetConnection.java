package connectServer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Lenovo on 04-Dec-15.
 */
public class CheckInternetConnection {
    private Context context;
    public CheckInternetConnection(Context cont){
        context=cont;
    }
    public Boolean isMobileInternetConnected(){
        //we create object for ConnectivityManager class that returns network related information
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //If connectivity object is not null
        if (connectivity != null) {
            //Get network info - Mobile internet access
            NetworkInfo info = connectivity.getActiveNetworkInfo();//ConnectivityManager.TYPE_MOBILE

            if (info != null) {
                //Look for whether device is currently connected to Mobile internet
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isWifiInternetConnected(){
        //we create object for ConnectivityManager class that returns network related information
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //If connectivity object is not null
        if (connectivity != null) {
            //Get network info - Wifi internet access
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (info != null) {
                //Look for whether device is currently connected to Mobile internet
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
}
