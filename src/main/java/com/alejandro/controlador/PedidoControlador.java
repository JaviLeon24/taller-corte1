/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.controlador;

import com.alejandro.modelo.Mesa;
import com.alejandro.modelo.Pedido;
import com.alejandro.vista.PanelPedidos;
import com.alejandro.vista.VentanaPrincipal;

/**
 *
 * @author jalm2
 */
public class PedidoControlador {

    private final PanelPedidos vista;
    private final VentanaPrincipal ventana;
    private final Pedido pedido;

    public PedidoControlador(PanelPedidos vista, VentanaPrincipal ventana, Pedido pedido) {
        this.vista = vista;
        this.ventana = ventana;
        this.pedido = pedido;

        Mesa mesa = pedido.getMesa();
        if (mesa.getPedido() == null) {
            mesa.asignarPedido(pedido);
            mesa.ocupar();
        }
    }
}

