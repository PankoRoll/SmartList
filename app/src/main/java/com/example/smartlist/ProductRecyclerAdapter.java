package com.example.smartlist;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.MyViewHolder>{

    private Context mContext;
    private List<Producto> mData;
    private Productos mfragment;


    public ProductRecyclerAdapter(Context mContext, List<Producto> mData, Productos mFragment) {
        this.mContext = mContext;
        this.mData = mData;
        this.mfragment = mFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        v = mInflater.inflate(R.layout.produc_cardview,parent,false);
        return new MyViewHolder(v,mfragment);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String precio = "$"+mData.get(position).getPrecio();

        String cantidadUnidad = mData.get(position).getCantidad();
        holder.tvNombre.setText(mData.get(position).getNombre());
        holder.tvPrecio.setText(precio);
        holder.tvCantidad.setText(cantidadUnidad);
        Picasso.get()
                .load(mData.get(position).getImagen()).into(holder.ivImg);
        if(!Productos.actionMode){
            holder.cbSelect.setVisibility(View.GONE);
        }
        else{
            holder.cbSelect.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvNombre;
        TextView tvCantidad;
        ImageView ivImg;
        TextView tvPrecio;
        CardView card;
        CheckBox cbSelect;
        Productos mProductos;
        public MyViewHolder(View itemView, Productos productos){
            super(itemView);
            this.mProductos = productos;
            tvPrecio = (TextView) itemView.findViewById(R.id.tvPrecio);
            tvNombre = (TextView) itemView.findViewById(R.id.tvNombreProducto);
            tvCantidad = (TextView) itemView.findViewById(R.id.tvCantidadProducto);
            ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
            card = (CardView) itemView.findViewById(R.id.cvProducto);
            cbSelect = (CheckBox) itemView.findViewById(R.id.cbSelect);
            cbSelect.setOnClickListener(this);

            card.setOnLongClickListener(productos);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!Productos.actionMode)
                        Productos.openEditor(getAdapterPosition());
                }
            });
        }
        @Override
        public void onClick(View v) {
            mProductos.prepareSelection(v,getAdapterPosition());
        }
    }
}
