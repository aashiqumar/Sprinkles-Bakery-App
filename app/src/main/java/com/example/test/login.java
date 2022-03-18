package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test.client.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class login extends AppCompatActivity {

    private Button btn_login_signup, btn_login;
    private EditText txt_email, txt_password;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login_page);
        txt_email= findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);

        firebaseAuth = FirebaseAuth.getInstance();

        //Admin Login Code



        btn_login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(login.this, signup.class));
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String adminEmail = txt_email.getText().toString();
                String adminPassword = txt_password.getText().toString();

//                String clientEmail = txt_email.getText().toString();
//                String clientPassword = txt

                if (adminEmail.isEmpty() && adminPassword.isEmpty()) {

                    Toast.makeText(getApplicationContext(), "Incorrect Admin Credentials", Toast.LENGTH_SHORT).show();
                } else if (adminEmail.equals("admin") && adminPassword.equals("admin123")) {

                    Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(login.this, admin.class));
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(adminEmail, adminPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Login Sucessful!", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(login.this, client_dashboard.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed To Load", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }


        });





    }
}