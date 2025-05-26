/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.logica.implementacion;

import com.alejandro.logica.CocinaLogica;
import com.alejandro.logica.PedidoLogica;
import com.alejandro.modelo.EstadoPedido;
import com.alejandro.modelo.Pedido;
import java.util.List;

/**
 *
 * @author jalm2
 */
public class CocinaImpl implements CocinaLogica {

    private final List<Pedido> pedidos;

    public CocinaImpl(PedidoLogica pedidoLogica) {
        this.pedidos = pedidoLogica.obtenerPedidos();
    }

    @Override
    public void recibirPedido(Pedido pedido) {
        pedido.setEstado(EstadoPedido.EN_PREPARACION);
        pedidos.add(pedido);
    }

    @Override
    public List<Pedido> getPedidosEnPreparacion() {
        return pedidos.stream()
                .filter(p -> p.getEstado() == EstadoPedido.EN_PREPARACION)
                .toList();
    }

    @Override
    public List<Pedido> getPedidosListos() {
        return pedidos.stream()
                .filter(p -> p.getEstado() == EstadoPedido.LISTO)
                .toList();
    }

    @Override
    public void marcarPedidoComoListo(int numeroMesa) {
        for (Pedido pedido : pedidos) {
            if (pedido.getMesa().getNumero() == numeroMesa && pedido.getEstado() == EstadoPedido.EN_PREPARACION) {
                pedido.setEstado(EstadoPedido.LISTO);
                break;
            }
        }
    }

    @Override
    public void removerPedido(Pedido pedido) {
        pedidos.remove(pedido);
    }
}
