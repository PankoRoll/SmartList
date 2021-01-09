package com.example.smartlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private Context mContext;
    private List<Producto> mData;


    public CartAdapter(Context mContext, List<Producto> mData) {
        this.mContext = mContext;
        this.mData = mData;

    }

    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        v = mInflater.inflate(R.layout.cart_cardview,parent,false);
        return new CartAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, int position) {
        String precio = "$"+mData.get(position).getPrecio();
        String count = ""+mData.get(position).getCartCount();

        holder.tvItemCount.setText(count);
        holder.tvNombre.setText(mData.get(position).getNombre());
        holder.tvPrecio.setText(precio);
        Picasso.get()
                .load(mData.get(position).getImagen()).into(holder.ivImg);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvNombre;
        ImageView ivImg;
        TextView tvPrecio;
        TextView tvItemCount;
        ImageButton ibDelete;
        ImageButton ibRemove;
        ImageButton ibAdd;

        public MyViewHolder(View itemView){
            super(itemView);

            tvPrecio = (TextView) itemView.findViewById(R.id.tvPrecio);
            tvNombre = (TextView) itemView.findViewById(R.id.tvNombreProducto);
            ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
            tvItemCount = (TextView) itemView.findViewById(R.id.tvitemCount);
            ibDelete = (ImageButton) itemView.findViewById(R.id.ibDelete);
            ibRemove = (ImageButton) itemView.findViewById(R.id.ibRemove);
            ibAdd = (ImageButton) itemView.findViewById(R.id.ibAdd);

            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogCart.deleteFromCart(getAdapterPosition());
                }
            });
            ibAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //tvItemCount.setText(count);
                    DialogCart.increaseCartItem(getAdapterPosition());

                }
            });
            ibRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogCart.decreaseCartItem(getAdapterPosition());
                }
            });
        }
    }
}

