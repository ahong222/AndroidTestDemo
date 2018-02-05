package com.ifnoif.androidtestdemo.glide;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ifnoif.androidtestdemo.BaseFragment;
import com.ifnoif.androidtestdemo.R;

/**
 * @author shen create on 2018/2/1.
 */

public class GlideFragment extends BaseFragment {
    @Override
    public int getContentResource() {
        return R.layout.glide_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView2 = (ImageView)view.findViewById(R.id.image_view2);
                Glide.with(getContext()).load(Uri.parse("https://gss1.bdstatic.com/9vo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike92%2C5%2C5%2C92%2C30/sign=c4931b2f2334349b600b66d7a8837eab/94cad1c8a786c9179e80a80cc23d70cf3bc75700.jpg"))
                        .apply(RequestOptions.skipMemoryCacheOf(true))
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                        .apply(RequestOptions.overrideOf(300,0))
                        .into(imageView2);

//                ImageView imageView = (ImageView)view.findViewById(R.id.image_view);
//                Glide.with(getContext()).load(Uri.parse("https://gss1.bdstatic.com/9vo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike92%2C5%2C5%2C92%2C30/sign=c4931b2f2334349b600b66d7a8837eab/94cad1c8a786c9179e80a80cc23d70cf3bc75700.jpg"))
//                        .apply(RequestOptions.overrideOf(0,0))
//                        .into(new SimpleTarget<Drawable>() {
//                            @Override
//                            public void onResourceReady(@NonNull Drawable drawable, @Nullable Transition<? super Drawable> transition) {
//                                System.out.println("syh width2:"+drawable.getIntrinsicWidth());
//                                imageView.setImageDrawable(drawable);
//                            }
//                        });

            }
        });
    }
}
