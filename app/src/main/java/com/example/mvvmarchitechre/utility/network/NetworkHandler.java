package com.example.mvvmarchitechre.utility.network;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.example.mvvmarchitechre.utility.DialogUtil;
import com.example.mvvmarchitechre.utility.Utilities;

public class NetworkHandler {
    public static boolean isConnected;

    public static boolean isConnected() {
        return isConnected;
    }

    public static boolean isConnected(Context context) {
        if (!isConnected) {
            NetworkHandler.isConnected = Utilities.getNetworkState(context);
            if(!isConnected){
                showErrorMessage(context);
            }
        }
        return isConnected;
    }

    public static boolean isConnected(View anyView) {
        if (!isConnected) {
            if (anyView != null) {
               NetworkHandler.isConnected = Utilities.getNetworkState(anyView.getContext());
                if(!isConnected){
                    showErrorMessage(anyView.getContext());
                }
            }
        }
        return isConnected;
    }

    public static void  showErrorMessage(Context context){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DialogUtil.showNoNetworkToast(context);
            }
        }, 100);
    }
}
