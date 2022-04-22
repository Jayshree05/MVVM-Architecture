package com.example.mvvmarchitechre.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.mvvmarchitechre.R;
import com.google.firebase.BuildConfig;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vdurmont.semver4j.Semver;

import org.jsoup.Jsoup;

import java.util.List;

import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {
    AlertDialog mAlertDialog;
    FirebaseAnalytics mFirebaseAnalytics;
    int playStoreInstalled = -1;
    String sAppPackageName;



    @LayoutRes
    protected abstract int layoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutRes());
        ButterKnife.bind(this);


        if (BuildConfig.FLAVOR.equals("development")) {
            Bundle params = new Bundle();
            mFirebaseAnalytics.logEvent("is_dev_env", params);
Log.e("Build", "development");
        } else if (BuildConfig.FLAVOR.equals("staging")) {
            Bundle params = new Bundle();
            mFirebaseAnalytics.logEvent("is_stag_env", params);
            Log.e("Build", "staging");
        } else if (BuildConfig.FLAVOR.equals("production")) {
            Bundle params = new Bundle();
            mFirebaseAnalytics.logEvent("is_prod_env", params);
            Log.e("Build", "production");
        } else {
        }

        if (!isAppPlayStoreGenuine(sAppPackageName)) {
            Log.e("App download play store", "" + isAppPlayStoreGenuine());
        } else {
            Bundle params = new Bundle();
            mFirebaseAnalytics.logEvent("appstore_user", params);
            Log.e("App download play store", "" + isAppPlayStoreGenuine());
        }
    }



    public boolean isPlayStoreInstalled() {
        // Check it only once, is the play store installed?
        // NOTICE: At first check this.playStoreInstalled is initialized with -1
        if (playStoreInstalled < 0) {
            // Because playstore it's name has changed, we must check for both
            String sPlayStorePackageNameOld = sAppPackageName;
            String sPlayStorePackageNameNew = sAppPackageName;
            String sPackageName = "";

            PackageManager packageManager = getApplication().getPackageManager();
            List<PackageInfo> packages = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
            for (PackageInfo packageInfo : packages) {
                sPackageName = packageInfo.packageName;
                //this.debug( sPackageName );
                if (sPackageName.equals(sPlayStorePackageNameOld) || sPackageName.equals(sPlayStorePackageNameNew)) {
                    playStoreInstalled = 1;
                    break;
                }
            }
        }
        return (this.playStoreInstalled > 0);
    }

    public boolean isAppPlayStoreGenuine(String sPackageName) {
        // If there is no playstore available, it is impossible that the app
        // is genuine installed via the playstore.
        if (!isPlayStoreInstalled()) {
            return false;
        }

        String sInstallerName = this.getInstallerPackageName(sPackageName);
        return (sInstallerName != null && sInstallerName.length() > 0);
    }

    public boolean isAppPlayStoreGenuine() // Check current app is properly/official genuine installed
    {
        return (isDebugVersion() || isAppPlayStoreGenuine(null));
    }

    public String getInstallerPackageName(String sPackageName) {
        String sInstallerName = "";
        try {
            if (sPackageName == null || sPackageName.length() == 0) {
                sPackageName = getPackageName();
            }
            PackageManager packageManager = getApplication().getPackageManager();
            sInstallerName = packageManager.getInstallerPackageName(sPackageName);
        } catch (Exception e) {
        }

        return sInstallerName;
    }

    public boolean isDebugVersion() {
// NOTICE: To make this functional, specify a debug buildType in the build.gradle file
        try {
            // Get the applicationId specified in the build.gradle file
            sAppPackageName = getPackageName();
            return sAppPackageName.endsWith(".debug");
        } catch (Exception e) {
        }
        return true;
    }

    public void checkAppUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String newVersion1;
                    String curVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                    String newVersion = curVersion;
                    newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en")
                            .timeout(30000)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .referrer("http://www.google.com")
                            .get()
                            .select("div.hAyfc:nth-child(4) .IQ1z0d .htlgb")
                            .first()
                            .ownText();

                    if (newVersion.equals("3.0")) {
                        newVersion1 = newVersion + ".0";
                    } else {
                        newVersion1 = newVersion;
                    }

                    Log.e("Curr Version", curVersion);
                    Log.e("New Version", newVersion1);

                    Semver sem = new Semver(newVersion1);
                    if (sem.isLowerThan(curVersion)) {
                        String finalNewVersion = newVersion;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                showAlertDialog(finalNewVersion, getResources().getString(R.string.app_upgrade_message), getResources().getString(R.string.update), false);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).
                start();
    }

    public void showAlertDialog(String ver, String text, String buttonText, boolean isCancelable) {
        if (text == null)
            text = "";

        text = "Please update to the new App with" + ver + "! Thank you for using";
        mAlertDialog = new AlertDialog.Builder(getApplicationContext())
                .setMessage(text)
                /*.setTitle(mContext.getString(R.string.app_name))*/
                .setCancelable(isCancelable)
                .setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String appPackageName = getPackageName();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        dialog.dismiss();
                    }
                }).create();

        mAlertDialog.show();
        Button button = mAlertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        button.setTextColor(getResources().getColor(R.color.purple_200));

    }

}