package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Map;

public class StartActivity extends AppCompatActivity  {
    private IntentIntegrator qrScan;
    IntentResult result,resultNEW;
    String dbString;
    private String publickey = "";
    private  String privateKey ="";
    DatabaseReference mref;
    String code,uid,subject,batch;
    FirebaseDatabase db;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    public String getPublicKey(){
        return publickey;
    }
    public String getPrivateKey(){
        return privateKey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        db = FirebaseDatabase.getInstance();
        subject = getIntent().getStringExtra("subject");
         uid = getIntent().getStringExtra("uid");
        FirebaseAuth auth = FirebaseAuth.getInstance();
         DatabaseReference mRef1 = FirebaseDatabase.getInstance().getReference("Users");
        Query query = mRef1.orderByChild("email").equalTo(auth.getCurrentUser().getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    batch = "" + d.child("batch").getValue();
                    mref  = db.getReference("Codes").child(batch).child(subject);
                    qrScan = new IntentIntegrator(StartActivity.this);
                    qrScan.initiateScan();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
                if(result.getContents()==null){
                    Toast.makeText(this,"Sorry not Found",Toast.LENGTH_LONG).show();
                }
                else{

                        Log.i("Entered","Got Value from qr code"+result.getContents());

                    checkCode(result);


                }


        }
        else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }

    public void checkCode(IntentResult resultNEW){
        Log.i("Got message","Entered Check Code function");

        readData(resultNEW);
        Log.i("below","dbref");


    }
    public void readData(final IntentResult newResult){
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dbString = dataSnapshot.getValue(String.class);
                Log.i("Got message","successfull");
                String news = newResult.getContents();

                if( news.equals(dbString)){



                    Log.i("Entered","Entered if condition");
                    Intent newIntent = new Intent(StartActivity.this,MarkAttendenceActivity.class);
                    newIntent.putExtra("subject",subject);
                    newIntent.putExtra("uid",uid);

                   startActivity(newIntent);
                   finish();

                }
                else{
                    Log.i("Error","Strings not equal");
                    Toast.makeText(StartActivity.this,"Wrong QR code",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(StartActivity.this,ProfileActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("Error","Error"+databaseError.toException());
            }
        });
        Log.i("Read data function",dbString+" this");


    }


}
