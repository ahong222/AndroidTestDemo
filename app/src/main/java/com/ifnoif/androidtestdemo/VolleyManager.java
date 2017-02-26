package com.ifnoif.androidtestdemo;

import android.graphics.Bitmap;
import android.os.Environment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

import java.io.File;

/**
 * Created by apple on 17-2-20.
 */

public class VolleyManager {
    private static VolleyManager instance;

    public static VolleyManager getInstance() {
        if (instance == null) {
            instance = new VolleyManager();
        }
        return instance;
    }

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    private VolleyManager() {
        mRequestQueue = new RequestQueue(new DiskBasedCache(new File(Environment.getExternalStorageDirectory(), "demo")), new BasicNetwork(new HurlStack()));
        mRequestQueue.start();
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}
