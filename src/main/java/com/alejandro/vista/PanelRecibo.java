/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.vista;

import com.alejandro.controlador.ReciboControlador;
import com.alejandro.modelo.Pedido;
import com.alejandro.modelo.Producto;
import java.awt.BorderLayout;
import java.awt.Font;
import java.io.IOException;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author jalm2
 */
public class PanelRecibo extends JPanel {

    private final ReciboControlador controlador;
    private final DefaultListModel<String> modeloLista;
    private final JList<String> listaPedidos;
    private List<Pedido> pedidos;

    public PanelRecibo(ReciboControlador controlador) {
        this.controlador = controlador;
        this.modeloLista = new DefaultListModel<>();
        this.listaPedidos = new JList<>(modeloLista);

        setLayout(new BorderLayout());
        add(new JScrollPane(listaPedidos), BorderLayout.CENTER);

        JButton btnGenerar = new JButton("Generar Recibo");
        btnGenerar.addActionListener(e -> generarRecibo());
        add(btnGenerar, BorderLayout.SOUTH);
    }

    public void refrescarPedidos() {
        modeloLista.clear();
        pedidos = controlador.obtenerPedidosListos();
        for (Pedido p : pedidos) {
            modeloLista.addElement("Mesa " + p.getMesa().getNumero() + " - " + p.getProductos().size() + " productos");
        }
    }

    private void generarRecibo() {
        int index = listaPedidos.getSelectedIndex();
        if (index != -1) {
            Pedido pedido = pedidos.get(index);
            try {
                controlador.generarRecibo(pedido);
                mostrarReciboEnPantalla(pedido); 
                JOptionPane.showMessageDialog(this, "Recibo generado.");
                refrescarPedidos();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error generando recibo: " + ex.getMessage());
            }
        }
    }

    private void mostrarReciboEnPantalla(Pedido pedido) {
        StringBuilder sb = new StringBuilder();
        sb.append("========= RECIBO =========\n");
        sb.append("🪑 Mesa: ").append(pedido.getMesa().getNumero()).append("\n");
        sb.append("===========================\n");

        for (Producto p : pedido.getProductos()) {
            sb.append(String.format("• %-20s $%.2f\n", p.descripcion(), p.getPrecio()));
        }

        sb.append("===========================\n");
        sb.append(String.format("TOTAL: %22s $%.2f\n", "", controlador.calcularTotal(pedido)));
        sb.append("===========================\n");
        sb.append("¡Gracias por su visita!");

        JTextArea area = new JTextArea(sb.toString());
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        area.setEditable(false);

        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Recibo", JOptionPane.INFORMATION_MESSAGE);
    }
}
