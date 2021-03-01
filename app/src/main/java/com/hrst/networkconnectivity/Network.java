package com.hrst.networkconnectivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.net.URL;
import java.net.URLConnection;

public class Network {
    final private String TAG = "Network";

    final private String mUrl;
    final private int mTimeout;
    final private Handler mThreadHandler;

    private boolean mWasOnline = false;

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
                    if (!mWasOnline) {
                        msg.arg1 = 1;
                        mThreadHandler.sendMessage(msg);
                    }
                    mWasOnline = true;
                } else {
                    Log.i(TAG, "OFFLINE");
                    if (mWasOnline) {
                        msg.arg1 = 0;
                        mThreadHandler.sendMessage(msg);
                    }
                    mWasOnline = false;
                }
            });
            t.start();

            // Repeat this the same runnable code block again
            // 'this' is referencing the Runnable object
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(this, mTimeout + 500);
        }
    };
}
