package com.riseinsteps.packbagbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riseinsteps.packbagbuddy.model.Review;

public class TripReviewActivity extends AppCompatActivity {
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Adventrous Trips/Kedarnath Tour");
    FirebaseAuth mauth=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_review);

        EditText comment=findViewById(R.id.comment);
        Button submit=findViewById(R.id.submit);
        TextView review=findViewById(R.id.review);

        submit.setOnClickListener((view)->{
            String text=comment.getText().toString();
            String id=mauth.getCurrentUser().getUid();
            ref.child("Reviews").push().setValue(new Review(id,text));


        });

        ref.child("Reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String text="";
                for(DataSnapshot child:snapshot.getChildren()){
                    text = text +child.child("comment").getValue()+"\n\n";
                }
                review.setText(text);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}