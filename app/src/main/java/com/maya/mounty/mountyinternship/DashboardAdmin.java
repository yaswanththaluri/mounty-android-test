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
import com.google.firebase.auth.FirebaseUser;

public class DashboardAdmin extends AppCompatActivity {

    private ActionBar toolBar;
    boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        Fragment fragment;
        fragment = new ItemsAddAdmin();
        loadFragent(fragment);

        auth = FirebaseAuth.getInstance();



        toolBar = getSupportActionBar();
        toolBar.setTitle("Admin DashBoard");


        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.bottomnavbaradmin);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment fragment;
                switch (item.getItemId())
                {
                    case R.id.addItem:
                        toolBar.setTitle("CA Schedule");
                        fragment = new ItemsAddAdmin();
                        loadFragent(fragment);
                        return true;

                    case R.id.deleteItems:

                        return true;

                    case R.id.listIems:
                        toolBar.setTitle("Profile");
                        fragment = new ItemsListAdmin();
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
        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
                auth.signOut();
                Intent i = new Intent(DashboardAdmin.this, Authentication.class);

                finishAffinity();
                startActivity(i);
                Toast.makeText(DashboardAdmin.this, "Logged off Successfully", Toast.LENGTH_SHORT).show();

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }



}
