package com.riseinsteps.packbagbuddy.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riseinsteps.packbagbuddy.AllAdventureTripActivity;
import com.riseinsteps.packbagbuddy.AllPopularSportActivity;
import com.riseinsteps.packbagbuddy.BookingsActivity;
import com.riseinsteps.packbagbuddy.ChatBot;
import com.riseinsteps.packbagbuddy.R;
import com.riseinsteps.packbagbuddy.adapter.AdventureTripAdapter;
import com.riseinsteps.packbagbuddy.adapter.PopularSportAdapter;
import com.riseinsteps.packbagbuddy.model.TripModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private View view;

    private FloatingActionButton exploreIndia, bookings;
    private RelativeLayout AllAdventureTrips, AllPopularSports;

    private RecyclerView adventureTripRecyclerView, popularSportRecyclerView;

    private AdventureTripAdapter adventureTripAdapter;
    private PopularSportAdapter popularSportAdapter;
    private DatabaseReference databaseReference;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        exploreIndia = view.findViewById(R.id.exploreIndia);
        bookings = view.findViewById(R.id.bookings);


        exploreIndia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exploreIndia = new Intent(view.getContext(), AllAdventureTripActivity.class);    //change here
                view.getContext().startActivity(exploreIndia);
            }
        });

        bookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createTour = new Intent(view.getContext(), BookingsActivity.class);
                view.getContext().startActivity(createTour);
            }
        });


        AllAdventureTrips = view.findViewById(R.id.ll_adventure_trip);

//        AllAdventureTrips.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent allAdventureTrip = new Intent(view.getContext(), AllAdventureTripActivity.class);
//                view.getContext().startActivity(allAdventureTrip);
//            }
//        });

        setAdventureTripRecyclerview(view);

        AllPopularSports = view.findViewById(R.id.ll_popular_sports);

//        AllPopularSports.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent allPopularSport = new Intent(view.getContext(), AllPopularSportActivity.class);
//                view.getContext().startActivity(allPopularSport);
//            }
//        });

        setPopularSportRecyclerview(view);

        return view;
    }


    private void setAdventureTripRecyclerview(View view) {
        adventureTripRecyclerView = view.findViewById(R.id.adventrous_recycler_home);
        adventureTripRecyclerView.setHasFixedSize(true);
        adventureTripRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

        List<TripModel> adventureTripList = new ArrayList<>();
        adventureTripAdapter = new AdventureTripAdapter(adventureTripList);

        adventureTripRecyclerView.setAdapter(adventureTripAdapter);

        // Fetching the data from firebase for adventure trip list

        databaseReference= FirebaseDatabase.getInstance().getReference("Adventrous Trips");
        //databaseReference= FirebaseDatabase.getInstance().
        final int[] count = {0};
         databaseReference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 List<TripModel> temp=new ArrayList<>();
                 for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                   //  String name= (String) dataSnapshot.child("name").getValue();

                    String name= dataSnapshot.getKey();
                     String image_url=(String) dataSnapshot.child("Image").getValue();
                     Toast.makeText(getContext(),"changed after",Toast.LENGTH_SHORT).show();
                     Log.v(name+" "+image_url,"checking database");
                     temp.add(new TripModel(name,image_url));
                     count[0]++;
                   //  Toast.makeText(getActivity(),"changed",Toast.LENGTH_SHORT).show();

                 }
                   adventureTripAdapter.setModelList(temp);
                adventureTripAdapter.notifyDataSetChanged();
                 Log.v("count",count[0]+""+snapshot.getKey());
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                 Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
             }

         });

    }

    private void setPopularSportRecyclerview(View view) {
        popularSportRecyclerView = view.findViewById(R.id.popular_recycler_home);
        popularSportRecyclerView.setHasFixedSize(true);
        popularSportRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));

       List<TripModel> popularSportList = new ArrayList<>();
        popularSportAdapter = new PopularSportAdapter(popularSportList);
        popularSportRecyclerView.setAdapter(popularSportAdapter);


        // fetching the data for popular sports from firebase

        databaseReference= FirebaseDatabase.getInstance().getReference("Popular Sports");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<TripModel> temp = new ArrayList<>();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
//                    String name= (String) dataSnapshot.child("name").getValue();
                    String name= (String) dataSnapshot.getKey();
                    String image_url=(String) dataSnapshot.child("Image").getValue();
                    Log.v(name+" "+image_url,"checking database");
                    temp.add(new TripModel(name,image_url));

                }
                popularSportAdapter.setPopularSportModelList(temp);
                popularSportAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.home_screen_menu, menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.nav_chatbot:
//                Intent intent = new Intent(view.getContext(), ChatBot.class);
//                startActivity(intent);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.home_screen_menu, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.nav_chatbot:
//                Intent intent = new Intent(view.getContext(), ChatBot.class);
//                startActivity(intent);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}