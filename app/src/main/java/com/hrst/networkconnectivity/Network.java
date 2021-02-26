package com.hrst.networkconnectivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.net.URL;
import java.net.URLConnection;

public class Network {
    final private String TAG = "Network";

    final private String mUrl;
    final private int mTimeout;
    final private Handler mThreadHandler;

    public Network(String url, int timeout, Handler threadHandler) {
        mUrl = url;
        mTimeout = timeout;
        mThreadHandler = threadHandler;
    }

    /**
     * Attempt to connect to a URL on the WWW
     */
    private boolean isOnline() {
        try {
            URL u = new URL(mUrl);
            URLConnection connection = u.openConnection();
            connection.setConnectTimeout(mTimeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ping URL periodically
     */
    public Runnable ping = new Runnable() {
        @Override
        public void run() {
            Thread t = new Thread(() -> {
                Message msg = new Message();
                if (isOnline()) {
                    Log.i(TAG, "ONLINE");
                    msg.arg1 = 1;
                } else {
                    Log.i(TAG, "OFFLINE");
                    msg.arg1 = 0;

                }
                mThreadHandler.sendMessage(msg);
            });
            t.start();

            // Repeat this the same runnable code block again another 2 seconds
            // 'this' is referencing the Runnable object
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(this, 2000);
        }
    };
}
