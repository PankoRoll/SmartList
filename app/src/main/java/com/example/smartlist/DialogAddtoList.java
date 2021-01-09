package com.example.smartlist;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

public class DialogAddtoList extends DialogFragment {


    ListView lvListas;
    List<Lista> lstListas ;
    List<String> lstShow ;
    Button btnCancel;
    boolean res;


    public DialogAddtoList() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.dialog_add_tolist,container,false);
        Context aContext = this.getContext();
        lvListas = (ListView) v.findViewById(R.id.lvListas);
        btnCancel = (Button) v.findViewById(R.id.btnClose);
        res=false;
        lstShow = new ArrayList<>();
        lstListas = new ArrayList<>();
        DBHelper conn = new DBHelper(this.getContext(),"bdsmartlist",null,DBHelper.DBVersion);
        lstListas = conn.getAllLists();

        for (int i=0;i<lstListas.size();i++)
            lstShow.add(lstListas.get(i).getNombre());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDialog();
            }
        });

        lvListas.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(int i=0;i<Productos.lstSelected.size();i++){
                    res = conn.addProductToList(lstListas.get(position).getId(),Productos.lstSelected.get(i).getID());
                    if(!res)
                        Toast.makeText(aContext,"EL PRODUCTO YA ESTA EN LA LISTA",Toast.LENGTH_LONG).show();
                }
                closeDialog();
                Productos.refreshItems(false);
                Productos.lstSelected=new ArrayList<>();
                if(res)
                    Toast.makeText(aContext,"ELEMENTOS AGREGADOS",Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this.getContext(),
                android.R.layout.simple_list_item_1,
                lstShow){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                return v;
            }
        };

        lvListas.setAdapter(arrayAdapter);
        return v;
    }

    private void closeDialog() {
        this.dismiss();
    }

}
