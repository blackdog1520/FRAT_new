package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.qrcodescanner.adapters.MyDetailsAdapter;
import com.example.qrcodescanner.models.ModelDate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class ShowUserAttActivity extends AppCompatActivity {
String teacherUid,SubjectCode,Date,TeacherMail,StudentRoll;
Integer Type;
FirebaseAuth mAuth;
int PresentCount,AbsentCount;
TextView review;
LottieAnimationView lottieView;
    private float percentage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_att);
        teacherUid = getIntent().getStringExtra("uid");

        mAuth = FirebaseAuth.getInstance();
        SubjectCode = getIntent().getStringExtra("subject");
        Log.i("Show attendance: ","teacheruid:"+teacherUid+"subject:"+SubjectCode);
        review = findViewById(R.id.remarkAttend);
        lottieView = findViewById(R.id.reviewLottie);



            Date = getIntent().getStringExtra("date");
            Log.i("Date: ",Date);
            showAttendanceByDateFun(Date,SubjectCode,teacherUid);
            showOverallAttendance(SubjectCode,teacherUid);



    }
    private void showOverallAttendance(final String subjectCode, final String teacherUid) {
        PresentCount = 0;
        AbsentCount = 0;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Attendance").child(teacherUid).child(subjectCode);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final String date = "" + ds.getKey();
                    final long len = dataSnapshot.getChildrenCount();
                    Log.i("Date", "" + date);
                    if (!TextUtils.isEmpty(date)) {
                        DatabaseReference newReference = FirebaseDatabase.getInstance().getReference("Attendance").child(teacherUid).child(subjectCode).child(date);
                        newReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                                    PresentCount++;

                                    int total = PresentCount + AbsentCount;
                                    percentage = (float) PresentCount * 100 / total;
                                    if(len == (long)total){
                                        showGraph();
                                    }



                                } else {
                                    AbsentCount++;
                                    Log.i("Count", "" + AbsentCount);
                                    int total = PresentCount + AbsentCount;
                                    Log.i("Total Count", "" + total);
                                    percentage = (float) PresentCount * 100 / total;
                                    Log.i("Total percentage", "" + percentage);

                                    if(len == (long)total){
                                        showGraph();
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showGraph() {
        if(percentage>=75.0){
            review.setText("Your attendance seems to be okay. Keep going!");
            lottieView.setAnimation("fine.json");
            lottieView.playAnimation();
            lottieView.setRepeatCount(ValueAnimator.INFINITE);


        }else if(percentage>=60.0){
            review.setText("Your attendance seems to be less than 75%. Attend classes!");
            lottieView.setAnimation("warning.json");
            lottieView.playAnimation();
            lottieView.setRepeatCount(ValueAnimator.INFINITE);

        }else{
            review.setText("Your attendance seems to be very low!");
            lottieView.setAnimation("danger.json");
            lottieView.playAnimation();
            lottieView.setRepeatCount(ValueAnimator.INFINITE);


        }

    }


    private void showAttendanceByDateFun(String date, String subjectCode, String teacherUid) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Attendance").child(teacherUid).child(subjectCode).child(date);


        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //show edit text
                if(dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                    TextView result = findViewById(R.id.resultTV);
                    result.setText("Present");
                }else{
                    TextView result = findViewById(R.id.resultTV);
                    result.setText("Absent");



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void SendEmailToTeacher(View view){


        DatabaseReference ref=  FirebaseDatabase.getInstance().getReference("Teachers");
        Query query = ref.orderByChild("uid").equalTo(teacherUid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                TeacherMail = ""+ds.child("email").getValue().toString();
                    String[] TO = {TeacherMail};
                    String[] CC = {"ashiyad1520@gmail.com","pandeysoumya2106@gmail.com","pranavagg14@gmail.com","arshsingh13579@gmail.com","jessepinkman@gmail.com"};          //admin mail
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");


                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is regarding issue raised by student (Roll no: "+StudentRoll+")");

                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Student Raised the issue of wrong attendance marked in database on "+Date+".Please resolve it with student.\n This is system generated mail. Ignore if you are aware of it.\n\n Thank you \nFRATS Team");


                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        finish();
                        Log.i("Finished sending email...", "");
                        Toast.makeText(ShowUserAttActivity.this,
                                "Email sent to your subject teacher", Toast.LENGTH_LONG).show();
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(ShowUserAttActivity.this,
                                "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference refnew=  FirebaseDatabase.getInstance().getReference("Users");
        Query newQuery = refnew.orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid());
        newQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                StudentRoll = ""+ds.child("roll").getValue();

            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    public void BackTomenu(View view){
        startActivity(new Intent(ShowUserAttActivity.this,ProfileActivity.class));


    }



}
