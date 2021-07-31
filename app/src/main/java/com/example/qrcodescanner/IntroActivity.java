package com.example.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.qrcodescanner.adapters.IntroViewPagerAdapter;
import com.example.qrcodescanner.models.ScreenItems;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {
       private ViewPager introPager;
        IntroViewPagerAdapter pagerAdapter;
        TabLayout tabLayout;
        Button getStarted;
        Animation btnanim;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        if(NotAnewUser()){
            startActivity(new Intent(IntroActivity.this,MainActivity.class));

            finish();

        }

        btnanim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animstarted);

        final List<ScreenItems> mList = new ArrayList<>();
        mList.add(new ScreenItems("FRATS works on the basis of teacher email and subject code. FRATS verifies that if your batch is taken by the teacher, whose email user has provided.","Provide Details","startintro.json"));
        mList.add(new ScreenItems("FRATS verifies face to authenticate the user. It helps FRATS to detect proxy and helping in making attendence environment proxy free!","Verify face","facerecogintro.json"));
        mList.add(new ScreenItems("FRATS renews the unique attendance ID in every 10 seconds. FRATS also encrypts the unique ID so as to secure it. User gets 2 minutes to scan the QR code." ,"Scan QR code","qrcode.json"));
        mList.add(new ScreenItems("You don't have to worry about proccess, FRATS updates the record and sends it to teacher as soon as you mark it. You can view your attendence anytime. ","Attendance Marked!","happy.json"));
        introPager = findViewById(R.id.intro_ViewPager);
        pagerAdapter =new IntroViewPagerAdapter(getApplicationContext(),mList);
        introPager.setAdapter(pagerAdapter);
        introPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(introPager.getCurrentItem()!= mList.size()-1) {
                    getStarted.setVisibility(View.INVISIBLE);
                    button.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                }else{
                    button.setVisibility(View.INVISIBLE);
                    tabLayout.setVisibility(View.INVISIBLE);
                    getStarted.setVisibility(View.VISIBLE);

                    getStarted.setAnimation(btnanim);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getStarted = findViewById(R.id.getStarted);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(introPager);
        button = findViewById(R.id.buttonNext);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = introPager.getCurrentItem();
                if(position<mList.size()-1){
                    getStarted.setVisibility(View.INVISIBLE);
                    button.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);

                    position = position+1;
                    introPager.setCurrentItem(position);
                }
                if(position == mList.size()-1){
                    loadgetStartedButton();


                }
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(introPager.getCurrentItem() == mList.size()-1)
                loadgetStartedButton();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroActivity.this,MainActivity.class));
                savePrefData();
                finish();
            }
        });


    }

    private boolean NotAnewUser() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("firstPref",MODE_PRIVATE);
        return sharedPreferences.getBoolean("IsIntroOpened",false);

    }

    private void savePrefData() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("firstPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsIntroOpened",true);
        editor.commit();
    }

    private void loadgetStartedButton() {

        button.setVisibility(View.INVISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
        getStarted.setVisibility(View.VISIBLE);
        getStarted.setAnimation(btnanim);
    }
}
