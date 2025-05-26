/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.vista;

import com.alejandro.controlador.CocinaControlador;
import com.alejandro.logica.PedidoLogica;
import com.alejandro.logica.implementacion.PedidoImpl;
import com.alejandro.modelo.Mesa;
import com.alejandro.modelo.Pedido;
import com.alejandro.modelo.Producto;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author jalm2
 */
public class PanelPedidos extends JPanel {

    private final Mesa mesa;
    private final Pedido pedido;
    private final List<Producto> menu;
    private final CocinaControlador cocinaControlador;
    private final PedidoLogica pedidoLogica;

    private final JComboBox<String> comboProductos;
    private final DefaultListModel<String> modeloLista;
    private final JList<String> listaProductos;

    public PanelPedidos(Mesa mesa, List<Producto> menu, CocinaControlador cocinaControlador) {
        this.mesa = mesa;
        this.pedido = mesa.getPedido();
        this.menu = menu;
        this.cocinaControlador = cocinaControlador;
        this.pedidoLogica = new PedidoImpl();

        setLayout(new BorderLayout());

        comboProductos = new JComboBox<>();
        for (Producto p : menu) {
            comboProductos.addItem(p.descripcion() + " - $" + p.getPrecio());
        }

        modeloLista = new DefaultListModel<>();
        listaProductos = new JList<>(modeloLista);
        actualizarLista();

        JPanel panelTop = new JPanel(new FlowLayout());
        JButton btnAgregar = new JButton("Agregar Producto");
        JButton btnEliminar = new JButton("Eliminar Producto");
        JButton btnFinalizar = new JButton("Confirmar y Enviar a Cocina");

        panelTop.add(comboProductos);
        panelTop.add(btnAgregar);
        panelTop.add(btnEliminar);
        panelTop.add(btnFinalizar);

        add(panelTop, BorderLayout.NORTH);
        add(new JScrollPane(listaProductos), BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> agregarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnFinalizar.addActionListener(e -> confirmarPedido());
    }

    private void actualizarLista() {
        modeloLista.clear();
        for (Producto p : pedido.getProductos()) {
            modeloLista.addElement(p.descripcion() + " - $" + p.getPrecio());
        }
    }

    private void agregarProducto() {
        if (!pedidoLogica.puedeModificar(pedido)) {
            JOptionPane.showMessageDialog(this, "No se puede modificar un pedido en estado: " + pedido.getEstado());
            return;
        }
        int index = comboProductos.getSelectedIndex();
        if (index >= 0) {
            pedidoLogica.agregarProducto(pedido, menu.get(index));
            actualizarLista();
        }
    }

    private void eliminarProducto() {
        if (!pedidoLogica.puedeModificar(pedido)) {
            JOptionPane.showMessageDialog(this, "No se puede modificar un pedido en estado: " + pedido.getEstado());
            return;
        }
        int index = listaProductos.getSelectedIndex();
        if (index >= 0) {
            pedidoLogica.eliminarProducto(pedido, index);
            actualizarLista();
        }
    }

    private void confirmarPedido() {
        cocinaControlador.recibirPedido(pedido);
        JOptionPane.showMessageDialog(this, "Pedido enviado a cocina.");
    }
}