package com.alejandro.modelo;

import java.util.ArrayList;
import java.util.List;

public class Cocina {

    private static final List<Pedido> pedidosEnPreparacion = new ArrayList<>();
    private static final List<Pedido> pedidosListos = new ArrayList<>();

    /**
     * Método para recibir en COCINA los pedidos realizados por los meseros
     *
     * @param pedido
     */
    public static void recibirPedido(Pedido pedido) {
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
    public static void marcarPedidoComoListo(int numeroMesa) {
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

    public static void removerPedido(Pedido pedido) {
        pedidosListos.remove(pedido);
    }

    public static List<Pedido> getPedidosListos() {
        return new ArrayList<>(pedidosListos);
    }

    public static List<Pedido> getPedidosEnPreparacion() {
        return new ArrayList<>(pedidosEnPreparacion);
    }
}
