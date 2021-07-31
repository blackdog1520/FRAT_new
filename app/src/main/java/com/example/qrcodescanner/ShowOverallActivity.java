package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ShowOverallActivity extends AppCompatActivity {
EditText email,subject;
String TeacherMail;
FirebaseAuth mAuth;

String userBatch,Teacheruid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_overall);
        email = findViewById(R.id.emailAllID);
        subject = findViewById(R.id.subjectForall);
        mAuth  = FirebaseAuth.getInstance();
        DatabaseReference mRef1 = FirebaseDatabase.getInstance().getReference("Users");
        Query query1 = mRef1.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid());
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    userBatch = "" + ds.child("batch").getValue();
                    Log.i("User Batch: ",""+userBatch);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void showOverFun(View view){
        email = findViewById(R.id.emailAllID);
        subject = findViewById(R.id.subjectForall);
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }


        final ProgressDialog dialog = new ProgressDialog(ShowOverallActivity.this);
        dialog.setTitle("Searching...");
        dialog.show();



        String emailIdTeacher = email.getText().toString();
        Log.i("Teacher email",""+emailIdTeacher);
        final String SubjectCode = subject.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final Boolean[] check1 = {true};
        final boolean[] check2 = {true};
        if(TextUtils.isEmpty(emailIdTeacher)){
            email.setError("Enter any email");
            email.setFocusable(true);
            check1[0] = false;
        }else  {



            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Teachers");
            Query query = mRef.orderByChild("email").equalTo(emailIdTeacher);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String uid = "" + ds.child("uid").getValue();
                        String batch1 = "" + ds.child("batch1").getValue();
                        String batch2 = "" + ds.child("batch2").getValue();
                        String batch3 = "" + ds.child("batch3").getValue();

                        if (!TextUtils.isEmpty(batch1) && batch1.equals(userBatch)) {
                            Teacheruid = ""+uid;
                            dialog.dismiss();
                            Log.i("Teacher UID in previous activity: ",""+Teacheruid);
                            Intent intent = new Intent(ShowOverallActivity.this, Activity_Overall.class);
                            intent.putExtra("uid", Teacheruid);
                            intent.putExtra("subject", SubjectCode);


                            startActivity(intent);
                            finish();

                            Log.i("Teacher UID: ",""+Teacheruid);
                        } else if (!TextUtils.isEmpty(batch2) && batch2.equals(userBatch)) {
                            Log.i("Teacher UId: ",""+Teacheruid);

                            dialog.dismiss();
                            Teacheruid = ""+uid;
                            Log.i("Teacher UID in previous activity: ",""+Teacheruid);
                            Intent intent = new Intent(ShowOverallActivity.this, Activity_Overall.class);
                            intent.putExtra("uid", Teacheruid);
                            intent.putExtra("subject", SubjectCode);

                            startActivity(intent);
                            finish();


                        } else if (!TextUtils.isEmpty(batch3) && batch3.equals(userBatch)) {
                            Log.i("Teacher UID: ",""+Teacheruid);
                            dialog.dismiss();
                            Teacheruid = ""+uid;
                            Log.i("Teacher UID in previous activity: ",""+Teacheruid);
                            Intent intent = new Intent(ShowOverallActivity.this, Activity_Overall.class);
                            intent.putExtra("uid", Teacheruid);
                            intent.putExtra("subject", SubjectCode);


                            startActivity(intent);
                            finish();
                        } else {
                            check2[0] = false;
                            email.setError("Wrong email");
                            email.setFocusable(true);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }


}
