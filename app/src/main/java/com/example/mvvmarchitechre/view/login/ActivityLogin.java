package com.example.mvvmarchitechre.view.login;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.room.Room;

import com.example.mvvmarchitechre.R;
import com.example.mvvmarchitechre.databinding.ActivitySigninBinding;
import com.example.mvvmarchitechre.db.AppDatabase;
import com.example.mvvmarchitechre.db.User;
import com.example.mvvmarchitechre.db.UserDao;
import com.example.mvvmarchitechre.model.Person;
import com.example.mvvmarchitechre.view.main.MainActivity;
import com.example.mvvmarchitechre.utility.Utilities;
import com.example.mvvmarchitechre.utility.dialog.DialogMultiLanguage;
import com.example.mvvmarchitechre.utility.permission.PermissionResultCallBack;
import com.example.mvvmarchitechre.utility.permission.PermissionUtils;
import com.example.mvvmarchitechre.utility.session.SessionManager;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class ActivityLogin extends AppCompatActivity implements View.OnClickListener, PermissionResultCallBack {
    Activity mActivity;
    ActivitySigninBinding mBinding;
    String etEmail, password;
    PermissionUtils permissionUtils;
    private static final int REQUEST_CODE = 200;
    public ArrayList<String> alForPermission;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = ActivityLogin.this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_signin);

        permissionUtils = new PermissionUtils(this);
        allPermission();

        mBinding.btnLogin.setOnClickListener(this);
        mBinding.btnImageUpload.setOnClickListener(this);
        mBinding.etForgotPass.setOnClickListener(this);
        mBinding.tvlanguage.setOnClickListener(this);

        db= AppDatabase.getInstance(getApplicationContext());

  }

    // Get the firebase notification
   /* public void showpushNotification() {
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {
            try {
                String tempObj = extras.getString("data");
                FCMResponse response = new Gson().fromJson(tempObj, FCMResponse.class);

                String notificationType = response.getNotificationType();
                String status = response.getNotificationSubType();
                int orderId = response.getKey1();
                isPushNotification = "yes";
                if (notificationType.equals("Order")) {
                    SessionManager.get().setOrderId(orderId);
                    hitOrderDetailApi();
                } else {
                    openActivity();
                }
            } catch (Exception e) {
                openActivity();
            }
        } else {
            openActivity();
        }
    }*/

    private void openActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ActivityLogin.this, MainActivity.class));
            }
        }, 100);
    }


    public void openLanguageDialog() {
        DialogMultiLanguage dialog = new DialogMultiLanguage(mActivity);
        dialog.setMultiLanguageListner(new DialogMultiLanguage.MultiLanguageListner() {
            @Override
            public void onSelectedLanguage(String language) {
            }
        });
    }

    public void allPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alForPermission = new ArrayList<>();
            alForPermission.add(READ_EXTERNAL_STORAGE);
            alForPermission.add(WRITE_EXTERNAL_STORAGE);
            permissionUtils.check_permission(alForPermission, getString(R.string.premission_des), REQUEST_CODE);
        }
    }

    @Override
    public void PermissionGranted(int request_code) {
        mBinding.btnImageUpload.setVisibility(View.VISIBLE);
//        getLastLocation();
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
//        Toast.makeText(mActivity, "permission PartialPermissionGranted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void PermissionDenied(int request_code) {
//        Toast.makeText(mActivity, "PermissionDenied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void NeverAskAgain(int request_code) {
//        Toast.makeText(mActivity, "NeverAskAgain", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length <= 0) {
        } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mBinding.btnImageUpload.setVisibility(View.VISIBLE);
        } else {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if (checkValidation()) {
                    Person person = new Person();
                    person.setEmail(etEmail);
                    person.setPassword(password);
                    mBinding.setPerson(person);

                    User user = new User();
                    user.setUid(getRandomNumber(10,100));
                    user.setFirstName(etEmail);
                    user.setLastName("xyz");

                    // Store data in shared prefrence
                    SessionManager.get().setUserEmail(person.getEmail());
                    SessionManager.get().setUserPassword(person.getPassword());

                    // Store data in Room database
                    UserDao userDao = db.userDao();
                    userDao.insertAll(user);

                    openActivity();
                }
                break;

            case R.id.btnImageUpload:
                openbottomSheetDialog();
                break;

            case R.id.tvlanguage:
                openLanguageDialog();
                break;

            case R.id.etForgotPass:
                break;
        }
    }

    public static int getRandomNumber(int min, int max) {
        return (new Random()).nextInt((max - min) + 1) + min;
    }


    private boolean checkValidation() {
        etEmail = mBinding.etName.getText().toString();
        password = mBinding.etPass.getText().toString().trim();

        if (etEmail.length() == 0) {
            mBinding.etName.setError("Empty email address");
            mBinding.etName.requestFocus();
            return false;
        } else if (!(Utilities.isValidEmailId(etEmail))) {
            mBinding.etName.setError("Enter valid email address");
            mBinding.etName.requestFocus();
            return false;
        } else if (password.length() == 0) {
            mBinding.etPass.setError("Required password");
            mBinding.etPass.requestFocus();
            return false;
        } else if (!Utilities.isValidPassword(mBinding.etPass)) {
            mBinding.etPass.setError("Please enter valid password");
            mBinding.etPass.requestFocus();
            return false;
        } else
            return true;
    }

    public void openbottomSheetDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this.mActivity, R.style.SheetDialog);
        View view = View.inflate(mActivity, R.layout.dialog_camera_gallery_view, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);

        LinearLayout  ll_gallery = dialog.findViewById(R.id.ll_gallery);
        LinearLayout ll_camera = dialog.findViewById(R.id.ll_camera);
        AppCompatImageView iv_cross = dialog.findViewById(R.id.iv_cross);

        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialog.dismiss(); }});


        ll_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ImagePicker.with(mActivity).cameraOnly()
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        ll_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ImagePicker.with(mActivity).galleryOnly()
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String filename = uri.getLastPathSegment();
            mBinding.ivProfilepic.setImageURI(uri);
        }

        else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }




}