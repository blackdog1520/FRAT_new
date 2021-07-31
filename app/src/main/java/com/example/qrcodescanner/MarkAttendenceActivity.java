package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MarkAttendenceActivity extends AppCompatActivity {
String subject,uid;
FirebaseAuth auth;
DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendence);
       subject = getIntent().getStringExtra("subject");
        uid = getIntent().getStringExtra("uid");
        auth = FirebaseAuth.getInstance();
        Date c = Calendar.getInstance().getTime();
        final TextView success = findViewById(R.id.textView11);
        final TextView failed = findViewById(R.id.textView11);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        mRef = FirebaseDatabase.getInstance().getReference("Attendance").child(uid).child(subject).child(formattedDate);            //here uid used in path is teachers UID

        mRef.child(auth.getCurrentUser().getUid()).setValue("Present").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                success.setVisibility(View.VISIBLE);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                failed.setVisibility(View.VISIBLE);
                Toast.makeText(MarkAttendenceActivity.this,"Error : "+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });



    }
    public void BackToMainMenu(View view){
        startActivity(new Intent(MarkAttendenceActivity.this,ProfileActivity.class));

    }
}
