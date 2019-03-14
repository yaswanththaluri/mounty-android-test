package com.maya.mounty.mountyinternship;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class ItemsAddAdmin extends Fragment
{

    private EditText title;
    private EditText description;
    private EditText price;
    private FloatingActionButton fab;
    private Button add;
    private Button cancel;
    private LinearLayout addview;
    private TextView textView;
    private Button addImg;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String file;
    private View viewTot;
    private Uri fileImg;
    private ProgressDialog imgDialog;
    private ProgressDialog uploadDialog;
    private TextView check;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.items_add_admin, container, false);

        viewTot = view;

        title = view.findViewById(R.id.tileofproduct);
        description = view.findViewById(R.id.descriptionofproduct);
        price = view.findViewById(R.id.priceofproduct);
        check = view.findViewById(R.id.checkoffile);

        imgDialog = new ProgressDialog(view.getContext());
        imgDialog.setMessage("Uploading Image...Please Wait!");
        imgDialog.setCanceledOnTouchOutside(false);

        uploadDialog = new ProgressDialog(view.getContext());
        uploadDialog.setMessage("Adding Item...Please Wait");
        uploadDialog.setCanceledOnTouchOutside(false);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("products");

        addImg = view.findViewById(R.id.addproductimage);

        fab = view.findViewById(R.id.fab);

        add = view.findViewById(R.id.additem);
        cancel = view.findViewById(R.id.cancel);

        addview = view.findViewById(R.id.addingitem);
        textView = view.findViewById(R.id.welcometext);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setVisibility(View.INVISIBLE);
                addview.setVisibility(View.VISIBLE);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addview.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.VISIBLE);
                title.setText("");
                description.setText("");
                price.setText("");
                check.setText("No Image Selected");
                check.setTextColor(Color.parseColor("#cc3300"));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                uploadDialog.show();
                String name = title.getText().toString();
                String desc = description.getText().toString();
                String pr = price.getText().toString();

                if(name.equals("") || desc.equals("") || pr.equals("") || (fileImg.toString()).equals("") )
                {
                    Toast.makeText(view.getContext(), "No fields should be left Empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addItemToCloud(name, desc, pr, fileImg.toString());
                }
            }
        });

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = title.getText().toString();
                name = name.replace(" ", "_");
                file = name;
                setImage();
            }
        });

        return view;
    }



    public void setImage()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    public void addItemToCloud(String name, String desc, String pricee, String imgPath)
    {
        name = name.replace(" ", "_");
        Item addItem = new Item(name, desc, pricee, imgPath);
        databaseReference.child(name).setValue(addItem);
        uploadDialog.dismiss();
        addview.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
        title.setText("");
        description.setText("");
        price.setText("");
        check.setText("No Image Selected");
        check.setTextColor(Color.parseColor("#cc3300"));
        Toast.makeText(viewTot.getContext(),"Product Added Successfully", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {

                Select();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void Select()
    {
        imgDialog.show();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if(filePath != null) {


            final StorageReference childRef = storageReference.child("image.jpg").child(file);

            //uploading the image
            UploadTask uploadTask = childRef.putFile(filePath);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(viewTot.getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                    childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            fileImg = uri;
                            check.setText("File Uploaded");
                            check.setTextColor(Color.parseColor("#00cc66"));
                            imgDialog.dismiss();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(viewTot.getContext(), "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                    imgDialog.dismiss();
                }
            });
        }
        else {
            Toast.makeText(viewTot.getContext(), "Select an image", Toast.LENGTH_SHORT).show();
            imgDialog.dismiss();
        }
    }
}
