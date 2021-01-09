package com.example.smartlist;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class EditarProducto extends AppCompatActivity {

    TextView tvBarcode;
    Button btnCancel;
    Button btnSave;
    EditText etCantidad;
    EditText etNombre;
    EditText etPrecio;
    EditText etURL;
    ImageView ivProducto;
    Producto editable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context auxContext = this.getBaseContext();

        int id = getIntent().getIntExtra("ID",-1);
        String barcode = getIntent().getStringExtra("barcode");
        String nombre = getIntent().getStringExtra("nombre");
        int tipo = getIntent().getIntExtra("tipo",-1);
        String imagen = getIntent().getStringExtra("imagen");
        String cantidad = getIntent().getStringExtra("cantidad");
        String precio = getIntent().getStringExtra("precio");
        editable = new Producto(id,barcode,nombre,tipo,imagen,cantidad,precio);

        setContentView(R.layout.activity_agregar_producto);
        tvBarcode = findViewById(R.id.tvTitle);
        tvBarcode.setText("EDITAR PRODUCTO");

        etCantidad = (EditText) findViewById(R.id.etCantidad);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etPrecio = (EditText) findViewById(R.id.etPrecio);
        etURL = (EditText) findViewById(R.id.etImagen);
        ivProducto = (ImageView) findViewById(R.id.ivProducto);

        etCantidad.setText(cantidad);
        etNombre.setText(nombre);
        etPrecio.setText(precio);
        etURL.setText(imagen);
        Picasso.get().load(imagen).into(ivProducto);


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
                    editable.setNombre(etNombre.getText().toString());
                    editable.setCantidad(etCantidad.getText().toString());
                    editable.setPrecio(etPrecio.getText().toString());
                    editable.setImagen(etURL.getText().toString());
                    boolean res = db.editProduct(editable);

                    if(res)
                        Toast.makeText(auxContext,"Modificado con éxito", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(auxContext,"Error al modificar", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}