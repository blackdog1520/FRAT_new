package com.example.qrcodescanner;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
FirebaseDatabase database;
TextView Name,RollNo,Batch;
CircleImageView profileImg;
DatabaseReference mRef;
FirebaseAuth auth;
FirebaseUser user;
    private StorageReference mStorageRef;
    public ProfileFragment() {
        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment




        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference("Users").child("ProfilePicture");

        Name = view.findViewById(R.id.nameText);
        RollNo = view.findViewById(R.id.rollnoText);
        profileImg = view.findViewById(R.id.avatarUser);
        Batch = view.findViewById(R.id.batchText);

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
                    Batch.setText(batch);



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
            final File file = new File(folder.getAbsoluteFile(), user.getUid() + ".jpg");
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

            catch(Exception e){
                    Picasso.get().load(R.drawable.ic_add_a_photo_black_24dp).into(profileImg);
                }



return view;
    }

}
