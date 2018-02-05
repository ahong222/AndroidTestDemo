package com.ifnoif.androidtestdemo.camera;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.ifnoif.androidtestdemo.BaseFragment;
import com.ifnoif.androidtestdemo.R;

import java.util.List;

/**
 * Created by shen on 17/8/28.
 */

public class CameraViewFragment extends BaseFragment {

    SurfaceView mSurfaceView;
    Camera mCameraDevice = null;
    boolean isFront = true;

    @Override
    public int getContentResource() {
        return R.layout.camera_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPreview();
            }
        });

        mSurfaceView = (SurfaceView) view.findViewById(R.id.surface);
    }

    private void startPreview() {
        int i = 0;

        if (mCameraDevice == null) {
            try {
                if (isFront) {
                    mCameraDevice = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                } else {
                    mCameraDevice = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            try {
//                if (DeviceVideoInfo.isHorizontal) {
//                    mCameraDevice.setDisplayOrientation(0);
//                } else {
//                    mCameraDevice.setDisplayOrientation(90);
//                }
                Camera.Parameters localParameters = mCameraDevice.getParameters();
                localParameters.setPreviewFormat(ImageFormat.NV21);
                List<Camera.Size> list = mCameraDevice.getParameters().getSupportedPreviewSizes();
                localParameters.setPreviewSize(list.get(7).width, list.get(7).height);
                mCameraDevice.setParameters(localParameters);
                mCameraDevice
                        .setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
                            @Override
                            public void onPreviewFrame(byte[] data,
                                                       Camera camera) {
                                if (data == null)
                                    return;
//                                camera.addCallbackBuffer(data);
                                try {

                                    Log.e(TAG,"onPreviewFrame size:"+data.length);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                mCameraDevice.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera) {
                        if (data == null)
                            return;
//                        camera.addCallbackBuffer(data);
                        try {

                            Log.e(TAG,"onPreviewFrame size2:"+data.length);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                mCameraDevice.setPreviewDisplay(mSurfaceView.getHolder());
                this.mCameraDevice.startPreview();

            } catch (Exception localThrowable) {
                localThrowable.printStackTrace();
            }

        } else {
        }

    }

}
