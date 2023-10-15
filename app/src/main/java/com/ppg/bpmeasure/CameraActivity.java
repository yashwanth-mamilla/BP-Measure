package com.ppg.bpmeasure;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.camera2.*;
import android.hardware.camera2.params.OutputConfiguration;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.ppg.bpmeasure.Details.UserInfo;
import com.ppg.bpmeasure.Math.Fft;
import com.ppg.bpmeasure.Math.ImageProcessing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("ALL")
public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "HeartRateMonitor";
        private static final int RED = 1;
        private static final int BLUE = 2;
        private static final int GREEN = 3;

        private static final AtomicBoolean processing = new AtomicBoolean(false);
        private SurfaceView surfaceView = null;
        private static SurfaceHolder surfaceHolder = null;
        private static PowerManager.WakeLock wakeLock = null;
        private CameraCaptureSession captureSession = null;

        private ProgressBar progess;
        private int progresscount;
        private Camera cameraDevice;

        UserInfo userInfo;


    private int numberOfFrames =0;
    private int null_frames=0;
    public ArrayList<Double> avgIntensityList = new ArrayList<Double>();
    private static long timer;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        userInfo=(UserInfo) getIntent().getSerializableExtra("UserInfo");

        progess =(ProgressBar) findViewById(R.id.Progess_Bar);
        progess.setProgress(0);
        progess.setMax(1200);
        progess.setMin(0);

        captureSession=new CameraCaptureSession() {
            @NonNull
            @Override
            public CameraDevice getDevice() {
                return null;
            }

            @Override
            public void prepare(@NonNull Surface surface) throws CameraAccessException {

            }

            @Override
            public void finalizeOutputConfigurations(List<OutputConfiguration> outputConfigs) throws CameraAccessException {

            }

            @Override
            public int capture(@NonNull CaptureRequest request, @Nullable CaptureCallback listener, @Nullable Handler handler) throws CameraAccessException {
                return 0;
            }

            @Override
            public int captureBurst(@NonNull List<CaptureRequest> requests, @Nullable CaptureCallback listener, @Nullable Handler handler) throws CameraAccessException {
                return 0;
            }

            @Override
            public int setRepeatingRequest(@NonNull CaptureRequest request, @Nullable CaptureCallback listener, @Nullable Handler handler) throws CameraAccessException {
                return 0;
            }

            @Override
            public int setRepeatingBurst(@NonNull List<CaptureRequest> requests, @Nullable CaptureCallback listener, @Nullable Handler handler) throws CameraAccessException {
                return 0;
            }

            @Override
            public void stopRepeating() throws CameraAccessException {

            }

            @Override
            public void abortCaptures() throws CameraAccessException {

            }

            @Override
            public boolean isReprocessable() {
                return false;
            }

            @Nullable
            @Override
            public Surface getInputSurface() {
                return null;
            }

            @Override
            public void close() {

            }
        };

        surfaceView=(SurfaceView) findViewById(R.id.preview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                cameraDevice.setPreviewCallback(previewCallback);

                try {
                    cameraDevice.setPreviewDisplay(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                Camera.Parameters parameters = cameraDevice.getParameters();
                Camera.Size preview_size = null;

                for (Camera.Size size : parameters.getSupportedPreviewSizes())
                {
                    if (size.width <= width && size.height <= height)
                    {
                        if (preview_size != null) {
                            int prevArea = preview_size.width * preview_size.height;
                            int newArea = size.width * size.height;
                            if ( prevArea > newArea)
                                preview_size = size;
                        } else {
                            preview_size = size;
                        }
                    }
                }

                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                parameters.setPreviewSize(preview_size.width,preview_size.height);
                cameraDevice.setParameters(parameters);
                cameraDevice.startPreview();

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        });
        WindowManager.LayoutParams layoutParams=new WindowManager.LayoutParams();
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock =powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"WakeUp");


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity2.class);
        i.putExtra("UserInfo", userInfo);
        startActivity(i);
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.release();
        cameraDevice.setPreviewCallback(null);
        cameraDevice.stopPreview();
        cameraDevice.release();
        cameraDevice = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
        cameraDevice = Camera.open();
        cameraDevice.setDisplayOrientation(90);
        timer=System.currentTimeMillis();

    }

    private Camera.PreviewCallback previewCallback=new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {

            Camera.Size preview_size = camera.getParameters().getPreviewSize();

            Double redIntensity;
            Double blueIntensity;
            Double greenIntensity;
            Double AvgIntensity;

            if(preview_size!=null && data!=null && processing.compareAndSet(false, true)){

                numberOfFrames++;
                Log.d("CameraActivity","No of Frames so far "+String.valueOf(numberOfFrames));
                redIntensity = ImageProcessing.decodeYUV420SPtoRedBlueGreenAvg(data.clone(), preview_size.height, preview_size.width,RED);
                blueIntensity = ImageProcessing.decodeYUV420SPtoRedBlueGreenAvg(data.clone(), preview_size.height, preview_size.width,BLUE);
                greenIntensity = ImageProcessing.decodeYUV420SPtoRedBlueGreenAvg(data.clone(), preview_size.height, preview_size.width,GREEN);
                AvgIntensity = (redIntensity+greenIntensity)/2;

                avgIntensityList.add(AvgIntensity);

                if(redIntensity<200){
                    // reset progress bar
                    progresscount=0;
                    numberOfFrames=0;
                    timer=System.currentTimeMillis();
                    avgIntensityList.clear();
                    progess.setProgress(0);
                    processing.set(false);
                    return;

                }

                long end_time = System.currentTimeMillis();

                Double TimePeriod = Double.valueOf((end_time-timer))/1000;

                int systole = 0, diastole = 0;
                int BPM=0;
                if( TimePeriod >= 45 || progess.getProgress()==progess.getMax()){

                    Double[] array =avgIntensityList.toArray(new Double[avgIntensityList.size()]);
                    Double samplingRate= (numberOfFrames/TimePeriod);

                    Double freq = Fft.FFT(array,numberOfFrames,samplingRate);
                    BPM = (int) Math.ceil(freq*60);

                    Double cardiac_output = userInfo.gender == UserInfo.MALE ? 5 : 4.5;


                    double ET =userInfo.ejectionTime;

                    if(ET==0)
                        ET=(364.5 - 1.23 * BPM);

                    Double resistance= userInfo.ROB;

                    if(resistance==0)
                        resistance=18.5;

                    double BSA = 0.007184*(Math.pow(userInfo.height, 0.725))*(Math.pow(userInfo.weight, 0.425));

                    double StrokeVolume = -6.6 + (0.25 * (ET - 35)) - (0.62 * BPM) + (40.4 * BSA) - (0.51 * userInfo.age);

                    double pulsePressure = StrokeVolume / ((0.013 * userInfo.weight - 0.007 * userInfo.age - 0.004 * BPM) + 1.307);

                    double meanArterialPressure = cardiac_output * resistance;



                    systole = (int) (meanArterialPressure+ (2*pulsePressure)/3);

                    diastole = (int) (meanArterialPressure-(pulsePressure)/3);
                }
                if(systole!=0 && diastole!=0){
                    Intent intent=new Intent(CameraActivity.this,Result.class);
                    intent.putExtra("UserInfo",userInfo);
                    intent.putExtra("Systole",systole);
                    intent.putExtra("Diastole",diastole);
                    intent.putExtra("HeartRate",BPM);
                    Log.d("CameraActivity","Null Frames so far "+null_frames);
                    startActivity(intent);
                    finish();
                }

                if(redIntensity!=0)
                progess.setProgress(numberOfFrames);
                Log.d("CameraActivity","Progress Value = "+progess.getProgress());
                processing.set(false);
            }
            else{
                null_frames++;
                Log.d("CameraActivity","Null Frames so far "+null_frames);
            }
        }
    };

}