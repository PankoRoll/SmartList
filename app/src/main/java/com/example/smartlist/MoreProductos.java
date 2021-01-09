package com.example.smartlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MoreProductos extends Fragment implements View.OnLongClickListener {

    static MoreProductsAdapter myadapter;
    private ListView lista;
    RecyclerView rvProductos;
    public static List<Producto> lstProductos;
    public static List<Producto> lstSelected = new ArrayList<>();
    androidx.appcompat.widget.Toolbar toolbar;
    static boolean actionMode=true;
    private static Activity mAct;
    static boolean addMore;
    static int id_lista;

    public MoreProductos() {
        // Required empty public constructor
    }

    public static void addProducts() {
        DBHelper conn = new DBHelper(mAct,"bdsmartlist",null,DBHelper.DBVersion);
        boolean res=false;

        for(int i=0;i<lstSelected.size();i++){
            res = conn.addProductToList(id_lista,MoreProductos.lstSelected.get(i).getID());
            if(!res)
                Toast.makeText(mAct,"EL PRODUCTO YA ESTA EN LA LISTA",Toast.LENGTH_LONG).show();
        }
        MoreProductos.lstSelected=new ArrayList<>();
        if(res)
            Toast.makeText(mAct,"ELEMENTOS AGREGADOS",Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAct = this.getActivity();
        actionMode=true;
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_more_productos, container, false);
        DBHelper conn = new DBHelper(this.getContext(),"bdsmartlist",null,DBHelper.DBVersion);
        id_lista=-1;
        Bundle b = getArguments();
        if(b!=null)
             id_lista = b.getInt("lista",-1);

        toolbar = (androidx.appcompat.widget.Toolbar) v.findViewById(R.id.tbProductos);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.more_product_toolbar_menu_action);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        lstProductos = conn.getMissingProducts(id_lista);

        rvProductos = (RecyclerView) v.findViewById(R.id.rvProductos);
        myadapter = new MoreProductsAdapter(this.getContext(),lstProductos,this);
        rvProductos.setLayoutManager(new GridLayoutManager(this.getContext(),2));
        rvProductos.setItemViewCacheSize(lstProductos.size());
        rvProductos.setAdapter(myadapter);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        DBHelper conn = new DBHelper(this.getContext(),"bdsmartlist",null,DBHelper.DBVersion);
        lstProductos = conn.getMissingProducts(id_lista);
        myadapter = new MoreProductsAdapter(this.getContext(),lstProductos,this);
        rvProductos.setLayoutManager(new GridLayoutManager(this.getContext(),2));
        rvProductos.setItemViewCacheSize(lstProductos.size());
        rvProductos.setAdapter(myadapter);
        refreshItems(true);
    }

    @Override
    public boolean onLongClick(View v) {
        if(actionMode)
            return false;

        refreshItems(true);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.more_product_toolbar_menu_action);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return true;
    }

    protected static void refreshItems(boolean actMode) {
        actionMode = actMode;
        for (int i=0;i<myadapter.getItemCount();i++){
            myadapter.notifyItemChanged(i);
        }
    }

    protected void prepareSelection(View v, int position){
        if(((CheckBox)v).isChecked()){
            lstSelected.add(lstProductos.get(position));
        }
        else{
            lstSelected.remove(lstProductos.get(position));
        }
    }

    @Override
    public void onPause() {
        lstSelected.clear();
        super.onPause();
    }

}