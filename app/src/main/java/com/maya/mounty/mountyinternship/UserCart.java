package com.maya.mounty.mountyinternship;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserCart extends Fragment
{

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ChildEventListener mChildEventListener;
    private DataAdapter adapter;
    private RecyclerView list;
    private List<Item> itemsAvailible;
    private FloatingActionButton fab;
    private LinearLayout lay;
    private TextView id;
    private Button b;
    private ProgressDialog load;


    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_cart, container, false);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("cart");

        fab = view.findViewById(R.id.purchase);

        list = (RecyclerView) view.findViewById(R.id.userlistcart);

        itemsAvailible = new ArrayList<>();
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new DataAdapter(itemsAvailible, getContext());
        list.setAdapter(adapter);

        lay = view.findViewById(R.id.billingsec);
        b = view.findViewById(R.id.okbill);
        id = view.findViewById(R.id.trackid);

        load = new ProgressDialog(view.getContext());
        load.setMessage("Fetching Items...Please wait!");
        load.setCanceledOnTouchOutside(false);
        load.show();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchaseItems();
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lay.setVisibility(View.INVISIBLE);
                list.setVisibility(View.VISIBLE);
            }
        });



        return  view;
    }



    private void attachDataBaseReadListener() {
        if (mChildEventListener == null) {

            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.i("dataproduct", dataSnapshot.toString());
                    Item items = dataSnapshot.getValue(Item.class);
                    itemsAvailible.add(items);
                    adapter.notifyDataSetChanged();
                    load.dismiss();
                }


                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

//                    progress.setVisibility(View.INVISIBLE);
                }
            };
            databaseReference.addChildEventListener(mChildEventListener);

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        attachDataBaseReadListener();
    }

    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        itemsAvailible.clear();
        adapter.notifyDataSetChanged();
    }




    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            databaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }



    public void purchaseItems()
    {

        Date date = new Date();
        String trackid = date.toString();

        FirebaseDatabase data = FirebaseDatabase.getInstance();
        DatabaseReference ref = data.getReference().child("orders").child(trackid.replace(" ","_"));


        for(Item i : itemsAvailible)
        {
            Item set = new Item(i.getName());
            ref.setValue(set);
        }

        databaseReference.removeValue();
        itemsAvailible.clear();
        adapter.notifyDataSetChanged();

        lay.setVisibility(View.VISIBLE);
        list.setVisibility(View.INVISIBLE);
        id.setText(trackid.replace(" ","_"));
    }

}
