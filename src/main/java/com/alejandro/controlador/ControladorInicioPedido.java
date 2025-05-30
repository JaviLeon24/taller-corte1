/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.controlador;

import com.alejandro.gestorArchivos.GestorArchivoProducto;
import com.alejandro.logica.PedidoLogica;
import com.alejandro.modelo.Mesa;
import com.alejandro.modelo.Mesero;
import com.alejandro.modelo.Pedido;
import com.alejandro.modelo.Producto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author jalm2
 */
public class ControladorInicioPedido {

    private final PedidoLogica pedidoLogica;
    private final List<Mesa> mesas;
    private final Map<Integer, Mesero> meseros;
    private final List<Producto> menu;
    private final CocinaControlador cocinaControlador;

    public ControladorInicioPedido(List<Mesa> mesas, Map<Integer, Mesero> meseros, CocinaControlador cocinaControlador, PedidoLogica pedidoLogica) {
        this.pedidoLogica = pedidoLogica;
        this.mesas = mesas;
        this.meseros = meseros;
        this.menu = cargarMenu();
        this.cocinaControlador = cocinaControlador;
    }

    private List<Producto> cargarMenu() {
        try {
            return GestorArchivoProducto.leerProductosDesdeArchivo("data/menu.csv");
        } catch (IOException e) {
            System.err.println("No se pudo cargar el menú: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Producto> obtenerMenu() {
        return menu;
    }

    public Map<Integer, Mesero> obtenerMeseros() {
        return meseros;
    }

    public List<Mesa> obtenerMesasDisponibles(int capacidad) {
        return mesas.stream()
                .filter(m -> !m.estaOcupada() && m.getCapacidad() >= capacidad)
                .toList();
    }

    public Mesa obtenerMesaPorNumero(int numero) {
        return mesas.stream()
                .filter(m -> m.getNumero() == numero)
                .findFirst()
                .orElse(null);
    }

    public Mesa asignarMesaPorCapacidad(int capacidad) {
        return mesas.stream()
                .filter(m -> m.getCapacidad() >= capacidad && !m.estaOcupada())
                .findFirst()
                .map(m -> {
                    m.ocupar();
                    return m;
                })
                .orElseThrow(() -> new RuntimeException("No hay mesas disponibles para esa capacidad."));
    }

    public Mesero asignarMeseroAleatorio() {
        List<Mesero> lista = new ArrayList<>(meseros.values());
        return lista.get(new Random().nextInt(lista.size()));
    }

    public void agregarProducto(Pedido pedido, Producto producto) {
        pedidoLogica.agregarProducto(pedido, producto);
    }

    public void eliminarProducto(Pedido pedido, int indice) {
        pedidoLogica.eliminarProducto(pedido, indice);
    }

    public void enviarPedidoACocina(Pedido pedido) {
        cocinaControlador.recibirPedido(pedido);
    }

    public PedidoLogica getPedidoLogica() {
        return this.pedidoLogica;
    }
}
