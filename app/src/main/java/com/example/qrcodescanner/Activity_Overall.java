package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.qrcodescanner.adapters.MyDetailsAdapter;
import com.example.qrcodescanner.models.ModelDate;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Activity_Overall extends AppCompatActivity {

    String subject,uid;
    float perc,remaining;
    ArrayList<PieEntry> yPresentValues = new ArrayList<>();
    ArrayList<PieEntry> yAbsentValues = new ArrayList<>();
    PieChart pieChartPresent,pieChartAbsent;
    RecyclerView recyclerView;
    List<ModelDate> list;
    float percentage;
    MyDetailsAdapter adapter;
    FirebaseAuth mAuth;
    int PresentCount,AbsentCount;
    private boolean ans = false;
    private String StudentRoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__overall);

        subject = getIntent().getStringExtra("subject");
        uid = getIntent().getStringExtra("uid");

        boolean check = showOverallAttendance(subject,uid);
        recyclerView = findViewById(R.id.recyclerViewDetails);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        list = new ArrayList<>();

        GridLayoutManager manager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(manager);

        mAuth = FirebaseAuth.getInstance();





    }



    public void showGraph(){
        perc = percentage;
        remaining = 100-perc;








        pieChartPresent = findViewById(R.id.presentPieAtt);
        pieChartAbsent = findViewById(R.id.absentPieAtt);
        pieChartPresent.setUsePercentValues(true);
        pieChartPresent.getDescription().setEnabled(false);
        pieChartPresent.setExtraOffsets(5,10,5,5);
        pieChartPresent.setDrawHoleEnabled(true);

        pieChartPresent.setHoleColor(Color.argb(100,65,60,60));

        pieChartPresent.setElevation(12);

        pieChartPresent.setCenterTextColor(Color.WHITE);
        pieChartPresent.setDrawRoundedSlices(true);
        pieChartPresent.setHoleRadius(75f);
        pieChartPresent.setHovered(true);

        pieChartPresent.setTransparentCircleRadius(61f);


        pieChartAbsent.setUsePercentValues(true);
        pieChartAbsent.getDescription().setEnabled(false);
        pieChartAbsent.setExtraOffsets(5,10,5,5);
        pieChartAbsent.setDrawHoleEnabled(true);
        pieChartAbsent.setHoleColor(Color.argb(100,65,60,60));

        pieChartAbsent.setElevation(12);

        pieChartAbsent.setCenterTextColor(Color.WHITE);
        pieChartAbsent.setDrawRoundedSlices(true);
        pieChartAbsent.setHoleRadius(75f);
        pieChartAbsent.setHovered(true);
        pieChartPresent.setCenterText("Present");
        pieChartPresent.setCenterTextColor(Color.parseColor("#FF7A89"));
        pieChartAbsent.setCenterText("Present");
        pieChartAbsent.setCenterTextColor(Color.parseColor("#FF7A89"));
        pieChartAbsent.animateY(7000, Easing.EaseInOutExpo);
        pieChartAbsent.setTransparentCircleRadius(61f);




        pieChartAbsent.setVisibility(View.VISIBLE);
        pieChartPresent.setVisibility(View.VISIBLE);


        yPresentValues.clear();
        pieChartPresent.animateY(7000, Easing.EaseInOutExpo);
        yAbsentValues.clear();
        yAbsentValues.add(new PieEntry(remaining));
        yAbsentValues.add(new PieEntry(perc));
        yPresentValues.add(new PieEntry(perc));
        yPresentValues.add(new PieEntry(remaining));
        PieDataSet dataSetAb = new PieDataSet(yAbsentValues,"Absent");

        PieDataSet dataSet = new PieDataSet(yPresentValues,"Present");
        dataSet.setHighlightEnabled(true);
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(0f);
        dataSet.setColors(Color.GREEN,Color.argb(64,109,101,101));
        dataSet.setValueTextColor(Color.TRANSPARENT);
        PieData data = new PieData(dataSet);

        dataSetAb.setHighlightEnabled(true);
        dataSetAb.setSliceSpace(0f);
        dataSetAb.setSelectionShift(0f);
        dataSetAb.setColors(Color.RED,Color.argb(64,109,101,101));
        dataSetAb.setValueTextColor(Color.TRANSPARENT);
        PieData dataAb = new PieData(dataSetAb);
        pieChartAbsent.setData(dataAb);
        pieChartPresent.setData(data);





    }

    private boolean showOverallAttendance(final String subjectCode, final String teacherUid) {
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
                                    Log.i("Count", "" + PresentCount);
                                    int total = PresentCount + AbsentCount;
                                    Log.i("Total Count", "" + total);
                                    percentage = (float) PresentCount * 100 / total;
                                    Log.i("Total percentage", "" + percentage);
                                    ModelDate modelDate = new ModelDate("Present",date);
                                    list.add(modelDate);
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
                                    ModelDate modelDate = new ModelDate("Absent",date);
                                    list.add(modelDate);
                                    if(len == (long)total){
                                        showGraph();
                                    }


                                }
                                adapter = new MyDetailsAdapter(Activity_Overall.this,list);
                                adapter.notifyDataSetChanged();

                                recyclerView.setAdapter(adapter);
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
        return ans;

    }

    public void showView(){
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Attendance").child(uid).child(subject);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    final String date = ""+ds.getKey();
                    Log.i("Date",""+date);
                    if(!TextUtils.isEmpty(date)){
                        DatabaseReference newReference = FirebaseDatabase.getInstance().getReference("Attendance").child(uid).child(subject).child(date);
                        newReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())){
                                    ModelDate modelDate = new ModelDate("Present",date);
                                    list.add(modelDate);








                                } else {
                                    ModelDate modelDate = new ModelDate("Absent",date);
                                    list.add(modelDate);


                                }
                                adapter = new MyDetailsAdapter(Activity_Overall.this,list);
                                adapter.notifyDataSetChanged();

                                recyclerView.setAdapter(adapter);

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {


                            }
                        });
                    }


                };
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void SendEmailToTeacher(View view){


        DatabaseReference ref=  FirebaseDatabase.getInstance().getReference("Teachers");
        Query query = ref.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String TeacherMail = "" + ds.child("email").getValue().toString();
                    String[] TO = {TeacherMail};
                    String[] CC = {"ashiyad1520@gmail.com","pandeysoumya2106@gmail.com","pranavagg14@gmail.com","arshsingh13579@gmail.com","jessepinkman@gmail.com"};          //admin mail
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");


                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is regarding issue raised by student (Roll no: "+StudentRoll+")");

                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Student Raised the issue of wrong attendance percentage in database.Please resolve it with student.\n This is system generated mail. Ignore if you are aware of it.\n\n Thank you \nFRATS Team");


                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        finish();
                        Log.i("Finished sending email...", "");
                        Toast.makeText(Activity_Overall.this,
                                "Email sent to your subject teacher", Toast.LENGTH_LONG).show();
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(Activity_Overall.this,
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




}
