package com.example.smartlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.security.PrivateKey;

public class DialogSearchOK extends DialogFragment {

    private Producto p;
    EditText etCantidad;
    EditText etNombre;
    EditText etPrecio;
    EditText etURL;
    ImageView ivProducto;
    Button btnCancel;
    Button btnSave;

    public DialogSearchOK(Producto p) {
        this.p = p;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Context auxContext = this.getContext();
        View v = inflater.inflate(R.layout.dialog_searchok,container,false);

        // OBTENER VISTAS Y ASIGNARLES VALORES DEL OBJETO
        etCantidad = (EditText) v.findViewById(R.id.etCantidad);
        etNombre = (EditText) v.findViewById(R.id.etNombre);
        etPrecio = (EditText) v.findViewById(R.id.etPrecio);
        etURL = (EditText) v.findViewById(R.id.etImagen);
        ivProducto = (ImageView) v.findViewById(R.id.ivProducto);

        etNombre.setText(p.getNombre());
        etCantidad.setText(p.getCantidad());
        //etPrecio.setText(p.getNombre());
        etURL.setText(p.getImagen());
        if(!p.getImagen().isEmpty())
            Picasso.get().load(p.getImagen()).into(ivProducto);

        // ACCIONES DE LOS BOTONES ACEPTAR Y CANCELAR
        btnCancel = (Button) v.findViewById(R.id.btnClose);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });
        btnSave = (Button) v.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNombre.getText().toString().isEmpty() || etCantidad.getText().toString().isEmpty()||
                    etPrecio.getText().toString().isEmpty() || etURL.getText().toString().isEmpty()){
                    Toast.makeText(auxContext,"LLENA TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }
                else{
                    DBHelper db = new DBHelper(auxContext,"bdsmartlist",null,DBHelper.DBVersion);
                    p.setPrecio(etPrecio.getText().toString());
                    p.setImagen(etURL.getText().toString());
                    p.setNombre(etNombre.getText().toString());
                    p.setCantidad(etCantidad.getText().toString());
                    boolean res = db.addProduct(p);

                    if(res)
                        Toast.makeText(auxContext,"Agregado con éxito", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(auxContext,"EL PRODUCTO YA EXISTE", Toast.LENGTH_SHORT).show();
                    closeDialog();
                }
            }
        });


        return v;
    }



    private void closeDialog() {
        this.dismiss();
    }

    @Override
    public void onDestroyView() {
        p = null;
        super.onDestroyView();
    }
}
