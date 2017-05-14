package com.ifnoif.androidtestdemo;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;


/**
 * Created by apple on 17-2-20.
 */

public class VolleyFragment extends BaseFragment {

//    @BindView(R.id.image_view)
    ImageView mImageView;

//    @BindView(R.id.load)
    Button mLoad;

//    @BindView(R.id.load2)
    Button mLoad2;

    private String url = "http://bpic.588ku.com/element_origin_pic/16/08/22/07e5f03085b48eebb42c24fb090fc258.png?_upd=true";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.volley_layout, container, false);

//        ButterKnife.bind(this, view);
        mImageView = (ImageView)view.findViewById(R.id.image_view);
        mLoad = (Button)view.findViewById(R.id.load);
        mLoad2 = (Button)view.findViewById(R.id.load2);

        init();

        mImageView.setImageResource(R.mipmap.ic_launcher);
        return view;
    }

    private void init(){
        mLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickLoad(view);
            }
        });
        mLoad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickLoad2(view);
            }
        });
    }

//    @OnClick(R.id.load)
    public void clickLoad(View view) {
        ImageLoader imageLoader = VolleyManager.getInstance().getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Log.d("volley", "onResponse");
                if (response.getBitmap() != null) {
                    mImageView.setImageBitmap(response.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley", "onErrorResponse");
            }
        }, 0, 0, ImageView.ScaleType.CENTER);
    }

//    @OnClick(R.id.load2)
    public void clickLoad2(View view) {
        ImageLoader imageLoader = VolleyManager.getInstance().getImageLoader();

        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Log.d("volley", "onResponse2");
                if (response.getBitmap() != null) {
                    mImageView.setImageBitmap(response.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley", "onErrorResponse 2");
            }
        }, 200, 200, ImageView.ScaleType.CENTER);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
