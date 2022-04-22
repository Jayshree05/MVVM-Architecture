package com.example.mvvmarchitechre.utility.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.mvvmarchitechre.utility.Utilities;


public class NetworkChangeReceiver extends BroadcastReceiver {

    /**
     * The constant connectivityReceiverListener.
     */
    public static ConnectivityReceiverListener connectivityReceiverListener;

    /**
     * Instantiates a new Network change receiver.
     */
    public NetworkChangeReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NetworkHandler.isConnected = Utilities.getNetworkState(context);
        if (connectivityReceiverListener != null)
            connectivityReceiverListener.onNetworkConnectionChanged(NetworkHandler.isConnected);
    }


    /**
     * The interface Connectivity receiver listener.
     */
    public interface ConnectivityReceiverListener {
        /**
         * This method is invoked bu receiver when internet connection enables or disables.
         *
         * @param isConnected network connectivity status.
         */
        void onNetworkConnectionChanged(boolean isConnected);
    }

}
