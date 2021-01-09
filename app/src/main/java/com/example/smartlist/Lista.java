package com.example.smartlist;

public class Lista {
    private int id;
    private String nombre;
    private int itemCount;

    public Lista(int id, String nombre, int itemCount) {
        this.id = id;
        this.nombre = nombre;
        this.itemCount = itemCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Lista(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Lista(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}