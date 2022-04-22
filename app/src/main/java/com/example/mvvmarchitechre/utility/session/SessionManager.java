package com.example.mvvmarchitechre.utility.session;

import android.app.Application;
import com.example.mvvmarchitechre.base.BaseApplication;


public class SessionManager {
    private static SessionManager sInstance;
    private final PreferenceUtil pref;
    String IS_LOGGED_IN = "is_logged_in";
    String UserEmail = "UserEmail";
    String Password = "Password";
    String language = "language";


    private SessionManager(Application application) {
        pref = new PreferenceUtil(application);
    }

    public static SessionManager get() {
        init(BaseApplication.getInstance());
        return sInstance;
    }


    public static void init(Application application) {
        if (sInstance == null) {
            sInstance = new SessionManager(application);
        }
    }

    public void clear() {
        pref.clear();
    }


    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN);
    }

    public void setLoggedIn(boolean b) {
        pref.setBooleanData(IS_LOGGED_IN, b);
    }

    public String getUserEmail() {
        return pref.getStringData(UserEmail);
    }

    public void setUserEmail(String email) {
        pref.setStringData(UserEmail, email);
    }

    public String getUserPassword() {
        return pref.getStringData(Password);
    }

    public void setUserPassword(String password) {
        pref.setStringData(Password, password);
    }


    public String getLanguage() {
        return pref.getStringData(language);
    }

    public void setLanguage(String lang) {
        pref.setStringData(language, lang);
    }

}