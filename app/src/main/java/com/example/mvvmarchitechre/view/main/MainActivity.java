package com.example.mvvmarchitechre.view.main;

import android.os.Bundle;
import android.widget.Button;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.mvvmarchitechre.R;
import com.example.mvvmarchitechre.utility.UploadWorker;
import com.example.mvvmarchitechre.base.BaseActivity;
import com.example.mvvmarchitechre.view.list.RepoListFragment;

public class MainActivity extends BaseActivity {
Button btnclick;

    @Override
    protected int layoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.screenContainer, new RepoListFragment()).commit();


        final WorkManager mWorkManager = WorkManager.getInstance(getApplicationContext());
        final OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(UploadWorker.class).build();
        mWorkManager.enqueue(mRequest);

    }
}
