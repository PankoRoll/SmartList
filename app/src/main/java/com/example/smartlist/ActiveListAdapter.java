package com.example.smartlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ActiveListAdapter extends RecyclerView.Adapter<ActiveListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Producto> mData;


    public ActiveListAdapter(Context mContext, List<Producto> mData) {
        this.mContext = mContext;
        this.mData = mData;

    }

    @Override
    public ActiveListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        v = mInflater.inflate(R.layout.shopping_cardview,parent,false);
        return new ActiveListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveListAdapter.MyViewHolder holder, int position) {
        String precio = "$"+mData.get(position).getPrecio();

        String cantidadUnidad = mData.get(position).getCantidad();
        holder.tvNombre.setText(mData.get(position).getNombre());
        holder.tvPrecio.setText(precio);
        holder.tvCantidad.setText(cantidadUnidad);
        Picasso.get()
                .load(mData.get(position).getImagen()).into(holder.ivImg);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvNombre;
        TextView tvCantidad;
        ImageView ivImg;
        TextView tvPrecio;
        ImageButton ibDelete;
        ImageButton ibAdd;

        public MyViewHolder(View itemView){
            super(itemView);

            tvPrecio = (TextView) itemView.findViewById(R.id.tvPrecio);
            tvNombre = (TextView) itemView.findViewById(R.id.tvNombreProducto);
            tvCantidad = (TextView) itemView.findViewById(R.id.tvCantidadProducto);
            ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
            ibDelete = (ImageButton) itemView.findViewById(R.id.ibDelete);
            ibAdd = (ImageButton) itemView.findViewById(R.id.ibAdd);

            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActiveList.deleteItem(getAdapterPosition());
                    //Toast.makeText(v.getContext(),"DELETE"+getAdapterPosition(),Toast.LENGTH_SHORT).show();
                }
            });
            ibAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActiveList.itemToCart(getAdapterPosition());
                    //Toast.makeText(v.getContext(),"DELETE"+getAdapterPosition(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

