package com.example.qrcodescanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
ProgressDialog dialog;
String emailAdd,password;
EditText emailText,passwordText;
Button btn;
TextView accountexist;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Registering...");
        accountexist = findViewById(R.id.textView);
        mAuth = FirebaseAuth.getInstance();



    }
@Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
}
public  void registerFun(View view){
    try {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    } catch (Exception e) {
        // TODO: handle exception
    }
        emailText = findViewById(R.id.emailID);
        passwordText = findViewById(R.id.passwordID);
         emailAdd = emailText.getText().toString();
        password = passwordText.getText().toString();

    Pattern letter = Pattern.compile("[a-zA-z]");
    Pattern digit = Pattern.compile("[0-9]");
    Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
    //Pattern eight = Pattern.compile (".{8}");
    Pattern thapar = Pattern.compile("thapar.edu");
Matcher hasThapr = thapar.matcher(emailAdd);
    Matcher hasLetter = letter.matcher(password);
    Matcher hasDigit = digit.matcher(password);
    Matcher hasSpecial = special.matcher(password);
    boolean letterbool = hasLetter.find();
    boolean digitbool = hasDigit.find();
    boolean specialbool = hasSpecial.find();
boolean thaparbool = hasThapr.find();
    if(!Patterns.EMAIL_ADDRESS.matcher(emailAdd).matches()){
        emailText.setError("Invalid Email");
        emailText.setFocusable(true);
    }
    else if(password.length()<6){
        passwordText.setError("Password must be atleast 6 character");
        passwordText.setFocusable(true);
    }
    else if(!letterbool){
        passwordText.setError("Must contain Alphabets");
        passwordText.setFocusable(true);

    }else if( !digitbool){
        passwordText.setError("Must contain Numbers");
        passwordText.setFocusable(true);

    }else if( !specialbool){
        passwordText.setError("Must contain Special Character");
        passwordText.setFocusable(true);

    }
    else if(!thaparbool){
        emailText.setError("Not a thapar student");
        emailText.setFocusable(true);

    }else{


        registerUser(emailAdd,password);


    }
}

    private void registerUser(final String emailAdd, final String password) {
        dialog.show();

        mAuth.createUserWithEmailAndPassword(
                emailAdd,password
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d("Register","Successfull register");

                    FirebaseUser user  = mAuth.getCurrentUser();
                    dialog.dismiss();

                    Intent intent = new Intent(RegisterActivity.this, SetupActivity.class);
                    intent.putExtra("email",emailAdd);
                    intent.putExtra("password",password);
                    startActivity(intent);
                                    finish();


                                }

            else{
                    Log.w("Register","Unsuccessful Register",task.getException());
                    dialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    public void loginOpenFun(View view){
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);


    }
}

