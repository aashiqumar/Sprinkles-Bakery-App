package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test.client.OrderAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class fOrderAdapter extends FirebaseRecyclerAdapter<add_products_model, fOrderAdapter.myViewHolder> {

    public fOrderAdapter(FirebaseRecyclerOptions<add_products_model> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int i, add_products_model model) {

        holder.txt_title.setText(model.getTitle());
        holder.txt_price.setText(model.getPrice());
        holder.txt_category.setText(model.getCategory());

        Glide.with(holder.img_view.getContext())
                .load(model.getUri())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .centerCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img_view);



       holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String userid = FirebaseAuth.getInstance().getUid();

                String title = holder.txt_title.getText().toString();
                String price = holder.txt_price.getText().toString();
                String category = holder.txt_category.getText().toString();
                String img = model.getUri();

                Map<String, Object> map = new HashMap<>();
                map.put("title", title);
                map.put("price", price);
                map.put("category", category);
                map.put("uri", img);

                FirebaseDatabase.getInstance().getReference("confirmed_orders").push()
                        .setValue(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(holder.txt_title.getContext(), "Order Confirmed.", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {


                                Toast.makeText(holder.txt_title.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });

        holder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userid = FirebaseAuth.getInstance().getUid();

                String title = holder.txt_title.getText().toString();
                String price = holder.txt_price.getText().toString();
                String category = holder.txt_category.getText().toString();
                String img = model.getUri();

                Map<String, Object> map = new HashMap<>();
                map.put("title", title);
                map.put("price", price);
                map.put("category", category);
                map.put("uri", img);



                FirebaseDatabase.getInstance().getReference("rejected_orders").push()
                        .setValue(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                FirebaseDatabase.getInstance().getReference("orders").child(getRef(i).getKey()).removeValue();


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(holder.txt_title.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });


            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rview_admin_order, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView txt_title, txt_price, txt_category;
        ImageView img_view;
        Button btn_confirm, btn_reject;
        LinearLayout btn_onclick;


        public myViewHolder(View itemView) {
            super(itemView);

            txt_title = itemView.findViewById(R.id.item_title_order);
            txt_price = itemView.findViewById(R.id.item_price_order);
            txt_category = itemView.findViewById(R.id.item_category_order);
            img_view = itemView.findViewById(R.id.item_img_view_order);
            btn_confirm = itemView.findViewById(R.id.btn_rview_order_confirm);
            btn_reject = itemView.findViewById(R.id.btn_rview_order_reject);

            //Client Side
            btn_onclick = itemView.findViewById(R.id.btn_rview_prods);
        }
    }


}
