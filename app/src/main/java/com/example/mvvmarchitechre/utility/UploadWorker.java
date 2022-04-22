package com.example.mvvmarchitechre.utility;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import javax.inject.Provider;

public class UploadWorker extends Worker {

   public UploadWorker(
       @NonNull Context context,
       @NonNull WorkerParameters params) {
       super(context, params);
   }

   @Override
   public Result doWork() {

     // Do the work here--in this case, upload the images.
      uploadData();

     // Indicate whether the work finished successfully with the Result
     return Result.success();
   }

    private void uploadData() {
        System.out.println("===upload data when network back===");
    }
}
