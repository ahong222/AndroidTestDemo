package com.ifnoif.androidtestdemo.share_transation;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.app.SharedElementCallback;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifnoif.androidtestdemo.R;

import java.util.List;

/**
 * Created by syh on 2016/9/21.
 */
public class TargetShare extends Activity {
    private int position = 0;
    private FrameLayout shareLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.target_share_layout);
        ActivityCompat.requestPermissions(TargetShare.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        shareLayout = (FrameLayout)findViewById(R.id.share);
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) shareLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels*3/4;
        findViewById(R.id.share).setLayoutParams(layoutParams);

        position = getIntent().getIntExtra("position",0);
        ((ImageView)findViewById(R.id.image)).setImageResource(MainShare.drawableArray[position]);

        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;
                ((ImageView)findViewById(R.id.image)).setImageResource(MainShare.drawableArray[position]);
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            setEnterSharedElementCallback(new SharedElementCallback() {
                @Override
                public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                    super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
                    Log.d("syh", "onSharedElementStart");

                    View playerLayout = MainShare.getPlayerLayout(TargetShare.this);
                    if (playerLayout.getParent() != null) {
                        ((ViewGroup) playerLayout.getParent()).removeView(playerLayout);
                    }
                    shareLayout.addView(playerLayout);

                }
            });
        }


//        Slide slideIn = new Slide();
//        slideIn.setDuration(500);
////设置为进入
//        slideIn.setMode(Visibility.MODE_IN);
////设置从右边进入
//        slideIn.setSlideEdge(android.view.Gravity.RIGHT);
//        getWindow().setEnterTransition(slideIn);

        initRecycleView();
    }



    public void initRecycleView(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new ItemAdapter());
    }
    public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView view = new TextView(parent.getContext());

            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120));
            view.setBackgroundColor(Color.GRAY);
            view.setGravity(Gravity.CENTER);

            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int position) {
            holder.itemView.setTag(position);
            ((TextView)holder.itemView).setText("position:"+position);
        }

        @Override
        public int getItemCount() {
            return 20;
        }


    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void finishAfterTransition() {
        Log.d("syh","finishAfterTransition");
        Intent data = new Intent();
        View playerLayout = MainShare.getPlayerLayout(TargetShare.this);


//        Bitmap  bitmap = Bitmap.createBitmap(playerLayout.getWidth(),playerLayout.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas  =new Canvas(bitmap);
//        ((View)playerLayout.getParent()).draw(canvas);
//        try {
//            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(new File(Environment.getExternalStorageDirectory(),"test.jpg")));
//            Log.d("syh","finishAfterTransition save bitmap success");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//
//        Log.d("syh","finishAfterTransition bitmapHeight:"+bitmap.getHeight()+" viewHeight:"+playerLayout.getHeight());
//
//        data.putExtra("bitmap",bitmap);
        setResult(RESULT_OK, data);
        super.finishAfterTransition();
    }

//    @Override
//    public void onBackPressed() {
////        super.onBackPressed();
//
//        WebView playerLayout = MainShare.getPlayerLayout(TargetShare.this);
//        Bitmap  bitmap = Bitmap.createBitmap(playerLayout.getWidth(),playerLayout.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas  =new Canvas(bitmap);
//        ((View)playerLayout.getParent()).draw(canvas);
//        try {
//            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(new File(Environment.getExternalStorageDirectory(),"test.jpg")));
//            Log.d("syh","finishAfterTransition save bitmap success");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("syh","onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("syh","onDestroy");
    }

    @Override
    public void finish() {
        Log.d("syh","finish");
        super.finish();
    }

    @Override
    public void onBackPressed() {
        Log.d("syh","onBackPressed");
        super.onBackPressed();
    }
}
