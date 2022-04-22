
package com.example.mvvmarchitechre.utility;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mvvmarchitechre.R;


public class DialogUtil {
    // get a blocking progress dialog.
    public static ProgressDialog getProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    /**
     * Static method to get an create of material styled progress bar
     *
     * @param context Context of the current class
     * @param resId   Resource Id of the progress bar
     * @return An create of MaterialProgressBar
     */
    public static ProgressBar getProgressBarInstance(Context context, int resId) {
        ProgressBar nonBlockingProgressBar = ((Activity) context).getWindow().findViewById(resId);
        return nonBlockingProgressBar;
    }

    public static ProgressBar getProgressBarInstance(View view, int resId) {
        if (view != null) {
            ProgressBar nonBlockingProgressBar = view.findViewById(resId);
            return nonBlockingProgressBar;
        }
        return null;
    }

    public static void showToastInfo(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showToastSuccess(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public static void showToastError(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public static void showToast(Context context, String msg) {
        showToastError(context, context.getString(R.string.success));
    }

    public static void showNoNetworkToast(Context context) {
        showToastError(context, context.getString(R.string.no_internet_error_msg));
    }

}
