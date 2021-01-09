package com.example.smartlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductoListaAdapter extends RecyclerView.Adapter<ProductoListaAdapter.MyViewHolder> {

    private Context mContext;
    private List<Producto> mData;


    public ProductoListaAdapter(Context mContext, List<Producto> mData) {
        this.mContext = mContext;
        this.mData = mData;

    }

    @Override
    public ProductoListaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        v = mInflater.inflate(R.layout.product_list_cardview,parent,false);
        return new ProductoListaAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoListaAdapter.MyViewHolder holder, int position) {
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
        CardView card;
        ImageButton ibDelete;
        public MyViewHolder(View itemView){
            super(itemView);

            tvPrecio = (TextView) itemView.findViewById(R.id.tvPrecio);
            tvNombre = (TextView) itemView.findViewById(R.id.tvNombreProducto);
            tvCantidad = (TextView) itemView.findViewById(R.id.tvCantidadProducto);
            ivImg = (ImageView) itemView.findViewById(R.id.ivImg);
            card = (CardView) itemView.findViewById(R.id.cvProducto);
            ibDelete = (ImageButton) itemView.findViewById(R.id.ibDelete);

            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetalleLista.deleteItem(getAdapterPosition());
                    //Toast.makeText(v.getContext(),"DELETE"+getAdapterPosition(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

