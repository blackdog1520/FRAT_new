package com.example.qrcodescanner;


import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
EditText email,password;

Button btn;

    RelativeLayout layout;

    private LottieAnimationView animationView,animationWelcome;
    CountDownTimer countDownTimer = new CountDownTimer(1500,1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            animationWelcome.playAnimation();
            animationWelcome.setMinAndMaxFrame(134,600);


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar action = getSupportActionBar();
        action.setTitle("FRATS");




        btn = findViewById(R.id.button3);
        animationView = (LottieAnimationView) findViewById(R.id.animationViewLottie);
        animationWelcome = findViewById(R.id.welcomeLottie);
        countDownTimer.start();
        


        mAuth = FirebaseAuth.getInstance();
        action.setDisplayShowHomeEnabled(true);
        action.setDisplayHomeAsUpEnabled(true);


    }


    public void loginFun(View view){
        email = findViewById(R.id.emailID);
        password = findViewById(R.id.passwordID);
        String emailAdd = email.getText().toString();
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        String passwordText = password.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(emailAdd).matches()){
            email.setError("Invalid email address");
            email.setFocusable(true);

        }else {

            LoginUser(emailAdd,passwordText);


        }

    }
    public void signupFun(View view){
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        finish();
    }


    public void changeFun(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Password");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText emailUser = new EditText(this);
        emailUser.setHint("Username");
        emailUser.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailUser.setMinEms(10);

        linearLayout.addView(emailUser);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);


        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            String emailAddress = emailUser.getText().toString();
            if(!TextUtils.isEmpty(emailAddress)){
            beginRecoverymail(emailAddress);}else{
                emailUser.setError("email address can't be null");
                emailUser.setFocusable(true);


            }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();

    }

    private void beginRecoverymail(String emailAddress) {


        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        animationView.playAnimation();
        btn.setVisibility(View.INVISIBLE);
    animationView.setVisibility(View.VISIBLE);


        mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                animationView.cancelAnimation();
                btn.setVisibility(View.VISIBLE);
                animationView.setVisibility(View.INVISIBLE);

                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Resest Mail has been sent to your E-mail",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(LoginActivity.this,"Failed sending mail...",Toast.LENGTH_LONG).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,"Failed :"+e.toString(),Toast.LENGTH_LONG).show();
            }
        });


    }


    private void LoginUser(String emailAdd, String passwordText) {






        btn.setVisibility(View.INVISIBLE);
        animationView.setVisibility(View.VISIBLE);
        animationView.playAnimation();



        mAuth.signInWithEmailAndPassword(emailAdd, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            animationView.cancelAnimation();
                            btn.setVisibility(View.VISIBLE);
                            animationView.setVisibility(View.INVISIBLE);

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
                            finish();
                        } else {
                            animationView.cancelAnimation();
                            btn.setVisibility(View.VISIBLE);
                            animationView.setVisibility(View.INVISIBLE);
                            // If sign in fails, display a message to the user.
                            animationWelcome.setAnimation("forgot.json");
                            animationWelcome.setRepeatMode(LottieDrawable.RESTART);
                            animationWelcome.playAnimation();
                            Log.w("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Wrong email or password.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }



}
