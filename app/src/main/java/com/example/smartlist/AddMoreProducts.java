package com.example.smartlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AddMoreProducts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_products);
        int id_lista = getIntent().getIntExtra("lista", -1);

        MoreProductos fragment = new MoreProductos();
        Bundle b = new Bundle();
        b.putInt("lista",id_lista);

        fragment.setArguments(b);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flMain, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.more_product_toolbar_menu_action,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final FragmentTransaction[] t = new FragmentTransaction[1];
        Fragment current = new Productos();
        AddMoreProducts act = this;

        if(item.getItemId()==R.id.iMoretoList){
            if(!(MoreProductos.lstSelected.size()==0)){
                MoreProductos.addProducts();
                finish();

            }
            else{
                Toast.makeText(this,"NO HAY NADA SELECCIONADO",Toast.LENGTH_LONG).show();
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}