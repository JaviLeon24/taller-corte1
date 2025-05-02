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
    private EstadoPedido estado;

    public Pedido(Mesa mesa) {
        this.mesa = mesa;
        this.productos = new ArrayList<>();
        this.estado = EstadoPedido.EN_PREPARACION;
    }

    /**
     * Método para agregar productos a un pedido
     * @param producto
     * @return 
     */
    public boolean agregarProducto(Producto producto) {
        if (estado == EstadoPedido.EN_PREPARACION) {
            productos.add(producto);
            return true;
        }
        System.out.println("\nPedido agregado con éxito\n");
        return false;
    }

    /**
     * Método para eliminar productos de un pedido
     * @param indice
     * @return 
     */
    public boolean eliminarProducto(int indice) {
        if (estado != EstadoPedido.EN_PREPARACION) return false;
        if (indice >= 0 && indice < productos.size()) {
            productos.remove(indice);
            return true;
        }
        return false;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public double calcularTotal() {
        return productos.stream().mapToDouble(Producto::getPrecio).sum();
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public boolean puedeModificar() {
        return estado == EstadoPedido.EN_PREPARACION;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }
    
    
}
