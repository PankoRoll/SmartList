package com.example.smartlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    final String CREAR_TABLA_PRODUCTOS = "CREATE TABLE PRODUCTOS(id INTEGER PRIMARY KEY AUTOINCREMENT, barcode TEXT UNIQUE, nombre TEXT, tipo INTEGER, imagen TEXT, cantidad TEXT, precio TEXT)";
    final String CREAR_TABLA_LISTAS = "CREATE TABLE LISTAS (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT UNIQUE)";
    final String CREAR_TABLA_PRODUCTO_LISTA = "CREATE TABLE PRODUCTO_LISTA(id_lista INTEGER, id_producto INTEGER, guid TEXT UNIQUE)";
    public static int DBVersion = 18;

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAR_TABLA_PRODUCTOS);
        db.execSQL(CREAR_TABLA_LISTAS);
        db.execSQL(CREAR_TABLA_PRODUCTO_LISTA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS PRODUCTOS");
        db.execSQL("DROP TABLE IF EXISTS LISTAS");
        db.execSQL("DROP TABLE IF EXISTS PRODUCTO_LISTA");
        onCreate(db);
    }

    public boolean addProduct(Producto p) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("barcode", p.getBarcode());
        cv.put("nombre", p.getNombre());
        cv.put("tipo", p.getTipo());
        cv.put("cantidad", p.getCantidad());
        cv.put("precio", p.getPrecio());
        cv.put("imagen", p.getImagen());

        if (db.insert("PRODUCTOS", null, cv) == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public List<Producto> getAllProducts() {
        List<Producto> resultado = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM PRODUCTOS", null);

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                String barcode = c.getString(1);
                String nombre = c.getString(2);
                int tipo = c.getInt(3);
                String imagen = c.getString(4);
                String cantidad = c.getString(5);
                String precio = c.getString(6);
                Producto r = new Producto(id, barcode, nombre, tipo, imagen, cantidad, precio);
                resultado.add(r);
            } while (c.moveToNext());
        } else {
            //fail
        }
        c.close();
        db.close();
        return resultado;
    }

    public boolean rmvProducts(List<Producto> lstSelected) {
        String query;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        for (int i = 0; i < lstSelected.size(); i++) {
            try {
                query = "DELETE FROM PRODUCTOS WHERE ID = " + lstSelected.get(i).getID();
                c = db.rawQuery(query, null);
                c.moveToFirst();
            } catch (SQLException e) {
                e.printStackTrace();
                db.close();
                return false;
            }
        }
        for (int i = 0; i < lstSelected.size(); i++) {
            try {
                query = "DELETE FROM PRODUCTO_LISTA WHERE ID_PRODUCTO = " + lstSelected.get(i).getID();
                c = db.rawQuery(query, null);
                c.moveToFirst();
            } catch (SQLException e) {
                e.printStackTrace();
                db.close();
                return false;
            }
        }
        db.close();
        return true;
    }

    public boolean editProduct(Producto editable) {
        String query;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;

        try {
            query = "UPDATE PRODUCTOS SET NOMBRE = \'" + editable.getNombre() + "\',CANTIDAD = \'" + editable.getCantidad() + "\',PRECIO = \'" + editable.getPrecio() + "\',IMAGEN = \'" + editable.getImagen() + "\' WHERE ID =" + editable.getID();
            c = db.rawQuery(query, null);
            c.moveToFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    public boolean addList(Lista l) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("nombre", l.getNombre());

        if (db.insert("LISTAS", null, cv) == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public List<Lista> getAllLists() {
        List<Lista> resultado = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM LISTAS", null);

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                String nombre = c.getString(1);
                Lista l = new Lista(id, nombre);
                resultado.add(l);
            } while (c.moveToNext());
        } else {
            //fail
        }

        for (int i = 0; i < resultado.size(); i++) {
            c = db.rawQuery("SELECT COUNT (*) FROM PRODUCTO_LISTA WHERE id_lista = " + resultado.get(i).getId(), null);
            if (c.moveToFirst()) {
                resultado.get(i).setItemCount(c.getInt(0));
            }
        }

        c.close();
        db.close();

        return resultado;
    }

    public boolean addProductToList(int lista, int producto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String guid = lista + "x" + producto;

        cv.put("id_lista", lista);
        cv.put("id_producto", producto);
        cv.put("guid", guid);

        if (db.insert("PRODUCTO_LISTA", null, cv) == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public List<Producto> getProductoLista(int id_lista) {
        List<Producto> resultado = new ArrayList<>();
        List<Integer> encontrados = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM PRODUCTOS WHERE id IN (SELECT ID_PRODUCTO FROM PRODUCTO_LISTA WHERE ID_LISTA = " + id_lista + ")", null);

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                String barcode = c.getString(1);
                String nombre = c.getString(2);
                int tipo = c.getInt(3);
                String imagen = c.getString(4);
                String cantidad = c.getString(5);
                String precio = c.getString(6);
                Producto r = new Producto(id, barcode, nombre, tipo, imagen, cantidad, precio);
                resultado.add(r);
            } while (c.moveToNext());
        } else {
            //fail
        }
        c.close();
        db.close();
        return resultado;
    }

    public boolean deleteList(int id) {

        String query;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;

        try {
            query = "DELETE FROM PRODUCTO_LISTA WHERE ID_LISTA = " + id;
            c = db.rawQuery(query, null);
            c.moveToFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            db.close();
            return false;
        }

        try {
            query = "DELETE FROM LISTAS WHERE ID = " + id;
            c = db.rawQuery(query, null);
            c.moveToFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            db.close();
            return false;
        }
        c.close();
        db.close();
        return true;
    }

    public boolean editList(Lista l) {
        String query;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;

        try {
            query = "UPDATE LISTAS SET NOMBRE = \'" + l.getNombre() + "\' WHERE ID =" + l.getId();
            c = db.rawQuery(query, null);
            c.moveToFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    public boolean deleteProductList(int id_lista, int pid) {
        String query;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        String combo = id_lista + "x" + pid;

        try {
            query = "DELETE FROM PRODUCTO_LISTA WHERE guid = \'" + combo + "\'";
            c = db.rawQuery(query, null);
            c.moveToFirst();
        } catch (SQLException e) {
            e.printStackTrace();
            db.close();
            return false;
        }
        c.close();
        db.close();
        return true;
    }

    public List<Producto> getMissingProducts(int id_lista) {
        List<Producto> resultado = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM PRODUCTOS WHERE ID NOT IN(SELECT ID_PRODUCTO FROM PRODUCTO_LISTA WHERE ID_LISTA=" + id_lista + ")";
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                String barcode = c.getString(1);
                String nombre = c.getString(2);
                int tipo = c.getInt(3);
                String imagen = c.getString(4);
                String cantidad = c.getString(5);
                String precio = c.getString(6);
                Producto r = new Producto(id, barcode, nombre, tipo, imagen, cantidad, precio);
                resultado.add(r);
            } while (c.moveToNext());
        } else {
            //fail
        }
        c.close();
        db.close();
        return resultado;
    }

    public Producto searchByBarcode(String barcode) {
        Producto resultado;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM PRODUCTOS WHERE BARCODE = \'" + barcode + "\'", null);

        if (c.moveToFirst()) {

            int id = c.getInt(0);
            String nbarcode = c.getString(1);
            String nombre = c.getString(2);
            int tipo = c.getInt(3);
            String imagen = c.getString(4);
            String cantidad = c.getString(5);
            String precio = c.getString(6);
            resultado = new Producto(id, nbarcode, nombre, tipo, imagen, cantidad, precio);
            c.close();
            db.close();
            return resultado;

        } else {
            c.close();
            db.close();
            return null;
        }


    }

    public String getListName(int id_lista) {
        String resultado;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT NOMBRE FROM LISTAS WHERE ID ="+id_lista, null);

        if (c.moveToFirst()) {
            resultado = c.getString(0);
            return resultado;

        } else {
            c.close();
            db.close();
            return null;
        }
    }
}