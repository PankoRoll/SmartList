package com.example.smartlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;

public class DialogPresupuesto extends DialogFragment {


    EditText etPresupuesto;
    Button btnCancel;
    Button btnSave;
    CheckBox cbOnOff;
    SwitchCompat sSwitch;

    public DialogPresupuesto() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Context auxContext = this.getContext();
        View v = inflater.inflate(R.layout.dialog_presupuesto,container,false);
        sSwitch = (SwitchCompat) v.findViewById(R.id.sSwitch);
        etPresupuesto = (EditText) v.findViewById(R.id.etPresupuesto);
        if(!ComprandoLista.budgetOn){
            sSwitch.setChecked(false);
            etPresupuesto.setEnabled(false);
            etPresupuesto.setFocusableInTouchMode(false);
        }
        else{
            String budget=""+ComprandoLista.budget;
            sSwitch.setChecked(true);
            etPresupuesto.setEnabled(true);
            etPresupuesto.setFocusableInTouchMode(true);
            etPresupuesto.setText(budget);
        }


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
                if(sSwitch.isChecked()){
                    if(!etPresupuesto.getText().toString().isEmpty()){
                        if(!(Float.parseFloat(etPresupuesto.getText().toString())==0)){
                            ComprandoLista.budgetOn=true;
                            ComprandoLista.budget = Float.parseFloat(etPresupuesto.getText().toString());
                        }
                        else{
                            Toast.makeText(getContext(),"NO PUEDE SER CERO",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getContext(),"INSERTE UNA CANTIDAD",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    ComprandoLista.budgetOn=false;
                    ComprandoLista.budget=0;
                    ComprandoLista.checkPresupuesto();
                }
                closeDialog();
            }
        });


        sSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    etPresupuesto.setEnabled(false);
                    etPresupuesto.setFocusableInTouchMode(false);
                    etPresupuesto.setText("");
                }
                else{
                    etPresupuesto.setEnabled(true);
                    etPresupuesto.setFocusableInTouchMode(true);
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
        ActiveList.tvPresupuesto.setText(ComprandoLista.getPresupuesto());
        ComprandoLista.checkPresupuesto();
        super.onDestroyView();
    }


}
