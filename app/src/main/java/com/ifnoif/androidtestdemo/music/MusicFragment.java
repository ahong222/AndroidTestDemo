package com.ifnoif.androidtestdemo.music;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifnoif.androidtestdemo.BaseFragment;
import com.ifnoif.androidtestdemo.R;

import java.io.IOException;

/**
 * Created by shen on 17/4/23.
 */

public class MusicFragment extends BaseFragment {

    private MediaPlayer mediaPlayer = null;
    private boolean prepared = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_fragment, container, false);//
        view.findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        view.findViewById(R.id.playTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTest();
            }
        });

        view.findViewById(R.id.pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });

        view.findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
            }
        });

        view.findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });


        mediaPlayer = new MediaPlayer();

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "onPrepared");
                prepared = true;
                mp.start();
            }
        });

        return view;
        // super.onCreateView(inflater, container, savedInstanceState);
    }

    public void play() {


        if (prepared) {
            mediaPlayer.reset();

            try {
                mediaPlayer.setDataSource(getContext(), Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.ordersound1));
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.prepareAsync();
        } else {
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(getContext(), Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.ordersound1));
            } catch (IOException e) {
                e.printStackTrace();
            }

            mediaPlayer.prepareAsync();
        }
    }


    private void playTest() {
        mediaPlayer.start();

    }

}
