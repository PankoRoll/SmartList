package com.example.smartlist;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DialogCart extends DialogFragment {

    static CartAdapter myadapter;
    public static TextView tvTotal;
    static RecyclerView rvProductos;
    private static Context mContext;

    Button btnClose;
    boolean res;


    public DialogCart() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.dialog_cart,container,false);
        mContext = this.getContext();

        tvTotal = v.findViewById(R.id.tvTotal);
        tvTotal.setText(ComprandoLista.updateSuma());

        rvProductos = (RecyclerView) v.findViewById(R.id.rvProductos);
        btnClose = (Button) v.findViewById(R.id.btnClose);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        myadapter = new CartAdapter(this.getContext(),ComprandoLista.lstCart);
        rvProductos.setLayoutManager(new GridLayoutManager(this.getContext(),1));
        rvProductos.setItemViewCacheSize(ComprandoLista.lstProductos.size());
        rvProductos.setAdapter(myadapter);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        myadapter = new CartAdapter(this.getContext(),ComprandoLista.lstCart);
        rvProductos.setLayoutManager(new GridLayoutManager(this.getContext(),1));
        rvProductos.setItemViewCacheSize(ComprandoLista.lstProductos.size());
        rvProductos.setAdapter(myadapter);
    }

    public static void increaseCartItem(int adapterPosition) {
        ComprandoLista.lstCart.get(adapterPosition).addCartCount();
        rvProductos.setLayoutManager(new GridLayoutManager(mContext,1));
        rvProductos.setItemViewCacheSize(ComprandoLista.lstCart.size());
        rvProductos.setAdapter(myadapter);
        tvTotal.setText(ComprandoLista.updateSuma());
        ComprandoLista.checkPresupuesto();
    }

    public static void decreaseCartItem(int adapterPosition) {
        ComprandoLista.lstCart.get(adapterPosition).removeCartCount();
        rvProductos.setLayoutManager(new GridLayoutManager(mContext,1));
        rvProductos.setItemViewCacheSize(ComprandoLista.lstCart.size());
        rvProductos.setAdapter(myadapter);
        tvTotal.setText(ComprandoLista.updateSuma());
        ComprandoLista.checkPresupuesto();
    }

    public static void deleteFromCart(int adapterPosition) {
        ComprandoLista.lstProductos.add(ComprandoLista.lstCart.get(adapterPosition));
        ComprandoLista.lstCart.remove(adapterPosition);
        rvProductos.setLayoutManager(new GridLayoutManager(mContext,1));
        rvProductos.setItemViewCacheSize(ComprandoLista.lstCart.size());
        rvProductos.setAdapter(myadapter);
        tvTotal.setText(ComprandoLista.updateSuma());
        ComprandoLista.checkPresupuesto();
    }

    private void closeDialog() {
        this.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow()
                .setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void dismiss() {
        ActiveList.tvTotal.setText(ComprandoLista.updateSuma());
        ActiveList.refreshItems();
        super.dismiss();
    }
}
