package com.example.smartlist;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ActiveList extends Fragment{

    static ActiveListAdapter myadapter;
    private ListView lista;
    static RecyclerView rvProductos;
    public static List<Producto> lstSelected = new ArrayList<>();
    androidx.appcompat.widget.Toolbar toolbar;
    static boolean actionMode=true;
    private static Activity mAct;
    static boolean addMore;
    static int id_lista;
    public static TextView tvTotal;
    public static TextView tvPresupuesto;
    public ActiveList() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentManager mng = getFragmentManager();
        View v = inflater.inflate(R.layout.active_list_fragment, container, false);
        id_lista=-1;
        Bundle b = getArguments();
        if(b!=null)
             id_lista = b.getInt("lista",-1);

        DBHelper conn = new DBHelper(this.getContext(),"bdsmartlist",null,DBHelper.DBVersion);

        toolbar = (Toolbar) v.findViewById(R.id.tbDetalleLista);
        String title = conn.getListName(id_lista);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        TextView tvTitle = (TextView) v.findViewById(R.id.tvTbTitle);
        tvTitle.setText(title);

        tvTotal = v.findViewById(R.id.tvTotal);
        tvTotal.setText(ComprandoLista.updateSuma());

        tvPresupuesto = (TextView) v.findViewById(R.id.tvPresupuesto);
        tvPresupuesto.setText(ComprandoLista.getPresupuesto());

        Button btnPresupuesto = (Button) v.findViewById(R.id.btnPresupuesto);
        ImageButton btnCart = (ImageButton) v.findViewById(R.id.btnCart);
        btnPresupuesto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogPresupuesto d = new DialogPresupuesto();
                d.show(mng,null);
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCart d = new DialogCart();
                d.setStyle(DialogFragment.STYLE_NO_FRAME, 0);
                d.show(mng,null);
            }
        });

        rvProductos = (RecyclerView) v.findViewById(R.id.rvProductos);
        myadapter = new ActiveListAdapter(this.getContext(),ComprandoLista.lstProductos);
        rvProductos.setLayoutManager(new GridLayoutManager(this.getContext(),1));
        rvProductos.setItemViewCacheSize(ComprandoLista.lstProductos.size());
        rvProductos.setAdapter(myadapter);
        ComprandoLista.checkPresupuesto();
        return v;
    }

    public static void addProducts() {
        DBHelper conn = new DBHelper(mAct,"bdsmartlist",null,DBHelper.DBVersion);
        boolean res=false;

        for(int i=0;i<lstSelected.size();i++){
            res = conn.addProductToList(id_lista, ActiveList.lstSelected.get(i).getID());
            if(!res)
                Toast.makeText(mAct,"EL PRODUCTO YA ESTA EN LA LISTA",Toast.LENGTH_LONG).show();
        }
        ActiveList.lstSelected=new ArrayList<>();
        if(res)
            Toast.makeText(mAct,"ELEMENTOS AGREGADOS",Toast.LENGTH_SHORT).show();
    }

    public static void deleteItem(int adapterPosition) {
        ComprandoLista.lstProductos.remove(adapterPosition);
        myadapter = new ActiveListAdapter(mAct,ComprandoLista.lstProductos);
        rvProductos.setLayoutManager(new GridLayoutManager(mAct,1));
        rvProductos.setItemViewCacheSize(ComprandoLista.lstProductos.size());
        rvProductos.setAdapter(myadapter);
        Toast.makeText(mAct,"BORRADO",Toast.LENGTH_SHORT).show();
    }

    public static void itemToCart(int adapterPosition) {
        Producto p = ComprandoLista.lstProductos.get(adapterPosition);
        p.setCartCount(1);
        ComprandoLista.lstCart.add(p);
        ComprandoLista.lstProductos.remove(adapterPosition);
        myadapter = new ActiveListAdapter(mAct,ComprandoLista.lstProductos);
        rvProductos.setLayoutManager(new GridLayoutManager(mAct,1));
        rvProductos.setItemViewCacheSize(ComprandoLista.lstProductos.size());
        rvProductos.setAdapter(myadapter);
        tvTotal.setText(ComprandoLista.updateSuma());
        Toast.makeText(mAct,"AGREGADO AL CARRITO",Toast.LENGTH_SHORT).show();
        ComprandoLista.checkPresupuesto();
    }

    public static  void itemToCart(Producto p){
        p.setCartCount(1);
        ComprandoLista.lstCart.add(p);
        myadapter = new ActiveListAdapter(mAct,ComprandoLista.lstProductos);
        rvProductos.setLayoutManager(new GridLayoutManager(mAct,1));
        rvProductos.setItemViewCacheSize(ComprandoLista.lstProductos.size());
        rvProductos.setAdapter(myadapter);
        tvTotal.setText(ComprandoLista.updateSuma());
        Toast.makeText(mAct,"AGREGADO AL CARRITO",Toast.LENGTH_SHORT).show();
        ComprandoLista.checkPresupuesto();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAct = this.getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        myadapter = new ActiveListAdapter(this.getContext(),ComprandoLista.lstProductos);
        rvProductos.setLayoutManager(new GridLayoutManager(this.getContext(),1));
        rvProductos.setItemViewCacheSize(ComprandoLista.lstProductos.size());
        rvProductos.setAdapter(myadapter);
    }

    protected void prepareSelection(View v, int position){
        if(((CheckBox)v).isChecked()){
            lstSelected.add(ComprandoLista.lstProductos.get(position));
        }
        else{
            lstSelected.remove(ComprandoLista.lstProductos.get(position));
        }
    }

    public static void refreshItems(){
        myadapter = new ActiveListAdapter(mAct,ComprandoLista.lstProductos);
        rvProductos.setLayoutManager(new GridLayoutManager(mAct,1));
        rvProductos.setItemViewCacheSize(ComprandoLista.lstProductos.size());
        rvProductos.setAdapter(myadapter);
    }

    @Override
    public void onPause() {
        lstSelected.clear();
        super.onPause();
    }

}