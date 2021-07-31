package com.example.qrcodescanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.LottieListener;
import com.airbnb.lottie.LottieTask;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

public class ProfileActivity extends AppCompatActivity {
    public ProgressDialog dialog;

boolean checkVerified;
    private FirebaseAuth mAuth;
    String userBatch;
    Button startButton;
    LottieAnimationView lottieAnimationView;

Boolean dataAvl =false;
    @Override
    public void onStart() {
        super.onStart();
      checkUserStatus();
    }
    public void checkUserStatus(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {


            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
        }else{


        }
    }
    private LottieDrawable animateCameraIcon,animateCameraIcon2,animateCameraIcon3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        lottieAnimationView = findViewById(R.id.animationViewLottiehome);
        startButton = findViewById(R.id.startButton);





        mAuth = FirebaseAuth.getInstance();
        checkVerified = mAuth.getCurrentUser().isEmailVerified();
        if(checkVerified){



        }else{
            Toast.makeText(this,"Verify your account",Toast.LENGTH_LONG).show();
            mAuth.getCurrentUser().sendEmailVerification();
            mAuth.signOut();
        }

        final BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);

        LottieTask<LottieComposition> task = LottieCompositionFactory.fromAsset(this, "home.json");

        task.addListener(new LottieListener<LottieComposition>() {
            @Override
            public void onResult(LottieComposition result) {

                animateCameraIcon = new LottieDrawable();
                animateCameraIcon.setComposition(result);
                animateCameraIcon.setRepeatCount(LottieDrawable.INFINITE);
                animateCameraIcon.setScale(0.23f);
                animateCameraIcon.playAnimation();
                animateCameraIcon.setSpeed(0.7f);
                MenuItem cameraMenuItem =bottomNavigationView.getMenu().getItem(0);
                cameraMenuItem.setIcon(animateCameraIcon);
            }
        });
        LottieTask<LottieComposition> task2 = LottieCompositionFactory.fromAsset(this, "profile.json");

        task2.addListener(new LottieListener<LottieComposition>() {
            @Override
            public void onResult(LottieComposition result) {

                animateCameraIcon2 = new LottieDrawable();
                animateCameraIcon2.setComposition(result);
                animateCameraIcon2.setRepeatCount(LottieDrawable.INFINITE);
                animateCameraIcon2.setScale(0.23f);
                animateCameraIcon2.playAnimation();
                animateCameraIcon2.setSpeed(0.7f);
                MenuItem cameraMenuItem =bottomNavigationView.getMenu().getItem(1);
                cameraMenuItem.setIcon(animateCameraIcon2);

            }
        });
        LottieTask<LottieComposition> task3 = LottieCompositionFactory.fromAsset(this, "stats.json");

        task3.addListener(new LottieListener<LottieComposition>() {
            @Override
            public void onResult(LottieComposition result) {

                animateCameraIcon3 = new LottieDrawable();
                animateCameraIcon3.setComposition(result);
                animateCameraIcon3.setRepeatCount(LottieDrawable.INFINITE);
                animateCameraIcon3.setScale(0.23f);
                animateCameraIcon3.playAnimation();
                animateCameraIcon3.setSpeed(0.7f);
                MenuItem cameraMenuItem =bottomNavigationView.getMenu().getItem(2);
                cameraMenuItem.setIcon(animateCameraIcon3);
            }
        });



        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content,fragment1,"");
        ft1.commit();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging Out...");

        checkUserStatus();


    }

    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();

    }



    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.nav_button :

                    HomeFragment fragment1 = new HomeFragment();
                    FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();



                    ft1.replace(R.id.content,fragment1,"");
                    ft1.commit();

                    return true;

                case R.id.profile_button:

                    ProfileFragment fragment2 = new ProfileFragment();
                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();

                    ft2.replace(R.id.content,fragment2,"");
                    ft2.commit();

                    return true;


                case R.id.Messagesbutton:
                    
                    ChatFragment fragment4 = new ChatFragment();
                    FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                    ft4.replace(R.id.content,fragment4,"");
                    ft4.commit();

                    return true;


            }
            return false;


        }


    };
    public void markAttendance(View view) {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        startButton = findViewById(R.id.startButton);
        lottieAnimationView = findViewById(R.id.animationViewLottiehome);
        final ProgressDialog dialog = new ProgressDialog(ProfileActivity.this);
        startButton.setVisibility(View.INVISIBLE);
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();

        final EditText teacher = findViewById(R.id.mailID);
        final EditText subject = findViewById(R.id.subjectID);

        final String teacherMail = teacher.getText().toString();
        final String subjectC = subject.getText().toString();
        final String subjectCode = subject.getText().toString();
        if (teacher.getText() == null) {
            teacher.setFocusable(true);


        } else if (subject.getText() == null) {
            subject.setFocusable(true);


        } else {
            DatabaseReference mRef1 = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());

            mRef1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userBatch = "" + dataSnapshot.child("batch").getValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Teachers");
            Query query = mRef.orderByChild("email").equalTo(teacherMail);



            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        lottieAnimationView.cancelAnimation();
                        startButton.setVisibility(View.VISIBLE);
                        lottieAnimationView.setVisibility(View.INVISIBLE);
                        teacher.setError("Wrong email");
                        teacher.setFocusable(true);
                    }

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        final String name = ""+ds.child("name").getValue();
                        final String uid = "" + ds.child("uid").getValue();
                        String batch1 = "" + ds.child("batch1").getValue();
                        String batch2 = "" + ds.child("batch2").getValue();
                        String batch3 = "" + ds.child("batch3").getValue();


                        if (!TextUtils.isEmpty(batch1) && batch1.equals(userBatch)) {

                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Codes").child(userBatch);
                            Query query1 = db.orderByKey().equalTo(subjectC);
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        lottieAnimationView.cancelAnimation();
                                        startButton.setVisibility(View.VISIBLE);
                                        lottieAnimationView.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(ProfileActivity.this, facerec.class);
                                        intent.putExtra("uid", uid);
                                        intent.putExtra("subject", subjectCode);
                                        SharedPreferences mypref = getApplicationContext().getSharedPreferences("HistoryPref",MODE_PRIVATE);
                                        int number = mypref.getInt("usersHist",0);
                                        for(int i=0;i<number;i++){
                                            String check = mypref.getString("TeacherName"+i,"");
                                            String check2 = mypref.getString("batch"+i,"");
                                            if(check.equals(name)&&check2.equals(subjectC)){
                                                dataAvl = true;
                                                break;
                                            }

                                        }
                                        if(dataAvl==false){
                                            SharedPreferences.Editor editor = mypref.edit();
                                            int update = number+1;
                                            editor.putInt("usersHist",update);
                                            editor.putString("TeacherName"+number,name);
                                            editor.putString("batch"+number,subjectC);
                                            editor.putString("mail"+number,teacherMail);
                                            editor.commit();}else {
                                            dataAvl = false;
                                        }
                                            startActivity(intent);




                                    } else {
                                        lottieAnimationView.cancelAnimation();
                                        startButton.setVisibility(View.VISIBLE);
                                        lottieAnimationView.setVisibility(View.INVISIBLE);
                                        subject.setError("Wrong subject code");
                                        subject.setFocusable(true);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        } else if (!TextUtils.isEmpty(batch2) && batch2.equals(userBatch)) {

                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Codes").child(userBatch);
                            Query query1 = db.orderByKey().equalTo(subjectC);
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        lottieAnimationView.cancelAnimation();
                                        startButton.setVisibility(View.VISIBLE);
                                        lottieAnimationView.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(ProfileActivity.this, facerec.class);
                                        intent.putExtra("uid", uid);
                                        intent.putExtra("subject", subjectCode);
                                        SharedPreferences mypref = getApplicationContext().getSharedPreferences("HistoryPref",MODE_PRIVATE);
                                        int number = mypref.getInt("usersHist",0);
                                        for(int i=0;i<number;i++){
                                        String check = mypref.getString("TeacherName"+i,"");
                                        String check2 = mypref.getString("batch"+i,"");
                                        if(check.equals(name)&&check2.equals(subjectC)){
                                            dataAvl = true;
                                            break;
                                        }

                                        }
                                        if(dataAvl==false){
                                        SharedPreferences.Editor editor = mypref.edit();
                                        int update = number+1;
                                        editor.putInt("usersHist",update);
                                        editor.putString("TeacherName"+number,name);
                                        editor.putString("batch"+number,subjectC);
                                        editor.putString("mail"+number,teacherMail);
                                        editor.commit();}else {
                                            dataAvl = false;
                                        }
                                        startActivity(intent);


                                    }
                                    else {
                                        lottieAnimationView.cancelAnimation();
                                        startButton.setVisibility(View.VISIBLE);
                                        lottieAnimationView.setVisibility(View.INVISIBLE);
                                        subject.setError("Wrong subject code");
                                        subject.setFocusable(true);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        } else if (!TextUtils.isEmpty(batch3) && batch3.equals(userBatch)) {

                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Codes").child(userBatch);
                            Query query1 = db.orderByKey().equalTo(subjectC);
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        lottieAnimationView.cancelAnimation();
                                        startButton.setVisibility(View.VISIBLE);
                                        lottieAnimationView.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(ProfileActivity.this, facerec.class);
                                        intent.putExtra("uid", uid);
                                        intent.putExtra("subject", subjectCode);
                                        SharedPreferences mypref = getApplicationContext().getSharedPreferences("HistoryPref",MODE_PRIVATE);
                                        int number = mypref.getInt("usersHist",0);
                                        for(int i=0;i<number;i++){
                                            String check = mypref.getString("TeacherName"+i,"");
                                            String check2 = mypref.getString("batch"+i,"");
                                            if(check.equals(name)&&check2.equals(subjectC)){
                                                dataAvl = true;
                                                break;
                                            }

                                        }
                                        if(dataAvl==false){
                                            SharedPreferences.Editor editor = mypref.edit();
                                            int update = number+1;
                                            editor.putInt("usersHist",update);
                                            editor.putString("TeacherName"+number,name);
                                            editor.putString("batch"+number,subjectC);
                                            editor.putString("mail"+number,teacherMail);
                                            editor.commit();}else {
                                            dataAvl = false;
                                        }
                                        startActivity(intent);


                                    }
                                    else {
                                        lottieAnimationView.cancelAnimation();
                                        lottieAnimationView.setVisibility(View.INVISIBLE);
                                        startButton.setVisibility(View.VISIBLE);

                                        subject.setError("Wrong subject code");
                                        subject.setFocusable(true);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            lottieAnimationView.cancelAnimation();
                            lottieAnimationView.setVisibility(View.INVISIBLE);
                            startButton.setVisibility(View.VISIBLE);
                            teacher.setError("Wrong email");
                            teacher.setFocusable(true);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    lottieAnimationView.cancelAnimation();
                    lottieAnimationView.setVisibility(View.INVISIBLE);
                    startButton.setVisibility(View.VISIBLE);
                }

            });



        }
    }



    public void changeProfileFunction(View view){

        startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class));


    }



    public void logoutButton(View view){
        PopupMenu menu = new PopupMenu(this,view);
       menu.inflate(R.menu.menu_main);
       menu.show();
       menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem menuItem) {
               switch (menuItem.getItemId()){
                   case R.id.LogutButton:
                       dialog.show();


                       mAuth.signOut();
                       dialog.dismiss();
                       onStart();
                       break;
                   case R.id.clearButton:
                       SharedPreferences pref = getSharedPreferences("HistoryPref",MODE_PRIVATE);
                       pref.edit().clear().apply();
                       break;



               }
               return true;
           }
       });





    }


    public void showByDate(View view){
        startActivity(new Intent(ProfileActivity.this,ShowAttendanceDateActivity.class));



    }
    public void showOverallAttendance(View view){
        startActivity(new Intent(ProfileActivity.this,ShowOverallActivity.class));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
