package com.example.smartlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ComprandoLista extends AppCompatActivity {

    static int id_lista;
    public static List<Producto> lstProductos;
    public static List<Producto> lstCart;
    public static boolean budgetOn;
    static float suma;
    static float budget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprando_lista);
        id_lista = getIntent().getIntExtra("lista", -1);
        //Bottom nav
        Fragment fLista = new ActiveList();
        suma = 0;
        budgetOn = false;
        budget=0;
        Bundle b = new Bundle();
        b.putInt("lista",id_lista);
        fLista.setArguments(b);

        lstCart = new ArrayList<>();
        DBHelper conn = new DBHelper(this,"bdsmartlist",null,DBHelper.DBVersion);
        ComprandoLista.lstProductos = conn.getProductoLista(id_lista);

        BottomNavigationView bnNavigation = findViewById(R.id.bnvNavigation);
        bnNavigation.setOnNavigationItemSelectedListener(navListener);
        bnNavigation.setSelectedItemId(R.id.iList);

        //Setting Home
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flMain, fLista).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new
            BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch(item.getItemId()){
                        case (R.id.iScan):
                            selectedFragment = new Scanner();
                            break;
                        case(R.id.iList):
                            Fragment fLista = new ActiveList();
                            Bundle b = new Bundle();
                            b.putInt("lista",id_lista);
                            fLista.setArguments(b);
                            selectedFragment = fLista;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flMain,selectedFragment).commit();
                    return true;
                }
            };

    public static String updateSuma(){
        float acumula=0;
        float actual;

        if(lstCart.size()==0)
            return "$0";

        for(int i=0;i<lstCart.size();i++){
            actual = Float.parseFloat(lstCart.get(i).getPrecio())*lstCart.get(i).getCartCount();
            acumula += actual;
        }
        suma=acumula;
        String res = "$"+acumula;
        return res;
    }

    public static String getPresupuesto(){
        if(!budgetOn){
            return "Sin límite";
        }
        else{
            return "$"+budget;
        }
    }

    public static void checkPresupuesto(){
        if(budgetOn){
            if(budget<suma){
                ActiveList.tvTotal.setTextColor(Color.RED);
            }
            else{
                ActiveList.tvTotal.setTextColor(Color.BLACK);
            }
        }
        else{
            ActiveList.tvTotal.setTextColor(Color.BLACK);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog dialog= new AlertDialog.Builder(this)
                .setTitle("CUIDADO!")
                .setMessage("Si sales, se perderá tu carrito.\n¿Quieres salir?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();

                    }
                }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

}