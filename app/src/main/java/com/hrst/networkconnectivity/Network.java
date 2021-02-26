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

    private final String mUrl;
    private final int mTimeout;

    public Network(String url, int timeout) {
        mUrl = url;
        mTimeout = timeout;
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
                if (isOnline()) {
                    Log.i(TAG, "ONLINE");
                    MainActivity.this.handler.sendMessage("ONLINE");
                } else {
                    Log.i(TAG, "OFFLINE");
                }
            });
            t.start();

            // Repeat this the same runnable code block again another 2 seconds
            // 'this' is referencing the Runnable object
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(this, 2000);
        }
    };
}
