package com.example.smartlist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DetalleLista extends AppCompatActivity {

    private TextView mTextView;
    static RecyclerView rvProductos;
    static List<Producto> lstProductos;
    static ProductoListaAdapter myadapter;
    Button btnDelete;
    Button btnEdit;
    Button btnStart;
    FloatingActionButton fabAdd;
    boolean deleteRes;
    static Context mContext;
    static int id_lista;
    static Activity mAct;
    private static Toolbar toolbar;
    private static DetalleLista mAux;


    public static void updateToolbar() {
        DBHelper conn = new DBHelper(mAct,"bdsmartlist",null,DBHelper.DBVersion);

        String title = conn.getListName(id_lista);
        mAux.setSupportActionBar(toolbar);
        TextView tvTitle = (TextView) mAux.findViewById(R.id.tvTbTitle);
        tvTitle.setText(title);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_lista);
        mContext = this;
        mAct = this;
        mAux = this;
        FragmentManager mng = getSupportFragmentManager();

        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnStart = (Button) findViewById(R.id.btnStart);
        fabAdd = (FloatingActionButton) findViewById(R.id.fabAddMore);
        rvProductos = (RecyclerView) findViewById(R.id.rvProductos);

        id_lista = getIntent().getIntExtra("lista", -1);
        String n_lista = getIntent().getStringExtra("nombre");
        DBHelper conn = new DBHelper(this,"bdsmartlist",null,DBHelper.DBVersion);

        toolbar = (Toolbar) findViewById(R.id.tbDetalleLista);
        String title = conn.getListName(id_lista);
        setSupportActionBar(toolbar);
        TextView tvTitle = (TextView) findViewById(R.id.tvTbTitle);
        tvTitle.setText(title);

        lstProductos = conn.getProductoLista(id_lista);
        myadapter = new ProductoListaAdapter(this,lstProductos);
        rvProductos.setLayoutManager(new GridLayoutManager(this,1));
        rvProductos.setItemViewCacheSize(lstProductos.size());
        rvProductos.setAdapter(myadapter);


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog= new AlertDialog.Builder(mContext)
                        .setTitle("ELIMINAR")
                        .setMessage("¿Está seguro que desea eliminar esta lista??")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                borrarLista(id_lista);
                            }
                        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogEditList d = new DialogEditList(new Lista(id_lista,n_lista));
                d.show(mng,null);
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mAct, AddMoreProducts.class);
                Productos.addMore=true;
                i.putExtra("lista", id_lista);
                mAct.startActivity(i);
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mAct, ComprandoLista.class);
                Productos.addMore=true;
                i.putExtra("lista", id_lista);
                mAct.startActivity(i);
            }
        });
    }

    private void borrarLista(int id) {
        DBHelper conn = new DBHelper(this,"bdsmartlist",null,DBHelper.DBVersion);
        boolean res = conn.deleteList(id);
        if(res){
            Toast.makeText(mContext,"LISTA ELIMINADA",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(mContext,"ERROR AL BORRAR",Toast.LENGTH_LONG).show();
        }
        finish();
    }

    public static void deleteItem(int adapterPosition) {
        int pid = lstProductos.get(adapterPosition).getID();
        DBHelper conn = new DBHelper(mContext,"bdsmartlist",null,DBHelper.DBVersion);
        boolean res = conn.deleteProductList(id_lista,pid);

        if(res){
            Toast.makeText(mContext,"BORRADO CON EXITO",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(mContext,"ERROR AL BORRAR",Toast.LENGTH_SHORT).show();
        }
        refreshItems();
    }

    private static void refreshItems() {
        DBHelper conn = new DBHelper(mContext,"bdsmartlist",null,DBHelper.DBVersion);
        lstProductos = conn.getProductoLista(id_lista);
        myadapter = new ProductoListaAdapter(mContext,lstProductos);
        rvProductos.setLayoutManager(new GridLayoutManager(mContext,1));
        rvProductos.setItemViewCacheSize(lstProductos.size());
        rvProductos.setAdapter(myadapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshItems();
    }
}