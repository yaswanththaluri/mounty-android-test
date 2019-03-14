package com.maya.mounty.mountyinternship;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;


public class UserList extends Fragment {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ChildEventListener mChildEventListener;
    private DataAdapter adapter;
    private RecyclerView list;
    private List<Item> itemsAvailible;
    private ProgressDialog load;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.userlist, container, false);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("products");

        list = (RecyclerView) view.findViewById(R.id.userlistitem);

        itemsAvailible = new ArrayList<>();
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new DataAdapter(itemsAvailible, getContext());
        list.setAdapter(adapter);

        load = new ProgressDialog(view.getContext());
        load.setMessage("Fetching Items...Please wait!");
        load.setCanceledOnTouchOutside(false);
        load.show();


        list.setItemAnimator(new SlideInLeftAnimator());
        TouchListener t = new TouchListener(adapter);
        ItemTouchHelper i =new ItemTouchHelper(t);
        i.attachToRecyclerView(list);

        return view;
    }



    private void attachDataBaseReadListener() {
        if (mChildEventListener == null) {

            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
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
    }




    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            databaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
}
