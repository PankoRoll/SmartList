package com.example.smartlist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ConcurrentModificationException;
import java.util.List;

public class Listas extends Fragment implements View.OnLongClickListener{

    FloatingActionButton fabNewList;
    public static boolean actionMode;
    private static RecyclerView rvListas;
    protected static List<Lista> lstListas;
    static ListRecyclerAdapter myAdapter;
    static Context mContext;
    static Listas mFragment;
    private static Activity mAct;
    androidx.appcompat.widget.Toolbar toolbar;



    public Listas() {
        // Required empty public constructor
    }

    public static void OpenList(int position) {
        int id_lista = lstListas.get(position).getId();
        String nombre = lstListas.get(position).getNombre();
        Intent i = new Intent(mAct, DetalleLista.class);
        i.putExtra("lista", id_lista);
        i.putExtra("nombre", nombre);
        mAct.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mFragment = this;
        mAct = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentManager mng = getFragmentManager();
        Fragment fAux = this;
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_listas, container, false);
        DBHelper conn = new DBHelper(this.getContext(),"bdsmartlist",null,DBHelper.DBVersion);

        toolbar = (androidx.appcompat.widget.Toolbar) v.findViewById(R.id.tbListas);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        lstListas = conn.getAllLists();

        rvListas = (RecyclerView) v.findViewById(R.id.rvListas);
        myAdapter = new ListRecyclerAdapter(this.getContext(),lstListas,this);
        rvListas.setLayoutManager(new GridLayoutManager(this.getContext(),1));
        rvListas.setItemViewCacheSize(lstListas.size());
        rvListas.setAdapter(myAdapter);
        // FLOATING ACTION BUTTON NEW LIST
        fabNewList = (FloatingActionButton) v.findViewById(R.id.fabNewList);
        fabNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNewList d = new DialogNewList();
                d.show(mng,null);
                refreshLists();
            }
        });
        return v;
    }

    public static void refreshLists() {
        DBHelper conn = new DBHelper(mContext,"bdsmartlist",null,DBHelper.DBVersion);
        lstListas = conn.getAllLists();

        myAdapter = new ListRecyclerAdapter(mContext,lstListas,mFragment);
        rvListas.setLayoutManager(new GridLayoutManager(mContext,1));
        rvListas.setItemViewCacheSize(lstListas.size());
        rvListas.setAdapter(myAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshLists();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public void prepareSelection(View v, int adapterPosition) {
    }
}