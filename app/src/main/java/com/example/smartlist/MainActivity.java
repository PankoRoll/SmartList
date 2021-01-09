package com.example.smartlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;

import static android.service.controls.ControlsProviderService.TAG;


public class MainActivity extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Bottom nav
        BottomNavigationView bnNavigation = findViewById(R.id.bnvNavigation);
        bnNavigation.setOnNavigationItemSelectedListener(navListener);
        bnNavigation.setSelectedItemId(R.id.iProducts);

        //Setting Home
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flMain, new Productos()).commit();
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
                        case(R.id.iProducts):
                            selectedFragment = new Productos();
                            break;
                        case(R.id.iLists):
                            selectedFragment = new Listas();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.flMain,selectedFragment).commit();
                    return true;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final boolean[] res = {false};
        final FragmentTransaction[] t = new FragmentTransaction[1];
        Fragment current = new Productos();
        MainActivity act = this;

        // DIALOGO CONFIRMAR Y ELIMINAR SELECCIONADOS
        if(item.getItemId()==R.id.iDeleteProduct){
            if(!(Productos.lstSelected.size()==0)){
                AlertDialog dialog= new AlertDialog.Builder(this)
                        .setTitle("ELIMINAR")
                        .setMessage("Está seguro que desea eliminar estos productos?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                res[0] = Productos.deleteSelected(act);
                                if(res[0]){
                                    t[0] = act.getSupportFragmentManager().beginTransaction();
                                    t[0].replace(R.id.flMain,current);
                                    t[0].detach(current);
                                    t[0].attach(current);
                                    t[0].commit();
                                }
                            }
                        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
            else{
                Toast.makeText(this,"NO HAY NADA SELECCIONADO",Toast.LENGTH_LONG).show();
            }
        }

        if(item.getItemId()==R.id.iAddtoList){
            if(!(Productos.lstSelected.size()==0)){
                DialogAddtoList d = new DialogAddtoList();
                d.show(getSupportFragmentManager(),null);
            }
            else{
                Toast.makeText(this,"NO HAY NADA SELECCIONADO",Toast.LENGTH_LONG).show();
            }
        }

        if(item.getItemId()==android.R.id.home){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flMain, new Productos()).commit();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(Productos.actionMode){
            Productos.exitActionMode();
        }
        else{
            finish();
        }
    }

}