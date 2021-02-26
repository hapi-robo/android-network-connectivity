package com.hrst.networkconnectivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

// Ref: https://guides.codepath.com/android/Repeating-Periodic-Tasks
// https://androidcookbook.com/Recipe.seam?recipeId=921&title=Sending%20messages%20between%20threads%20using%20activity%20thread%20queue%20and%20Handler%20class
// https://techtej.blogspot.com/2011/02/android-passing-data-between-main.html
// https://www.dev2qa.com/android-thread-message-looper-handler-example/
public class MainActivity extends AppCompatActivity {
    final private String TAG = "MainActivity";

    // Start the initial runnable task by posting through the handler
    public Handler mHandler = new Handler(Looper.getMainLooper())  {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 1) {
                Log.i(TAG, "ONLINE");
            } else {
                Log.i(TAG, "OFFLINE");
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Network network = new Network("https://www.google.com", 2000, mHandler);
        mHandler.post(network.ping);
    }
}