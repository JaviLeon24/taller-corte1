/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.vista;

import com.alejandro.controlador.CocinaControlador;
import com.alejandro.modelo.Pedido;
import com.alejandro.modelo.Producto;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author jalm2
 */
public final class PanelPedidosListos extends JPanel {

    private final CocinaControlador cocinaControlador;
    private final DefaultListModel<String> modelo;
    private final JList<String> lista;

    public PanelPedidosListos(CocinaControlador cocinaControlador) {
        this.cocinaControlador = cocinaControlador;

        setLayout(new BorderLayout());

        modelo = new DefaultListModel<>();
        lista = new JList<>(modelo);
        add(new JScrollPane(lista), BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarPedidosListos());

        add(btnActualizar, BorderLayout.SOUTH);
        cargarPedidosListos();
    }

    void cargarPedidosListos() {
        modelo.clear();
        List<Pedido> pedidos = cocinaControlador.obtenerPedidosListos();

        if (pedidos.isEmpty()) {
            modelo.addElement("No hay pedidos listos.");
            return;
        }

        for (Pedido pedido : pedidos) {
            StringBuilder descripcion = new StringBuilder();
            descripcion.append("Mesa ").append(pedido.getMesa().getNumero()).append(" - ");
            descripcion.append(pedido.getProductos().size()).append(" productos:\n");

            for (Producto p : pedido.getProductos()) {
                descripcion.append("• ").append(p.descripcion()).append(" - $").append(p.getPrecio()).append("\n");
            }

            modelo.addElement(descripcion.toString());
        }
    }
}
