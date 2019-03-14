package com.maya.mounty.mountyinternship;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> implements SwipeListener{

    private List<Item> dataList;
    private Context context;
    public View vview;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, desc;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            vview = view;
            name = (TextView) view.findViewById(R.id.displayproductname);
            price = (TextView) view.findViewById(R.id.displayproductprice);
            desc = (TextView) view.findViewById(R.id.displayproductdescription);
            image = view.findViewById(R.id.displayProductImage);
        }
    }

    public DataAdapter(List<Item> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item item = dataList.get(position);

        holder.name.setText(item.getName());
        holder.price.setText(item.getPrice());
        holder.desc.setText(item.getDescription());
        Glide.with(vview.getContext()).load(item.getImagePath()).apply(RequestOptions.circleCropTransform()).into(holder.image);



    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onSwipe(final int pos) {
        Item item = dataList.get(pos);
        Toast.makeText(vview.getContext(), item.getName()+" added to cart", Toast.LENGTH_SHORT).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference().child("cart");
        Item i = new Item(item.getName(), item.getDescription(), item.getPrice(), item.getImagePath());
        reference.push().setValue(i);

        notifyDataSetChanged();
    }
}