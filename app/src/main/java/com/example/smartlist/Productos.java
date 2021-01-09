package com.example.smartlist;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class Productos extends Fragment implements View.OnLongClickListener {

    static ProductRecyclerAdapter myadapter;
    private ListView lista;
    static RecyclerView rvProductos;
    public static List<Producto> lstProductos;
    public static List<Producto> lstSelected = new ArrayList<>();
    static androidx.appcompat.widget.Toolbar toolbar;
    static boolean actionMode=false;
    private static Activity mAct;
    static boolean addMore;
    public static Productos mFragment;

    public Productos() {
        // Required empty public constructor
    }


    public static void openEditor(int adapterPosition) {
        Producto editable = lstProductos.get(adapterPosition);
        Intent i = new Intent(mAct, EditarProducto.class);
        i.putExtra("ID", editable.getID());
        i.putExtra("barcode", editable.getBarcode());
        i.putExtra("nombre", editable.getNombre());
        i.putExtra("tipo", editable.getTipo());
        i.putExtra("imagen", editable.getImagen());
        i.putExtra("cantidad", editable.getCantidad());
        i.putExtra("precio", editable.getPrecio());
        mAct.startActivity(i);
    }

    public static void exitActionMode() {
        actionMode=false;
        DBHelper conn = new DBHelper(mAct,"bdsmartlist",null,DBHelper.DBVersion);
        lstProductos = conn.getAllProducts();
        myadapter = new ProductRecyclerAdapter(mAct,lstProductos,mFragment);
        rvProductos.setLayoutManager(new GridLayoutManager(mAct,2));
        rvProductos.setItemViewCacheSize(lstProductos.size());
        rvProductos.setAdapter(myadapter);
        ((AppCompatActivity)mAct).setSupportActionBar(toolbar);
        ((AppCompatActivity)mAct).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAct = this.getActivity();
        mFragment=this;
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_productos, container, false);
        DBHelper conn = new DBHelper(this.getContext(),"bdsmartlist",null,DBHelper.DBVersion);


        actionMode = false;
        toolbar = (androidx.appcompat.widget.Toolbar) v.findViewById(R.id.tbProductos);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        lstProductos = conn.getAllProducts();

        rvProductos = (RecyclerView) v.findViewById(R.id.rvProductos);
        myadapter = new ProductRecyclerAdapter(this.getContext(),lstProductos,this);
        rvProductos.setLayoutManager(new GridLayoutManager(this.getContext(),2));
        rvProductos.setItemViewCacheSize(lstProductos.size());
        rvProductos.setAdapter(myadapter);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        DBHelper conn = new DBHelper(this.getContext(),"bdsmartlist",null,DBHelper.DBVersion);
        lstProductos = conn.getAllProducts();
        myadapter = new ProductRecyclerAdapter(this.getContext(),lstProductos,this);
        rvProductos.setLayoutManager(new GridLayoutManager(this.getContext(),2));
        rvProductos.setItemViewCacheSize(lstProductos.size());
        rvProductos.setAdapter(myadapter);
        refreshItems(false);
    }

    @Override
    public boolean onLongClick(View v) {
        if(actionMode)
            return false;


        refreshItems(true);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.product_toolbar_menu_action);
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

    public static boolean deleteSelected(Context c){
        DBHelper conn = new DBHelper(c,"bdsmartlist",null,DBHelper.DBVersion);
        boolean res = conn.rmvProducts(lstSelected);
        refreshItems(false);
        lstSelected = new ArrayList<>();
        if(res)
            Toast.makeText(c,"Borrado con éxito",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(c,"Error al Borrar",Toast.LENGTH_LONG).show();
        return res;
    }
}