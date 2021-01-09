package com.example.smartlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.MyViewHolder>{

    private Context mContext;
    private List<Lista> mData;
    private Listas mfragment;


    public ListRecyclerAdapter(Context mContext, List<Lista> mData, Listas mFragment) {
        this.mContext = mContext;
        this.mData = mData;
        this.mfragment = mFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        v = mInflater.inflate(R.layout.list_cardview,parent,false);
        return new MyViewHolder(v,mfragment);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String itemCount = "";
        itemCount += mData.get(position).getItemCount();
        holder.tvItemCount.setText( itemCount+" items");
        holder.tvNombre.setText(mData.get(position).getNombre());

        if(!Listas.actionMode){
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
        TextView tvItemCount;
        CardView card;
        CheckBox cbSelect;
        Listas mListas;
        public MyViewHolder(View itemView, Listas listas){
            super(itemView);
            this.mListas = listas;
            tvNombre = (TextView) itemView.findViewById(R.id.tvListName);
            tvItemCount= (TextView) itemView.findViewById(R.id.tvListItemCount);
            card = (CardView) itemView.findViewById(R.id.cvLista);
            cbSelect = (CheckBox) itemView.findViewById(R.id.cbLista);
            cbSelect.setOnClickListener(this);

            card.setOnLongClickListener(listas);
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Listas.OpenList(getAdapterPosition());
                    //Productos.openEditor(getAdapterPosition());
                }
            });
        }
        @Override
        public void onClick(View v) {
            mListas.prepareSelection(v,getAdapterPosition());
        }
    }
}
