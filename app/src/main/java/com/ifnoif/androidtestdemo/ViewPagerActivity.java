package com.ifnoif.androidtestdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.ifnoif.androidtestdemo.customview.VViewPager;

import java.util.HashMap;


/**
 * Created by syh on 2016/12/19.
 */

public class ViewPagerActivity extends Activity {

    private ViewPager viewPager;
    private HashMap<Integer, View> viewHashMap = new HashMap<Integer, View>();
    private PagerAdapter mAdapter = new PagerAdapter() {
        @Override
        public float getPageWidth(int position) {
            if(position==2){
                return 1;
            }
            return 0.8f;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            if (view == null) {
                return false;
            }
            return view.equals(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d("ViewPager", "syh instantiateItem position:" + position);
            View view = viewHashMap.get(position);
            if (view == null) {
                view = new Button(ViewPagerActivity.this);
                view.setBackgroundColor(0x99775566 + (int) (Math.random() * 1000000));
                ((Button) view).setText("item position:" + position);
                view.setTag("tag" + position);
                viewHashMap.put(position, view);
            }

            ViewPager.LayoutParams layoutParams = new ViewPager.LayoutParams();
            layoutParams.width = getResources().getDisplayMetrics().widthPixels;
            layoutParams.height = getResources().getDisplayMetrics().widthPixels;
            layoutParams.gravity = Gravity.CENTER;


            container.addView(view, layoutParams);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.d("ViewPager", "syh destroyItem position:" + position);
            if (object instanceof View) {
                container.removeView((View) object);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager_test);
        viewPager = (ViewPager) findViewById(R.id.view_pager2);
//        viewPager.setPageMargin(-60);

        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(2);
//        viewPager.setPageTransformer(false, new VViewPager.PageTransformer() {
//            @Override
//            public void transformPage(@NonNull View page, float position) {
//                Log.d("ViewPager","transformPage position:"+position+" page:"+page.getTag());
//                if (position < -2 || position > 2) {
//                    return; // do not need to do anything.
//                }
//
//                if (position >= -1 && position <= 1) {
//                    changeView(page, Math.abs(position));
//                } else {
//                    changeView(page, 1.0F);
//                }
//            }
//        });
        viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private static final float OFFSCREEN_PAGE_SCALE_FACTOR = 0.8F;
    private static final float OFFSCREEN_PAGE_ALPHA_FACTOR = 0.5F;

    /**
     * @param factor 0 ~ 1，0表示正常状态，1表示 out 的状态。
     */
    private static void changeView(@NonNull View view, float factor) {
        final float scaleFactor = 1.0F - ((1.0F - OFFSCREEN_PAGE_SCALE_FACTOR) * factor);
        final float alphaFactor = 1.0F - ((1.0F - OFFSCREEN_PAGE_ALPHA_FACTOR) * factor);
        view.setScaleX(scaleFactor);
        view.setScaleY(scaleFactor);
        view.setAlpha(alphaFactor);
    }


}

