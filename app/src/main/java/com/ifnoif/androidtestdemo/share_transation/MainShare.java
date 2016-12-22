package com.ifnoif.androidtestdemo.share_transation;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ifnoif.androidtestdemo.R;

import java.util.List;

/**
 * Created by syh on 2016/9/21.
 */
public class MainShare extends AppCompatActivity {

    public static int[] drawableArray = new int[]{R.drawable.item_img_1, R.drawable.item_img_2, R.drawable.item_img_3, R.drawable.item_img_4, R.drawable.item_img_5, R.drawable.item_img_6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTransition();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_share_layout);

        initRecycleView();


        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
                Log.d("syh", "setEnterSharedElementCallback onSharedElementStart");

            }
        });




    }

    private int position =0 ;
    private void setTransition() {
        if (Build.VERSION.SDK_INT < 21) {
            return;
        }
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

    }

    RecyclerView recyclerView;
    public void initRecycleView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new ItemAdapter());
    }
    public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> implements View.OnClickListener {

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_item_layout, parent, false);

            ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) view.getLayoutParams();
            layoutParams.height = getResources().getDisplayMetrics().widthPixels*3/4;
            view.setLayoutParams(layoutParams);

            view.findViewById(R.id.play).setOnClickListener(this);
            view.findViewById(R.id.jump).setOnClickListener(this);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.itemView.findViewById(R.id.play).setTag(position);
            ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.image);
            imageView.setImageResource(drawableArray[position]);
        }

        @Override
        public int getItemCount() {
            return drawableArray.length;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.play:
                    Object object = v.getTag();
                    if (object instanceof Integer) {
                        final LinearLayout progressBar = getPlayerLayout(MainShare.this);
                        if (progressBar.getParent() != null) {
                            ((ViewGroup) progressBar.getParent()).removeView(progressBar);
                        }

                        ((ViewGroup)((ViewGroup) v.getParent()).findViewById(R.id.video_container)).addView(progressBar);
                        position = (Integer) object;
                    }
                    break;
                case R.id.jump:
                    Log.d("syh","start TargetShare");
                    Intent intent = new Intent(MainShare.this, TargetShare.class);
                    Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainShare.this, (View)v.getParent(), "share").toBundle();
                    intent.putExtra("position", position);
                    startActivity(intent, bundle);
                    break;
            }

        }
    }

    private Bitmap bitmap = null;
    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        bitmap = data.getParcelableExtra("bitmap");
        getWindow().getSharedElementReenterTransition().addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                Log.d("syh", "onTransitionStart");
                addPlayerLayout();
                getWindow().getSharedElementReenterTransition().removeListener(this);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                Log.d("syh", "onTransitionEnd");
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        super.onActivityReenter(resultCode, data);
        Log.d("syh","onActivityReenter");
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    private static LinearLayout sPlayerLayout;

    public static LinearLayout getPlayerLayout(Activity activity) {
        if (sPlayerLayout == null) {
            sPlayerLayout = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.player_layout, null);
            WebView webView = ((WebView) sPlayerLayout.findViewById(R.id.webview));
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
            webView.loadUrl("http://m.baidu.com");
        }
        return sPlayerLayout;
    }

    public void onSharedElementReturnStart() {
        Log.d("syh", "MainShare onSharedElementStart");


    }

    public void addPlayerLayout() {

        View playerLayout = MainShare.getPlayerLayout(MainShare.this);
        if (playerLayout.getParent() != null) {
            ((ViewGroup) playerLayout.getParent()).removeView(playerLayout);
        }
        View view = recyclerView.findViewWithTag(position);

        ((ViewGroup)((ViewGroup) view.getParent()).findViewById(R.id.video_container)).addView(playerLayout);

        Log.d("syh", "MainShare onSharedElementReturnEnd");
    }


}
