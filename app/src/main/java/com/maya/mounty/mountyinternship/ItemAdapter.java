package com.maya.mounty.mountyinternship;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class ItemAdapter extends ArrayAdapter<Item>
{

    public ItemAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_item, parent, false);

        }

        TextView name = convertView.findViewById(R.id.displayproductname);
        TextView price = convertView.findViewById(R.id.displayproductprice);
        TextView description = convertView.findViewById(R.id.displayproductdescription);
        ImageView image = convertView.findViewById(R.id.displayProductImage);


        Item item = getItem(position);

        name.setText(item.getName());
        price.setText(item.getPrice());
        description.setText(item.getDescription());


        Log.i("nameproduct", item.getName());


        Glide.with(convertView.getContext()).load(item.getImagePath()).apply(RequestOptions.circleCropTransform()).into(image);




        return convertView;
    }
}
