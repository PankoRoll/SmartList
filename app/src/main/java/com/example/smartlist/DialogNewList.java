package com.example.smartlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.squareup.picasso.Picasso;

public class DialogNewList extends DialogFragment {


    EditText etNombre;
    Button btnCancel;
    Button btnSave;

    public DialogNewList() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Context auxContext = this.getContext();
        View v = inflater.inflate(R.layout.dialog_list_name,container,false);

        etNombre = (EditText) v.findViewById(R.id.etNombreLista);

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
                if(etNombre.getText().toString().isEmpty()){
                    Toast.makeText(auxContext,"LLENA TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }
                else{
                    DBHelper db = new DBHelper(auxContext,"bdsmartlist",null,DBHelper.DBVersion);
                    Lista l = new Lista(etNombre.getText().toString());

                    boolean res = db.addList(l);

                    if(res)
                        Toast.makeText(auxContext,"Agregada con éxito", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(auxContext,"LA LISTA YA EXISTE", Toast.LENGTH_SHORT).show();
                    closeDialog();
                    Listas.refreshLists();
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
        super.onDestroyView();
    }


}
