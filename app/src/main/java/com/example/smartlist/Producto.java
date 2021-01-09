package com.example.smartlist;

public class Producto {
    private int ID;
    private String barcode;
    private String nombre;
    private int tipo;
    private String imagen;
    private String cantidad;
    private String precio;
    private int cartCount;



    public Producto() {
    }

    public void addCartCount(){
        cartCount++;
    }

    public void removeCartCount(){
        if (cartCount>1)
            cartCount--;
    }

    public int getCartCount() {
        return cartCount;
    }

    public void setCartCount(int cartCount) {
        this.cartCount = cartCount;
    }

    public Producto(int ID, String barcode, String nombre, int tipo, String imagen, String cantidad, String precio) {
        this.ID = ID;
        this.barcode = barcode;
        this.nombre = nombre;
        this.tipo = tipo;
        this.imagen = imagen;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public Producto(String barcode, String nombre, int tipo, String imagen, String cantidad, String precio) {
        this.barcode = barcode;
        this.nombre = nombre;
        this.tipo = tipo;
        this.imagen = imagen;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imaagen) {
        this.imagen = imaagen;
    }

}
