/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.alejandro.logica;

import com.alejandro.modelo.Pedido;
import com.alejandro.modelo.Producto;

/**
 *
 * @author jalm2
 */
public interface PedidoLogica {

    boolean agregarProducto(Pedido pedido, Producto producto);

    boolean eliminarProducto(Pedido pedido, int indice);

    double calcularTotal(Pedido pedido);

    boolean puedeModificar(Pedido pedido);

}
