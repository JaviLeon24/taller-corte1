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
    private List<Producto> productos;
    private double total;

    public Pedido() {
        this.productos = new ArrayList<>();
        this.total = 0;
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
        total += producto.getPrecio();
    }

    public double getTotal() {
        return total;
    }

    public void mostrarPedido() {
        System.out.println("Pedido actual:");
        for (Producto p : productos) {
            System.out.println(p);
        }
        System.out.println("Total: $" + total);
    }
}
