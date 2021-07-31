package com.example.qrcodescanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private static final int IMAGE_CHANGE = 1;
    FirebaseAuth auth;
FirebaseUser user;
FirebaseDatabase database;
DatabaseReference mRef;
StorageReference mStorageRef;
EditText Name , RollNo,BatchText,MobileNumber;
ProgressDialog dialog;
CircleImageView profileImg;
    Bitmap bitmap;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
           Toast.makeText(this,"User Not Found", Toast.LENGTH_LONG).show();


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference("Users").child("ProfilePicture");

        Name = findViewById(R.id.userNameText);
        RollNo = findViewById(R.id.rollnoUserTxt);
        profileImg =findViewById(R.id.ProfilePicture);
        BatchText =findViewById(R.id.batchUserTxt);
        MobileNumber = findViewById(R.id.mobileNumberUserText);
        Query query = mRef.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    String name = ""+d.child("name").getValue();
                    String roll = ""+d.child("roll").getValue();
                    String phone = ""+d.child("phone").getValue();
                    String batch = ""+d.child("batch").getValue();

                    Name.setText(name);
                    RollNo.setText(roll);
                    MobileNumber.setText(phone);
                BatchText.setText(batch);



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        try {
            File sdcard = Environment.getExternalStorageDirectory();

            File folder = new File(sdcard.getAbsoluteFile(), "ProfilePicture_hiChat");//the dot makes this directory hidden to the user
            folder.mkdir();
            File file = new File(folder.getAbsoluteFile(), user.getUid() + ".jpg");
            if (file.exists()) {

                FileInputStream input = new FileInputStream(file);
                profileImg.setImageBitmap(BitmapFactory.decodeStream(input));




            } else {


                StorageReference imageName = mStorageRef.child("User:" + user.getUid());
                imageName.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        profileImg.setImageBitmap(image);


                        File sdcard = Environment.getExternalStorageDirectory();

                        File folder = new File(sdcard.getAbsoluteFile(), "ProfilePicture_hiChat");//the dot makes this directory hidden to the user
                        folder.mkdir();
                        File file = new File(folder.getAbsoluteFile(), user.getUid() + ".jpg");





                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            image.compress(Bitmap.CompressFormat.JPEG, 50, out);
                            out.flush();
                            out.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        }

        catch (Exception e){
            Picasso.get().load(R.drawable.ic_add_a_photo_black_24dp).into(profileImg);
        }
    }



    public void changeProfileFun(View view){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        Name = findViewById(R.id.userNameText);
        RollNo = findViewById(R.id.rollnoUserTxt);
        profileImg =findViewById(R.id.avatarUser);
        BatchText =findViewById(R.id.batchUserTxt);
        MobileNumber = findViewById(R.id.mobileNumberUserText);

        //changes profile  of user
        final String NameUser = Name.getText().toString();
        final String Roll = RollNo.getText().toString();
        final String Batch = BatchText.getText().toString();
        final String mobile= MobileNumber.getText().toString();


                            // Sign in success, update UI with the signed-in user's information

                            user = auth.getCurrentUser();
                            String mailID = user.getEmail();
                            Log.i("email",""+mailID);
                            String UID = user.getUid();
                            Log.i("email",""+UID);
                            HashMap<Object , String> hasmap = new HashMap<>();

                            Log.i("register","hashmap");
                            hasmap.put("email",mailID);
                            hasmap.put("uid", UID);
                            hasmap.put("phone", mobile);
                            hasmap.put("name", NameUser);
                            hasmap.put("batch",Batch);


                            hasmap.put("roll", Roll);
                            // do it later
                            Log.i("register","hashmap successful");

                            FirebaseDatabase firebaseApp = FirebaseDatabase.getInstance();
                            DatabaseReference mRef = firebaseApp.getReference("Users");

                            mRef.child(UID).setValue(hasmap);

                            startActivity(new Intent(EditProfileActivity.this,ProfileActivity.class));
                            finish();





                        }









    public void removeImgFun(View view){
        dialog= new ProgressDialog(this);
        dialog.setMessage("Removing...");
        dialog.show();
        StorageReference deleteImage = mStorageRef.child("User:"+user.getUid());
        deleteImage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();
                File sdcard = Environment.getExternalStorageDirectory();

                File folder = new File(sdcard.getAbsoluteFile(), "ProfilePicture_hiChat");//the dot makes this directory hidden to the user
                folder.mkdir();
                File file = new File(folder.getAbsoluteFile(), user.getUid() + ".jpg");
                file.delete();
                Toast.makeText(EditProfileActivity.this,"Removed photo",Toast.LENGTH_LONG).show();

                Picasso.get().load(R.drawable.ic_add_a_photo_black_24dp).into(profileImg);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(EditProfileActivity.this,"Unsuccessful removing photo",Toast.LENGTH_LONG).show();

            }
        });



        //removes image


    }


    public void cancelFun(View view){
        startActivity(new Intent(EditProfileActivity.this,ProfileActivity.class));
        finish();
        //cancel profile change



    }

    public void changeImgFun(View view){
    Intent intent = new Intent();
    intent.setType("image/*");
    startActivityForResult(intent,IMAGE_CHANGE);

        //change profile image of user

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==IMAGE_CHANGE && resultCode == RESULT_OK) {



            Uri selectedImage = data.getData();
            bitmap = null;


            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                profileImg.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                //here you can choose quality factor in third parameter(ex. i choosen 25)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                byte[] fileInBytes = baos.toByteArray();

                dialog = new ProgressDialog(EditProfileActivity.this);
                dialog.setTitle("Uploading Image...");
                dialog.show();
                user = auth.getCurrentUser();
                StorageReference imageName = mStorageRef.child("User:"+user.getUid());




                imageName.putBytes(fileInBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        File sdcard = Environment.getExternalStorageDirectory();

                        File folder = new File(sdcard.getAbsoluteFile(), "ProfilePicture_hiChat");//the dot makes this directory hidden to the user
                        folder.mkdir();
                        File file = new File(folder.getAbsoluteFile(), user.getUid() + ".jpg");
                        file.delete();
                        //file deleted from internal storage


                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                            out.flush();
                            out.close();
                            //file created in local storage

                        } catch (Exception e) {
                            e.printStackTrace();
                        }




                        Toast.makeText(EditProfileActivity.this,"Photo Changed Successfully",Toast.LENGTH_LONG).show();




                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(EditProfileActivity.this,"Photo change Unsuccessful",Toast.LENGTH_LONG).show();;
                    }
                });



            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}
