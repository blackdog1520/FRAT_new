package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class ShowAttendanceDateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    EditText email,subject,dateView;
    String Datestr,userBatch,Teacheruid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_attendance_date);
        email = findViewById(R.id.emaillAttID);
        subject = findViewById(R.id.subjectForAttendance);
        dateView = findViewById(R.id.DateAttID);
    }
    public void ShowCalFun(View view){


        showDatePicker();

    }
    public void showAttFun(View view){

        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        final ProgressDialog dialog = new ProgressDialog(ShowAttendanceDateActivity.this);
        dialog.setMessage("Searching");


        dialog.show();


        email = findViewById(R.id.emaillAttID);
        String emailIdTeacher = email.getText().toString();
        Log.i("Teacher email",""+emailIdTeacher);
        final String SubjectCode = subject.getText().toString();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final Boolean[] check1 = {true};
        final boolean[] check2 = {true};
        if(TextUtils.isEmpty(emailIdTeacher)){
            email.setError("Enter any email");
            email.setFocusable(true);
            dialog.dismiss();
            check1[0] = false;
        }else  if(TextUtils.isEmpty(Datestr)){
            dateView.setError("Please choose a date");
            dialog.dismiss();

            dateView.setFocusable(true);
        }else{
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


            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Teachers");
            Query query = mRef.orderByChild("email").equalTo(emailIdTeacher);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        email.setError("Wrong email");
                        email.setFocusable(true);
                        dialog.dismiss();

                    }

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String uid = "" + ds.child("uid").getValue();
                        String batch1 = "" + ds.child("batch1").getValue();
                        String batch2 = "" + ds.child("batch2").getValue();
                        String batch3 = "" + ds.child("batch3").getValue();

                         if (!TextUtils.isEmpty(batch1) && batch1.equals(userBatch)) {
                            Teacheruid = ""+uid;
                            dialog.dismiss();
                            Log.i("Teacher UID in previous activity: ",""+Teacheruid);
                            Intent intent = new Intent(ShowAttendanceDateActivity.this, ShowUserAttActivity.class);
                            intent.putExtra("uid", Teacheruid);
                            intent.putExtra("subject", SubjectCode);

                            intent.putExtra("date", Datestr);
                            startActivity(intent);
                            finish();

                            Log.i("Teacher UID: ",""+Teacheruid);
                        } else if (!TextUtils.isEmpty(batch2) && batch2.equals(userBatch)) {
                            Log.i("Teacher UId: ",""+Teacheruid);

                             dialog.dismiss();
                            Teacheruid = ""+uid;
                            Log.i("Teacher UID in previous activity: ",""+Teacheruid);
                            Intent intent = new Intent(ShowAttendanceDateActivity.this, ShowUserAttActivity.class);
                            intent.putExtra("uid", Teacheruid);
                            intent.putExtra("subject", SubjectCode);
                            intent.putExtra("date", Datestr);
                            startActivity(intent);
                            finish();


                        } else if (!TextUtils.isEmpty(batch3) && batch3.equals(userBatch)) {
                            Log.i("Teacher UID: ",""+Teacheruid);
                             dialog.dismiss();
                            Teacheruid = ""+uid;
                            Log.i("Teacher UID in previous activity: ",""+Teacheruid);
                            Intent intent = new Intent(ShowAttendanceDateActivity.this, ShowUserAttActivity.class);
                            intent.putExtra("uid", Teacheruid);
                            intent.putExtra("subject", SubjectCode);

                            intent.putExtra("date", Datestr);
                            startActivity(intent);
                            finish();
                        } else {
                            check2[0] = false;
                            email.setError("Wrong email");
                            email.setFocusable(true);
                             dialog.dismiss();

                         }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }











    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

        if(month == 0){
            Datestr = date+"-Jan-"+year;
            dateView.setText(Datestr);



        }else if(month == 1){
            Datestr = date+"-Feb-"+year;
            dateView.setText(Datestr);



        }else if(month == 3){
            Datestr = date+"-Apr-"+year;
            dateView.setText(Datestr);



        }else if(month == 4){
            Datestr = date+"-May-"+year;
            dateView.setText(Datestr);



        }else if(month == 5){
            Datestr = date+"-Jun-"+year;
            dateView.setText(Datestr);



        }else if(month == 6){
            Datestr = date+"-Jul-"+year;
            dateView.setText(Datestr);



        }else if(month == 7){
            Datestr = date+"-Aug-"+year;
            dateView.setText(Datestr);



        }else if(month == 8){
            Datestr = date+"-Sep-"+year;
            dateView.setText(Datestr);



        }else if(month == 9){
            Datestr = date+"-Oct-"+year;
            dateView.setText(Datestr);



        }else if(month == 10){
            Datestr = date+"-Nov-"+year;
            dateView.setText(Datestr);



        }else if(month == 11){
            Datestr = date+"-Dec-"+year;
            dateView.setText(Datestr);



        }else if(month == 2){
            Datestr = date+"-Mar-"+year;
            dateView.setText(Datestr);



        }




    }
    public void showDatePicker(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,this, Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();


    }
}
