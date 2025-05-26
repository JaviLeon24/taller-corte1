/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.controlador;

import com.alejandro.logica.CocinaLogica;
import com.alejandro.modelo.Pedido;
import java.util.List;

/**
 *
 * @author jalm2
 */
public class CocinaControlador {

    private final CocinaLogica cocinaLogica;

    public CocinaControlador(CocinaLogica cocinaLogica) {
        this.cocinaLogica = cocinaLogica;
    }

    public void recibirPedido(Pedido pedido) {
        cocinaLogica.recibirPedido(pedido);
    }

    public List<Pedido> obtenerPedidosEnPreparacion() {
        return cocinaLogica.getPedidosEnPreparacion();
    }

    public List<Pedido> obtenerPedidosListos() {
        return cocinaLogica.getPedidosListos();
    }

    public void marcarPedidoComoListo(int numeroMesa) {
        cocinaLogica.marcarPedidoComoListo(numeroMesa);
    }

    public void removerPedido(Pedido pedido) {
        cocinaLogica.removerPedido(pedido);
    }
}
