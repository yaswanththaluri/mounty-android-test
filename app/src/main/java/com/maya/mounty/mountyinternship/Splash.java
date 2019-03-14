package com.maya.mounty.mountyinternship;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        logo = findViewById(R.id.logo);
        Glide.with(this).load(R.drawable.mountylogo).apply(RequestOptions.circleCropTransform()).into(logo);



        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user!=null)
                {
                    Intent i = new Intent(Splash.this,DashboardAdmin.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(Splash.this,Authentication.class);
                    startActivity(i);

                }
            }
        },2000);

    }
}
