package com.example.mvvmarchitechre.utility.permission;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PermissionUtils
{
    Context context;
    Activity current_activity;

    PermissionResultCallBack permissionResultCallback;

    ArrayList<String> permission_list=new ArrayList<>();
    ArrayList<String> listPermissionsNeeded=new ArrayList<>();
    String dialog_content="";
    int req_code;
    ArrayList<String> pending_permissions;

    public PermissionUtils(Context context)
    {
        this.context=context;
        this.current_activity= (Activity) context;
        permissionResultCallback= (PermissionResultCallBack) context;
    }

    /**
     * Check the API Level & Permission
     *
     * @param permissions
     * @param dialog_content
     * @param request_code
     */

    public void check_permission(ArrayList<String> permissions, String dialog_content, int request_code)
    {
        this.permission_list=permissions;
        this.dialog_content=dialog_content;
        this.req_code=request_code;

       if(Build.VERSION.SDK_INT >= 23)
       {
           if (checkAndRequestPermissions(permissions, request_code))
           {
               permissionResultCallback.PermissionGranted(request_code);
               Log.i("all permissions", "granted");
               Log.i("proceed", "to callback");
           }
       }
        else
       {
           permissionResultCallback.PermissionGranted(request_code);

           Log.i("all permissions", "granted");
           Log.i("proceed", "to callback");
       }
    }


    /**
     * Check and request the Permissions
     *
     * @param permissions
     * @param request_code
     * @return
     */

    private  boolean checkAndRequestPermissions(ArrayList<String> permissions, int request_code) {

        if(permissions.size()>0)
        {
            listPermissionsNeeded = new ArrayList<>();
            for(int i=0;i<permissions.size();i++)
            {
                int hasPermission = ContextCompat.checkSelfPermission(current_activity,permissions.get(i));

                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permissions.get(i));
                }

            }

            if (!listPermissionsNeeded.isEmpty())
            {
                ActivityCompat.requestPermissions(current_activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),request_code);
                return false;
            }
        }

        return true;
    }

    /**
     *
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public  void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 200:
                 if(grantResults.length>0)
                 {
                     Map<String, Integer> perms = new HashMap<>();

                     for (int i = 0; i < permissions.length; i++)
                     {
                         perms.put(permissions[i], grantResults[i]);
                     }

                    pending_permissions=new ArrayList<>();

                     for (int i = 0; i < listPermissionsNeeded.size(); i++)
                     {
                         if (perms.get(listPermissionsNeeded.get(i)) != PackageManager.PERMISSION_GRANTED)
                         {
                            if(ActivityCompat.shouldShowRequestPermissionRationale(current_activity,listPermissionsNeeded.get(i)))
                                    pending_permissions.add(listPermissionsNeeded.get(i));
                            else
                            {
                                Log.i("Go to settings","and enable permissions");
                                permissionResultCallback.NeverAskAgain(req_code);
//                                Toast.makeText(current_activity, "Go to settings/app permissions and enable permissions", Toast.LENGTH_LONG).show();
//
//                                Intent intent = new Intent();
//                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                intent.setData(uri);
//                                context.startActivity(intent);
//                                return;
                            }
                         }
                     }

                     if(pending_permissions.size()>0)
                     {
//                         alertDialogCall();

//                         showMessageOKCancel(dialog_content,
//                                 new DialogInterface.OnClickListener() {
//                                     @Override
//                                     public void onClick(DialogInterface dialog, int which) {
//
//                                         switch (which) {
//                                             case DialogInterface.BUTTON_POSITIVE:
//                                                 check_permission(permission_list,dialog_content,req_code);
//                                                 break;
//                                             case DialogInterface.BUTTON_NEGATIVE:
//                                                 Log.i("permisson","not fully given");
//                                                 if(permission_list.size()==pending_permissions.size())
//                                                     permissionResultCallback.PermissionDenied(req_code);
//                                                 else
//                                                     permissionResultCallback.PartialPermissionGranted(req_code,pending_permissions);
//                                                 break;
//                                         }
//
//
//                                     }
//                                 });

                     }
                     else
                     {
                        Log.i("all","permissions granted");
                        Log.i("proceed","to next step");
                           permissionResultCallback.PermissionGranted(req_code);

                     }
                 }
                 break;
        }
    }



    private void alertDialogCall() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(current_activity);
        alertDialogBuilder.setMessage("Are you Sure! you want to remove all items from the cart?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        check_permission(permission_list,dialog_content,req_code);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("permisson", "not fully given");
                if (permission_list.size() == pending_permissions.size()) {
                    permissionResultCallback.PermissionDenied(req_code);
                } else {
                    permissionResultCallback.PartialPermissionGranted(req_code, pending_permissions);

                }
            }
        });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
