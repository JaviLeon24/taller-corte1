/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.controlador;

import com.alejandro.gestorArchivos.GestorArchivoProducto;
import com.alejandro.logica.PedidoLogica;
import com.alejandro.logica.implementacion.PedidoImpl;
import com.alejandro.modelo.Mesa;
import com.alejandro.modelo.Mesero;
import com.alejandro.modelo.Pedido;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jalm2
 */
public class ReciboControlador {

    private final PedidoLogica pedidoLogica;
    private final CocinaControlador cocinaControlador;
    private final List<Mesa> mesas;
    private final Map<Integer, Mesero> meseros;

    public ReciboControlador(List<Mesa> mesas, Map<Integer, Mesero> meseros, CocinaControlador cocinaControlador) {
        this.pedidoLogica = new PedidoImpl(mesas);
        this.cocinaControlador = cocinaControlador; 
        this.mesas = mesas;
        this.meseros = meseros;
    }

    public List<Pedido> obtenerPedidosListos() {
        return cocinaControlador.obtenerPedidosListos();
    }

    public double calcularTotal(Pedido pedido) {
        return pedidoLogica.calcularTotal(pedido);
    }

    public void generarRecibo(Pedido pedido) throws IOException {
        String carpeta = "data/pedidos";
        String nombreArchivo = "pedido_mesa_" + pedido.getMesa().getNumero() + "_" + System.currentTimeMillis() + ".csv";
        GestorArchivoProducto.guardarPedido(carpeta, nombreArchivo, pedido.getProductos());

        // Liberar mesa
        Mesa mesa = pedido.getMesa();
        mesa.liberar();

        // Quitar de mesero
        meseros.values().forEach(m -> m.getMesasAsignadas().remove(mesa));

        // Quitar de cocina
        cocinaControlador.removerPedido(pedido);
    }
}