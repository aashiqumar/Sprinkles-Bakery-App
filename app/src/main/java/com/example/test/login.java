package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {

    private Button btn_login_signup, btn_login;
    private EditText txt_email, txt_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login_signup = findViewById(R.id.btn_signup);
        btn_login = findViewById(R.id.btn_login_page);
        txt_email= findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);

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

                if ( adminEmail.equals("admin") && adminPassword.equals("admin123") ) {

                    Toast.makeText(getApplicationContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(login.this, admin.class));
                }

                if ((!"admin".equals(adminEmail)) && (!"admin123".equals(adminPassword)) ){

                    Toast.makeText(getApplicationContext(), "Incorrect Admin Credentials", Toast.LENGTH_SHORT).show();
                }

                if (adminEmail.isEmpty() && adminPassword.isEmpty() )
                {
                    Toast.makeText(getApplicationContext(), "Incorrect Admin Credentials", Toast.LENGTH_SHORT).show();
                }




            }

        });





    }
}