package com.maya.mounty.mountyinternship;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class ItemsListAdmin extends Fragment
{

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ListView list;
    private ItemAdapter adapter;
    private ChildEventListener mChildEventListener;
    private ProgressDialog load;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.items_list_admin, container, false);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("products");

        list = (ListView)view.findViewById(R.id.itemList);

        List<Item> itemsAvailible = new ArrayList<>();

        load = new ProgressDialog(view.getContext());
        load.setMessage("Fetching Items...Please Wait!");
        load.setCanceledOnTouchOutside(false);
        load.show();

        adapter = new ItemAdapter(getContext(), R.layout.list_item, itemsAvailible);
        list.setAdapter(adapter);


        return view;
    }


    private void attachDataBaseReadListener() {
        if (mChildEventListener == null) {

            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Log.i("datastart", "onChildAdded: ");
                    Item items = dataSnapshot.getValue(Item.class);
                    Log.i("dataproduct", dataSnapshot.toString());
                    adapter.add(items);
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
        adapter.clear();
    }


    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            databaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
}
