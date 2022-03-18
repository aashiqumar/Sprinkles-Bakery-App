package com.example.test.client;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.add_products_model;
import com.example.test.login;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class client_dashboard extends AppCompatActivity {

    private RecyclerView rView;
    private ClientAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference db;
    private ArrayList<add_products_model> list;
    private ProgressBar pbar;
    private Button btn_cart, btn_logout;
    private ImageButton btn_exit, btn_delete, btn_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);

        rView = findViewById(R.id.client_rview);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setHasFixedSize(true);

        btn_cart = findViewById(R.id.btn_cart_client);
        btn_logout = findViewById(R.id.btn_logout_client);

        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(client_dashboard.this, client_cart.class));
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                builder.setTitle("Are You Sure?");
                builder.setMessage("You Will Be Logged Out of App Now");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getApplicationContext(), "Sucessfully Logged Out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(client_dashboard.this, login.class));
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                });
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();

        db = firebaseDatabase.getReference("cupcakes").child("classic");
        db.keepSynced(true);

        list = new ArrayList<>();
        adapter = new ClientAdapter(this, list);

        rView.setAdapter(adapter);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot datasnap : snapshot.getChildren())
                {

                    add_products_model sm = datasnap.getValue(add_products_model.class);
                    list.add(sm);

                    adapter.notifyDataSetChanged();

                    if (!snapshot.exists())
                    {
                        Toast.makeText(client_dashboard.this, "No DATA", Toast.LENGTH_SHORT).show();
                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(client_dashboard.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}