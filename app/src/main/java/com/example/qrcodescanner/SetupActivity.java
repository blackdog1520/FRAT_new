package com.example.qrcodescanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {






    private static final int ImageBack = 1;
    FirebaseAuth mAuth;
    CircleImageView imageView;
    Bitmap bitmap;
    String email,password;
    Uri selectedImage;
    FirebaseUser user;

    ProgressDialog dialog;
    private StorageReference mStorageRef;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        imageView= findViewById(R.id.ProfilePicture);
        email = getIntent().getExtras().getString("email");
        password = getIntent().getExtras().getString("password");
        mStorageRef = FirebaseStorage.getInstance().getReference("Users").child("ProfilePicture");
        dialog = new ProgressDialog(SetupActivity.this);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==ImageBack && resultCode == RESULT_OK) {



            selectedImage = data.getData();
             bitmap = null;


             try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageView.setImageBitmap(bitmap);
                dialog.setTitle("Uploading Image...");
                user = mAuth.getCurrentUser();
                StorageReference imageName = mStorageRef.child("User:"+user.getUid());




                imageName.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        dialog.dismiss();
                        Toast.makeText(SetupActivity.this,"Uploaded Successfully",Toast.LENGTH_LONG).show();




                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(SetupActivity.this,"Upload UnSuccessful",Toast.LENGTH_LONG).show();;
                    }
                });













                //For the API less than 28 (Android version 8 )
                //String base64Image = android.util.Base64.encodeToString(bytes, android.util.Base64.DEFAULT);


                // we finally have our base64 string version of the image, save it.
               // firebase.child("pic").setValue(base64Image);
               // System.out.println("Stored image with length: " + bytes.length);


            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }



    public void addimgFun(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,ImageBack);


    }


    public void register(View view){
        Toast.makeText(this,"Registering",Toast.LENGTH_LONG).show();


        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }

        EditText name = findViewById(R.id.userNameText);
        EditText rollNo = findViewById(R.id.rollnoTxt);
        EditText BatchNO = findViewById(R.id.batchTxt);
        EditText Mobileno = findViewById(R.id.mobileNumberText);
        final String NameUser = name.getText().toString();
        final String Roll = rollNo.getText().toString();
        final String Batch = BatchNO.getText().toString();
        final String mobile= Mobileno.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("signin", "signInWithEmail:success");
                            user = mAuth.getCurrentUser();
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

                            startActivity(new Intent(SetupActivity.this,ProfileActivity.class));
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signin", "signInWithEmail:failure", task.getException());
                            Toast.makeText(SetupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });






    }
}
