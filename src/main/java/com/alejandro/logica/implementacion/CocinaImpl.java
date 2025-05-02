/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.logica.implementacion;

import com.alejandro.logica.CocinaLogica;
import com.alejandro.modelo.EstadoPedido;
import com.alejandro.modelo.Pedido;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jalm2
 */
public class CocinaImpl implements CocinaLogica {
    private static final List<Pedido> pedidosEnPreparacion = new ArrayList<>();
    private static final List<Pedido> pedidosListos = new ArrayList<>();

    /**
     * Método para recibir en COCINA los pedidos realizados por los meseros
     *
     * @param pedido
     */
    @Override
    public void recibirPedido(Pedido pedido) {
        if (pedido.getEstado() == EstadoPedido.EN_PREPARACION) {
            pedidosEnPreparacion.add(pedido);
            System.out.println("Pedido de la mesa " + pedido.getMesa().getNumero() + " recibido en cocina.");
        }
    }

    /**
     * Método que permite marcar un pedido en estado LISTO desde la cocina
     *
     * @param numeroMesa
     */
    @Override
    public void marcarPedidoComoListo(int numeroMesa) {
        Pedido pedido = pedidosEnPreparacion.stream()
                .filter(p -> p.getMesa().getNumero() == numeroMesa)
                .findFirst()
                .orElse(null);

        if (pedido != null) {
            pedido.setEstado(EstadoPedido.LISTO);
            pedidosEnPreparacion.remove(pedido);
            pedidosListos.add(pedido);
            System.out.println("Pedido de la mesa " + numeroMesa + " está listo para ser entregado.");
        } else {
            System.out.println("No se encontró pedido en preparación para la mesa " + numeroMesa);
        }
    }

    @Override
    public void removerPedido(Pedido pedido) {
        pedidosListos.remove(pedido);
    }

    @Override
    public List<Pedido> getPedidosListos() {
        return new ArrayList<>(pedidosListos);
    }

    @Override
    public List<Pedido> getPedidosEnPreparacion() {
        return new ArrayList<>(pedidosEnPreparacion);
    }
}
