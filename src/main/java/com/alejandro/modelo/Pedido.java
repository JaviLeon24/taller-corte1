/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jalm2
 */
public class Pedido {

    private Mesa mesa;
    private List<Producto> productos;

    public Pedido(Mesa mesa) {
        this.mesa = mesa;
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }
    
    public void eliminarProducto(int indice) {
        if (indice >= 0 && indice < productos.size()) {
            productos.remove(indice);
        } else {
            System.out.println("Índice de producto inválido.");
        }
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public double calcularTotal() {
        return productos.stream().mapToDouble(Producto::getPrecio).sum();
    }
}

