package com.maya.mounty.mountyinternship;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Authentication extends AppCompatActivity {

    private ImageView logo;
    private Button guestLogin;
    private Button guestToAdmin;
    private Button adminLogin;
    private Button adminToGuest;
    private LinearLayout guest;
    private LinearLayout admin;
    private FirebaseAuth auth;
    private EditText guestname;
    private EditText guestEmail;
    private EditText guestPhone;
    private FirebaseUser user;
    private EditText adminMail;
    private EditText adminPassword;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(Authentication.this);
        dialog.setMessage("Logging in...Please Wait!");
        dialog.setCanceledOnTouchOutside(false);

        guestLogin = findViewById(R.id.guestenter);
        guestToAdmin = findViewById(R.id.changetoadmin);
        guest = findViewById(R.id.guestlogin);

        adminLogin = findViewById(R.id.adminenter);
        adminToGuest = findViewById(R.id.changetoguest);
        admin = findViewById(R.id.adminlogin);

        guestname = findViewById(R.id.guestname);
        guestEmail = findViewById(R.id.guestemail);
        guestPhone = findViewById(R.id.guestmobile);

        adminMail = findViewById(R.id.adminemail);
        adminPassword = findViewById(R.id.adminpassword);

        logo = findViewById(R.id.applogo);
        Glide.with(this).load(R.drawable.mountylogo).apply(RequestOptions.circleCropTransform()).into(logo);


        guestToAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guest.setVisibility(View.INVISIBLE);
                admin.setVisibility(View.VISIBLE);
            }
        });

        adminToGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                admin.setVisibility(View.INVISIBLE);
                guest.setVisibility(View.VISIBLE);
            }
        });

        guestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String na, em, nu;
                na = guestname.getText().toString();
                em = guestEmail.getText().toString();
                nu = guestPhone.getText().toString();
                if (!na.equals("") && !em.equals("") && !nu.equals(""))
                    authenticateGuest(na, em, nu);
                else
                    Toast.makeText(Authentication.this, "Fill all the Details", Toast.LENGTH_SHORT).show();
            }
        });

        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = adminMail.getText().toString();
                String password = adminPassword.getText().toString();
                if (mail.equals("") || password.equals("")) {
                    Toast.makeText(Authentication.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    authenticateAdmin(mail, password);
                }
            }
        });
    }

    public void authenticateGuest(String name, String email, String phone) {
        Toast.makeText(Authentication.this, "User Entry Successful", Toast.LENGTH_SHORT).show();
        Bundle extras = new Bundle();
        extras.putString("name", name);
        extras.putString("email", email);
        extras.putString("phone", phone);
        Intent i = new Intent(Authentication.this, UserDashboard.class);
        i.putExtras(extras);
        startActivity(i);
    }

    public void authenticateAdmin(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Authentication.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            Toast.makeText(Authentication.this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Intent i = new Intent(Authentication.this, DashboardAdmin.class);
                            startActivity(i);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(Authentication.this, "Invalid Details", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
