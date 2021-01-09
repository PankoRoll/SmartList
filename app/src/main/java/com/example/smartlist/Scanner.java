package com.example.smartlist;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import dalvik.system.BaseDexClassLoader;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.service.controls.ControlsProviderService.TAG;

public class Scanner extends Fragment implements ZXingScannerView.ResultHandler {
    Producto pResult;
    private ZXingScannerView scanView;
    public Scanner() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v =inflater.inflate(R.layout.fragment_scanner, container, false);
         scanView = (ZXingScannerView) v.findViewById(R.id.svScanner);
         scanView.setResultHandler(Scanner.this);
        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.getActivity(), new String[] {Manifest.permission.CAMERA}, 50);
        }
         scanView.startCamera();
         return v;
    }

    @Override
    public void onDestroy() {
        scanView.stopCamera();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        scanView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        sendResult(rawResult);
        scanView.setResultHandler(Scanner.this);
        scanView.startCamera();
    }

    private void sendResult(Result rawResult) {
        if(this.getActivity() instanceof MainActivity){
           searchItem(rawResult.getText());
        }
        if(this.getActivity() instanceof ComprandoLista){
            searchLocal(rawResult.getText());
        }
    }

    private void searchLocal(String barcode) {
        boolean found =false;
        Producto p;
        String encontrado;


        //BUSCAR EN EL CARRITO
        for(int i=0;i<ComprandoLista.lstCart.size();i++) {
            if (ComprandoLista.lstCart.get(i).getBarcode().equals(barcode)) {
                Toast.makeText(getContext(),"EL PRODUCTO YA ESTÁ EN EL CARRITO",Toast.LENGTH_SHORT).show();
                found = true;
            }
        }
        // BUSCAR EN LA LISTA DE COMPRAS
        if(!found){
            for(int i=0;i<ComprandoLista.lstProductos.size();i++){
                if(ComprandoLista.lstProductos.get(i).getBarcode().equals(barcode)){
                    int finalI = i;
                    p = ComprandoLista.lstProductos.get(i);
                    encontrado = p.getNombre() +" $" + p.getPrecio();
                    found=true;
                    AlertDialog dialog= new AlertDialog.Builder(this.getContext())
                            .setTitle(encontrado)
                            .setMessage("¿Quiere agregar este producto al carrito?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActiveList.itemToCart(finalI);

                                }
                            }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
            }
        }
        // BUSCAR EN TODOS LOS PRODUCTOS
        if(!found){
            DBHelper conn = new DBHelper(this.getContext(),"bdsmartlist",null,DBHelper.DBVersion);
            p = conn.searchByBarcode(barcode);
            if(p!=null){
                encontrado = p.getNombre() +" $" + p.getPrecio();
                found = true;
                Producto finalP = p;
                AlertDialog dialog= new AlertDialog.Builder(this.getContext())
                        .setTitle(encontrado)
                        .setMessage("Este producto no estaba en la lista\n¿Quiere agregar este producto al carrito?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActiveList.itemToCart(finalP);

                            }
                        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        }
        // BUSCAR EN INTERNET
        if(!found){
            searchItem(barcode);
        }
    }

    private void searchItem(String rawResult) {
        RequestQueue mQueue = Volley.newRequestQueue(this.getContext());
        final DialogSearchOK[] dialog = new DialogSearchOK[1];
        FragmentManager mng = getChildFragmentManager();


        String url = "https://world.openfoodfacts.org/api/v0/product/"+rawResult+".json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONObject prod = response.getJSONObject("product");
                            String nombre = prod.optString("product_name");
                            String imagen = prod.optString("image_front_url");
                            String cantidad = prod.optString("quantity");
                            pResult = new Producto(rawResult,nombre,1,imagen,cantidad,null);
                            dialog[0] = new DialogSearchOK(pResult);
                            dialog[0].show(mng,null);
                        }catch (JSONException e){
                            e.printStackTrace();
                            nuevoProducto(rawResult);
                        }
                    }
                },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void nuevoProducto(String barcode) {
        Intent i = new Intent(this.getActivity().getBaseContext(), AgregarProducto.class);
        i.putExtra("barcode", barcode);
        startActivity(i);
    }
}