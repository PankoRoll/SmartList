package com.example.smartlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogEditList extends DialogFragment {


    EditText etNombre;
    Button btnCancel;
    Button btnSave;
    Lista lista;

    public DialogEditList(Lista lista) {
        this.lista = lista;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Context auxContext = this.getContext();
        View v = inflater.inflate(R.layout.dialog_list_name,container,false);

        etNombre = (EditText) v.findViewById(R.id.etNombreLista);
        etNombre.setText(lista.getNombre());

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
                    Lista l = new Lista(lista.getId(), etNombre.getText().toString());

                    boolean res = db.editList(l);

                    if(res){
                        Toast.makeText(auxContext,"Editada con éxito", Toast.LENGTH_SHORT).show();
                        DetalleLista.updateToolbar();
                    }

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
