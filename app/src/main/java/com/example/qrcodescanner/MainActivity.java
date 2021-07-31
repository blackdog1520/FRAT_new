package com.example.qrcodescanner;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    Button register,login;
    LottieAnimationView spinner;
    FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!= null){
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView im = findViewById(R.id.cloundImgFirst);
        spinner = findViewById(R.id.SunImage);
        mAuth = FirebaseAuth.getInstance();
        spinner.playAnimation();
        ImageView im2 = findViewById(R.id.CloudImgSecond);
        ImageView birdone = findViewById(R.id.imageView2);
        ImageView birdtwo = findViewById(R.id.imageView3);
        ImageView birdthree = findViewById(R.id.imageView4);
        ImageView birdfour = findViewById(R.id.birdthree);
        ImageView birdfive = findViewById(R.id.birdfour);

        final Animation animUpDown,animUpDown2,anim1,anim2,anim3,anim4,anim5;
        animUpDown2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_translation_sec);
        anim1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_rotate);
        birdone.startAnimation(anim1);
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation.setRepeatMode(ValueAnimator.REVERSE);
                animation.setRepeatCount(ValueAnimator.INFINITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_rotate_four);
        birdtwo.startAnimation(anim2);
        anim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation.setRepeatMode(ValueAnimator.REVERSE);
                animation.setRepeatCount(ValueAnimator.INFINITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim3 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_rotate_three);
        birdthree.startAnimation(anim3);
        anim4 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_rotate_two);
        birdfour.startAnimation(anim4);
        anim5 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_rotate);
        birdfive.startAnimation(anim5);
        // start the animation
        im2.startAnimation(animUpDown2);
        animUpDown2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation.setRepeatMode(ValueAnimator.REVERSE);
                animation.setRepeatCount(ValueAnimator.INFINITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // load the animation
        animUpDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_translation);

        // start the animation
        im.startAnimation(animUpDown);
        animUpDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            animation.setRepeatMode(ValueAnimator.REVERSE);
            animation.setRepeatCount(ValueAnimator.INFINITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        checkAndRequestPermissions();

        
        
        



    }
    private boolean checkAndRequestPermissions() {

        int wtite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("in fragment on request", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("in fragment on request", "CWRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("in fragment on request", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showDialogOK("Storage Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {


                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }
    
    
    
    
    
    
    
    
    
    
    public void registerFun(View view){
        Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
        if(mAuth.getCurrentUser()!=null){
            finish();
        }

    }

    public void loginFun(View view){
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        if(mAuth.getCurrentUser()!=null){
            finish();
        }


    }
}
