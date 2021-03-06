package com.riseinsteps.packbagbuddy.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.riseinsteps.packbagbuddy.LoginActivity;
import com.riseinsteps.packbagbuddy.R;
import com.riseinsteps.packbagbuddy.model.User;
import com.squareup.picasso.Picasso;


public class MyAccountFragment extends Fragment {

    private FirebaseUser mCurrentUser;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseDatabase database;
    private String name, emailId, phoneNumber, username;
    private Uri photoUrl;
    private ImageView profileImage;
    private TextView tvFullName, tvUserEmailId, tvUserPhone;
    private FirebaseAuth mAuth;
    private View view;
    private Button signout;
    private ImageView btnImageChange;

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_account, container, false);
        initComponenet();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference image_ref = storageReference.child("users/" + mAuth.getCurrentUser().getUid() + "/profile_image");
        image_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        mCurrentUser = mAuth.getCurrentUser();
        if (mCurrentUser != null) {

            fetchDetatils();

        } else {
            Toast.makeText(getActivity(), "Please Sign In with official id", Toast.LENGTH_SHORT).show();
        }

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mAuth.signOut();
                Intent intent= new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        btnImageChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery,100);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Uri imageUri= data.getData();
//                profileImage.setImageURI(imageUri);
                storeImageToFireBase(imageUri);

            }
        }
    }

    private void storeImageToFireBase(Uri imageUri)
    {
        StorageReference filerefrence = storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"/profile_image");
        filerefrence.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                filerefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        Picasso.get().load(uri).into(profileImage);
                        NavigationView navigationView=getActivity().findViewById(R.id.nav_view);
                        View headerView = navigationView.getHeaderView(0);

                        ImageView nav_userImage=headerView.findViewById(R.id.user_profile_image);
                        Picasso.get().load(uri).into(nav_userImage);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void fetchDetatils()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference("user/" + mCurrentUser.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                name = user.getName();
                Log.v("hello",name);
              //  username = user.getUserName();
                emailId = user.getEmail();
                phoneNumber = user.getPhoneNumber();

                //Uri photoUrl = user.getPhotoUrl();

                tvFullName.setText(name);
                //tvUserName.setText(username);
                tvUserPhone.setText(phoneNumber);
                tvUserEmailId.setText(emailId);
                //Glide.with(this).load(photoUrl).into(profileImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.v(error.getMessage(), "My Account Fragment");
            }
        });
    }

    public void initComponenet() {
        mAuth = FirebaseAuth.getInstance();
        tvFullName = view.findViewById(R.id.fullname);
        tvUserEmailId = view.findViewById(R.id.tvUserEmailid);
        tvUserPhone = view.findViewById(R.id.tvUserPhone);
        profileImage = view.findViewById(R.id.profileimage);

        //  tvUserName = view.findViewById(R.id.tvUserName);


        signout = view.findViewById(R.id.ms_Signout);
//        //tvUserName = view.findViewById(R.id.tvUserName);
        btnImageChange = view.findViewById(R.id.btnChangeImage);

    }
}