package com.maya.mounty.mountyinternship;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDashboard extends AppCompatActivity {



    private ActionBar toolBar;
    boolean doubleBackToExitPressedOnce = false;
    private String name;
    private String email;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);


        Intent i = getIntent();
        Bundle extras = i.getExtras();

        name = extras.getString("name");
        email = extras.getString("email");
        phone = extras.getString("phone");

        Fragment fragment;
        fragment = new UserList();
        loadFragent(fragment);




        toolBar = getSupportActionBar();
        toolBar.setTitle("User DashBoard");


        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.bottomnavbaruser);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment;
                switch (item.getItemId())
                {
                    case R.id.useritemlist:
                        toolBar.setTitle("Products Availible");
                        fragment = new UserList();
                        loadFragent(fragment);
                        return true;


                    case R.id.usercart:
                        toolBar.setTitle("Cart");
                        fragment = new UserCart();
                        loadFragent(fragment);
                        addUserDetails();
                        return true;


                    case R.id.track:
                        toolBar.setTitle("Track Order");
                        fragment = new Track();
                        loadFragent(fragment);
                        return true;
                }
                return false;
            }
        });
    }


    public void loadFragent(Fragment fragment)
    {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.userframe, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



    public void addUserDetails()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();

        UserDetails user = new UserDetails(name, email, phone);
        reference.child("UserDetails").setValue(user);

    }




    @Override
    public void onBackPressed() {


        if (doubleBackToExitPressedOnce) {
            finishAffinity();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbarmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                Intent i = new Intent(UserDashboard.this, Authentication.class);

                finishAffinity();
                startActivity(i);
                Toast.makeText(UserDashboard.this, "Logged off Successfully", Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
