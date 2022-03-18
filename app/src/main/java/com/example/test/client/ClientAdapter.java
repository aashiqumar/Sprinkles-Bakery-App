package com.example.test.client;

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
import com.example.test.R;
import com.example.test.add_products_model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ImageViewHolder> {

    Context context;
    ArrayList<add_products_model> list;

    public ClientAdapter(Context context, ArrayList<add_products_model> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int i) {
        add_products_model model = list.get(i);
        holder.txt_title.setText(model.getTitle());
        holder.txt_price.setText(model.getPrice());
        holder.txt_category.setText(model.getCategory());

        Glide.with(holder.img_view.getContext())
                .load(model.getUri())
                .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                .centerCrop()
                .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img_view);

        holder.btn_onclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.txt_title.getContext())
                        .setContentHolder(new ViewHolder(R.layout.activity_client_order))
                        .setExpanded(true, 1000)
                        .create();

                TextView ctitle, cprice, ccategory;
                ImageView c_img;
                Button btn_order;

                View view = dialogPlus.getHolderView();

                ctitle = view.findViewById(R.id.txt_order_title);
                cprice = view.findViewById(R.id.txt_order_price);
                ccategory = view.findViewById(R.id.txt_order_category);
                c_img = view.findViewById(R.id.img_order_cake);
                btn_order = view.findViewById(R.id.btn_order_submit);

                ctitle.setText(model.getTitle());
                cprice.setText(model.getPrice());
                ccategory.setText(model.getCategory());

                Glide.with(c_img.getContext())
                        .load(model.getUri())
                        .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                        .centerCrop()
                        .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                        .into(c_img);

                btn_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String userid = FirebaseAuth.getInstance().getUid();

                        String title = ctitle.getText().toString();
                        String price = cprice.getText().toString();
                        String orderid = userid.toString();
                        String category = ccategory.getText().toString();
                        String img = model.getUri().toString();

                        Map<String, Object> map = new HashMap<>();
                        map.put("title", title);
                        map.put("price", price);
                        map.put("category", category);
                        map.put("uri", img);

                        FirebaseDatabase.getInstance().getReference("orders").push()
                                .setValue(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        Toast.makeText(context.getApplicationContext(), "Order Sucessfull.", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(context.getApplicationContext(), "A Call For Confirmation Will Take Place Soon", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                });

                dialogPlus.show();

            }
        });
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rview_client, parent, false);
        return new ImageViewHolder(view);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder{
       TextView txt_title, txt_price, txt_category;
       ImageView img_view;
       Button btn_delete;
       LinearLayout btn_onclick;

       public ImageViewHolder (View itemView){
           super(itemView);

           txt_title = itemView.findViewById(R.id.item_title_client);
           txt_price = itemView.findViewById(R.id.item_price_client);
           txt_category = itemView.findViewById(R.id.item_category_client);
           img_view = itemView.findViewById(R.id.item_img_view_client);
           btn_delete = itemView.findViewById(R.id.btn_rview_delete);

           //Client Side
           btn_onclick = itemView.findViewById(R.id.btn_rview_prods);

       }
   }
}
