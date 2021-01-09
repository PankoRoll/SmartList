package com.example.smartlist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class AgregarProducto extends AppCompatActivity {

    TextView tvBarcode;
    Button btnCancel;
    Button btnSave;
    EditText etCantidad;
    EditText etNombre;
    EditText etPrecio;
    EditText etURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context auxContext = this.getBaseContext();

        String barcode = getIntent().getStringExtra("barcode");
        setContentView(R.layout.activity_agregar_producto);
        tvBarcode = findViewById(R.id.tvTitle);

        etCantidad = (EditText) findViewById(R.id.etCantidad);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etPrecio = (EditText) findViewById(R.id.etPrecio);
        etURL = (EditText) findViewById(R.id.etImagen);
        //ivProducto = (ImageView) v.findViewById(R.id.ivProducto);

        btnCancel = (Button) findViewById(R.id.btnClose);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNombre.getText().toString().isEmpty() || etCantidad.getText().toString().isEmpty()||
                        etPrecio.getText().toString().isEmpty() || etURL.getText().toString().isEmpty()){
                    Toast.makeText(auxContext,"LLENA TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }
                else{
                    DBHelper db = new DBHelper(auxContext,"bdsmartlist",null,DBHelper.DBVersion);
                    Producto p = new Producto(barcode,etNombre.getText().toString(),1,etURL.getText().toString(),etCantidad.getText().toString(),etPrecio.getText().toString());
                    boolean res = db.addProduct(p);

                    if(res)
                        Toast.makeText(auxContext,"Agregado con éxito", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(auxContext,"EL PRODUCTO YA EXISTE", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}