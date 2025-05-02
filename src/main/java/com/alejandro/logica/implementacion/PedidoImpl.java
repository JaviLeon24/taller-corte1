/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.logica.implementacion;

import com.alejandro.logica.PedidoLogica;
import com.alejandro.modelo.EstadoPedido;
import com.alejandro.modelo.Pedido;
import com.alejandro.modelo.Producto;

/**
 *
 * @author jalm2
 */
public class PedidoImpl implements PedidoLogica {

    /**
     * Método para agregar productos a un pedido
     *
     * @param pedido
     * @param producto
     * @return
     */
    @Override
    public boolean agregarProducto(Pedido pedido, Producto producto) {
        if (pedido.getEstado() == EstadoPedido.EN_PREPARACION) {
            pedido.getProductos().add(producto);
            return true;
        }
        System.out.println("\nPedido agregado con éxito\n");
        return false;
    }

    /**
     * Método para eliminar productos de un pedido
     *
     * @param pedido
     * @param indice
     * @return
     */
    @Override
    public boolean eliminarProducto(Pedido pedido, int indice) {
        if (pedido.getEstado() != EstadoPedido.EN_PREPARACION) return false;
        if (indice >= 0 && indice < pedido.getProductos().size()) {
            pedido.getProductos().remove(indice);
            return true;
        }
        return false;
    }

    /**
     * Método que calcula el valor total de un pedido
     * @param pedido
     * @return 
     */
    @Override
    public double calcularTotal(Pedido pedido) {
        return pedido.getProductos().stream()
                .mapToDouble(Producto::getPrecio)
                .sum();
    }

    /**
     * Método que valida si se puede agregar o eliminar un producto a un pedido dependiendo de su estado
     * @param pedido
     * @return 
     */
    @Override
    public boolean puedeModificar(Pedido pedido) {
        return pedido.getEstado() == EstadoPedido.EN_PREPARACION;
    }
}
