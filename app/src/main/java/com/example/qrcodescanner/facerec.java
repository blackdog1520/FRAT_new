package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;


import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.json.JSONObject;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class facerec extends AppCompatActivity  {
    AnimationDrawable animation;
    Boolean check = true;

    //a variable to store a reference to the Surface View at the main.xml file
    private TextureView textureView;

    //a bitmap to display the captured image
    private CameraManager cameraManager;
    private String cameraId;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder captureBuilder;
    private Size ImageDimension;
    private ImageReader imageReader;
    private File file;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    //Camera variables
    //a surface holder
    private SurfaceHolder sHolder;
    //a variable to control the camera
    private Camera mCamera;
    //the camera parameters
    private Camera.Parameters parameters;
    private int request_code_perm = 100;
    private int request_code_audio = 200;


    public class ImageDownload extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                URL web = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) web.openConnection();
                InputStream stream = (InputStream) connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(stream);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;


                    data = reader.read();

                }


            } catch (Exception e) {
                e.printStackTrace();

                result = "Failed";
            }
            return result;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject json = new JSONObject(s);
                String response = json.getString("status");
                if (response.equals("200")) {
                    cameraDevice.close();
                    cameraDevice = null;
                    dialog.dismiss();

                    Log.i("response", "this is my message" + response);
                    Intent intent = new Intent(facerec.this, StartActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("subject", subjectCode);
                    startActivity(intent);
                    finish();


                } else {
                    dialog.dismiss();
                    Toast.makeText(facerec.this, "Face not recognized! Please try again", Toast.LENGTH_SHORT).show();
                    recreate();


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    CameraDevice.StateCallback stateCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;

            Log.i("cameraOpemn", "camera initialized");
            
            createCameraPreview();

        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int i) {

            cameraDevice.close();
            cameraDevice = null;

        }
    };
    StorageReference mStorageRef;
    FirebaseAuth mAuth;
    String uid, subjectCode;
    ProgressDialog dialog;

    //String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facerec);
        uid = getIntent().getStringExtra("uid");

        LottieAnimationView animationView1 = findViewById(R.id.animImagesec);

        animationView1.playAnimation();





        Log.i("cameraOpemn", "oncreate");
        subjectCode = getIntent().getStringExtra("subject");
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(facerec.this);
        if (ContextCompat.checkSelfPermission(facerec.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(facerec.this, new String[]{Manifest.permission.CAMERA}, request_code_perm);


        }else if(ContextCompat.checkSelfPermission(facerec.this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(facerec.this, new String[]{Manifest.permission.CAMERA}, request_code_audio);




        }if(ContextCompat.checkSelfPermission(facerec.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(facerec.this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED){
            textureView = findViewById(R.id.camerView);
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            assert textureView != null;

            textureView.setSurfaceTextureListener(msurfaceTextureListener);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == request_code_perm) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(ContextCompat.checkSelfPermission(facerec.this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {

                textureView = findViewById(R.id.camerView);
                cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                assert textureView != null;
                textureView.setSurfaceTextureListener(msurfaceTextureListener);
                } else {

                    ActivityCompat.requestPermissions(facerec.this, new String[]{Manifest.permission.RECORD_AUDIO}, request_code_audio);
                }

            } else {

                ActivityCompat.requestPermissions(facerec.this, new String[]{Manifest.permission.CAMERA}, request_code_perm);
            }

        }
        if(requestCode == request_code_audio){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(facerec.this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                    textureView = findViewById(R.id.camerView);
                    cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                    assert textureView != null;
                    textureView.setSurfaceTextureListener(msurfaceTextureListener);
                } else {

                    ActivityCompat.requestPermissions(facerec.this, new String[]{Manifest.permission.CAMERA}, request_code_perm);
                }

            }
            else {

                ActivityCompat.requestPermissions(facerec.this, new String[]{Manifest.permission.RECORD_AUDIO}, request_code_audio);}
        }











        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void runcamera() {

        if (cameraDevice == null) {
            Log.i("cameraOpemn", "camera device null runcammera");
            Toast.makeText(facerec.this, "Please try again!", Toast.LENGTH_SHORT).show();
            return;
        }
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.EFFECT_TICK);
        try {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            Log.i("cameraOpemn", "runcamera1");

            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);

            }
            int width = 640;
            int height = 480;
            if (jpegSizes != null && jpegSizes.length > 0) {

                    Log.i("jpeg sizes", "" + jpegSizes.length);


                    width = jpegSizes[(int)jpegSizes.length/2].getWidth();
                    height = jpegSizes[(int)jpegSizes.length/2].getHeight();


            }
            Log.i("cameraOpemn", "run camera2");

            imageReader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);

            List<Surface> outputSurface = new ArrayList<>(2);
            outputSurface.add(imageReader.getSurface());
            outputSurface.add(new Surface(textureView.getSurfaceTexture()));
            captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(imageReader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            Log.i("cameraOpemn", "run camera3");


            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    Log.i("cameraOpemn", "image available");


                    Image image = null;
                    try {
                        image = imageReader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        // send this bytes to db

                        mStorageRef = FirebaseStorage.getInstance().getReference("Verify").child("users");
                        Log.i("cameraOpemn", "callback");
                        Toast.makeText(facerec.this, "Hold on Please!", Toast.LENGTH_SHORT).show();


                        final StorageReference imageName = mStorageRef.child("User:" + mAuth.getCurrentUser().getUid());


                        imageName.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                dialog.dismiss();
                                dialog.setMessage("Verifying...");
                                ImageDownload task = new ImageDownload();
                                try {
                                    dialog.show();
                                    task.execute("http://fratsvapi-env.eba-fufsc9vp.us-east-2.elasticbeanstalk.com/loadImg?Image-Url=" + mAuth.getCurrentUser().getUid());
                                } catch (Exception e) {
                                    dialog.dismiss();
                                    e.printStackTrace();
                                }




                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(facerec.this, "Photo upload Unsuccessful", Toast.LENGTH_LONG).show();
                                ;
                            }
                        });
                    } finally {
                        if (image != null)
                            image.close();
                    }
                }
            };
            imageReader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Log.i("Capture Photo", "capturing photo");


                    createCameraPreview();
                }
            };

            cameraDevice.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    try {
                        cameraCaptureSession.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                        Log.i("cameraOpemn", "camera device configure");

                    } catch (CameraAccessException e) {

                    }
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                }
            }, mBackgroundHandler);

        } catch (CameraAccessException e) {

        }


        //sv = (SurfaceView) findViewById(R.id.camerView);

        //Get a surface
        //sHolder = sv.getHolder();

        //add the callback interface methods defined below as the Surface View callbacks
        //sHolder.addCallback(this);

        //tells Android that this surface will have its data constantly replaced
        //sHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            Log.i("cameraOpemn", "camera preview");
            Toast.makeText(facerec.this, "camera preview", Toast.LENGTH_SHORT).show();

            assert texture != null;
            texture.setDefaultBufferSize(ImageDimension.getWidth(), ImageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                    if (cameraDevice == null)
                        return;
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview();
                    Log.i("cameraOpemn", "after update[review");
                    Toast.makeText(facerec.this, "Click On the animation to start", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    public void updatePreview() {
        if (cameraDevice == null) {
            Toast.makeText(this, "Error in updating preview", Toast.LENGTH_SHORT).show();
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            try {
                Log.i("cameraOpemn", "repeating request");
                cameraCaptureSessions.setRepeatingRequest(captureBuilder.build(), null, mBackgroundHandler);

            } catch (CameraAccessException e) {
                e.printStackTrace();
            }

        }


    }

    public void openCamera() {
        try {
            cameraId = cameraManager.getCameraIdList()[1];
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            Log.i("cameraOpemn", "open camera");


            ImageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.

                return;
            }
            Log.i("cameraOpemn", "opencamera starting");
            cameraManager.openCamera(cameraId, stateCallBack, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


    }







    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    TextureView.SurfaceTextureListener msurfaceTextureListener = new TextureView.SurfaceTextureListener() {
       @Override
       public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
           Log.i("cameraOpemn", "texture view");
        openCamera();

       }

       @Override
       public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

       }

       @Override
       public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
           return false;
       }

       @Override
       public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

       }
   };
public void clickImage(View view){
    runcamera();

}
}
