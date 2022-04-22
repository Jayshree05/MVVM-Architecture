package com.example.mvvmarchitechre.utility;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.EditText;

import com.example.mvvmarchitechre.utility.session.SessionManager;

import java.util.Locale;
import java.util.regex.Pattern;

public class Utilities {
    private static final int PASS_MIN_VALUE = 5;
    private static final int PASS_MAX_VALUE = 20;
    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_ARABIC = "ar";
    public static final String LANGUAGE_CHINESE = "zh";
    public static String selectedLanguage;

//    public static String returnMeFCMtoken() {
//        if (NetworkHandler.isConnected()) {
//            final String[] token = {""};
//            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//                @Override
//                public void onComplete(@NonNull Task<String> task) {
//                    if (task.isComplete()) {
//                        token[0] = task.getResult();
//                        Log.e("AppConstants", "onComplete: new Token got: " + token[0]);
//                        SessionManager.get().setFirebaseToken(token[0]);
//                    }
//                }
//            });
//            return token[0];
//        }
//        return "";
//    }

    public static boolean isValidPassword(final EditText editText) {
        String val = editText.getText().toString();
        if (!TextUtils.isEmpty(val)) {
            if (val.length() < PASS_MIN_VALUE) {
                return false;
            } else if (val.length() > PASS_MAX_VALUE) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static boolean isValidEmailId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public static void getApplicationLanguage(Activity mActivity) {
        selectedLanguage = SessionManager.get().getLanguage();
        if (selectedLanguage.equals(LANGUAGE_ENGLISH)) {
            Utilities.setApplicationlanguage(mActivity, LANGUAGE_ENGLISH);
        } else {
            if (selectedLanguage.isEmpty()) {
                Utilities.setApplicationlanguage(mActivity, LANGUAGE_ENGLISH);
            } else {
                Utilities.setApplicationlanguage(mActivity, LANGUAGE_ARABIC);
            }

        }
    }

    public static void setApplicationlanguage(Activity activity, String language) {
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            conf.setLocale(new Locale(language)); // API 17+ only.
        } else {
            conf.locale = new Locale(language);
        }
        try {
            res.updateConfiguration(conf, dm);
            SessionManager.get().setLanguage(language);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getNetworkState(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
