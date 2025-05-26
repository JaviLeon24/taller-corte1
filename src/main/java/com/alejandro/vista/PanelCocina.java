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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author jalm2
 */
public final class PanelCocina extends JPanel {

    private final CocinaControlador cocinaControlador;
    private final DefaultListModel<String> modeloPedidos;
    private final JList<String> listaPedidos;
    private final JButton btnMarcarListo;

    private List<Pedido> pedidosEnPreparacion;

    public PanelCocina(CocinaControlador cocinaControlador) {
        this.cocinaControlador = cocinaControlador;
        setLayout(new BorderLayout());

        modeloPedidos = new DefaultListModel<>();
        listaPedidos = new JList<>(modeloPedidos);
        btnMarcarListo = new JButton("Marcar como Listo");

        add(new JScrollPane(listaPedidos), BorderLayout.CENTER);
        add(btnMarcarListo, BorderLayout.SOUTH);

        btnMarcarListo.addActionListener(e -> marcarPedidoComoListo());

        cargarPedidos();
    }

    void cargarPedidos() {
        modeloPedidos.clear();
        pedidosEnPreparacion = cocinaControlador.obtenerPedidosEnPreparacion();

        if (pedidosEnPreparacion.isEmpty()) {
            modeloPedidos.addElement("No hay pedidos en preparación.");
            btnMarcarListo.setEnabled(false);
        } else {
            for (Pedido p : pedidosEnPreparacion) {
                String descripcion = "Mesa " + p.getMesa().getNumero() + ": ";
                descripcion += p.getProductos().stream()
                        .map(Producto::descripcion)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("Sin productos");
                modeloPedidos.addElement(descripcion);
            }
            btnMarcarListo.setEnabled(true);
        }
    }

    private void marcarPedidoComoListo() {
        int index = listaPedidos.getSelectedIndex();
        if (index != -1 && !pedidosEnPreparacion.isEmpty()) {
            Pedido pedido = pedidosEnPreparacion.get(index);
            cocinaControlador.marcarPedidoComoListo(pedido.getMesa().getNumero());
            JOptionPane.showMessageDialog(this,
                    "Pedido de la mesa " + pedido.getMesa().getNumero() + " marcado como LISTO.");
            cargarPedidos();
        }
    }

}
