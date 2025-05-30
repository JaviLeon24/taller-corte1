/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.vista;

import com.alejandro.logica.PedidoLogica;
import com.alejandro.logica.implementacion.PedidoImpl;
import com.alejandro.modelo.Mesa;
import com.alejandro.modelo.Pedido;
import com.alejandro.modelo.Producto;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author jalm2
 */
public class PanelConsultaMesa extends JPanel {

    private final List<Mesa> mesas;
    private final JTextField campoNumeroMesa;
    private final JTextArea areaResultado;
    private final PedidoLogica pedidoLogica;
    private JComboBox<String> comboMesas;

    public PanelConsultaMesa(List<Mesa> mesas) {
        this.mesas = mesas;
        this.pedidoLogica = new PedidoImpl(mesas);

        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new FlowLayout());
        campoNumeroMesa = new JTextField(5);
        JButton btnConsultar = new JButton("Consultar Pedido");

        panelSuperior.add(new JLabel("Número de Mesa:"));
        panelSuperior.add(campoNumeroMesa);
        panelSuperior.add(btnConsultar);

        add(panelSuperior, BorderLayout.NORTH);

        areaResultado = new JTextArea(15, 50);
        areaResultado.setEditable(false);
        add(new JScrollPane(areaResultado), BorderLayout.CENTER);

        btnConsultar.addActionListener(e -> consultarPedido());
    }

    private void consultarPedido() {
        String entrada = campoNumeroMesa.getText();
        areaResultado.setText("");

        try {
            int numeroMesa = Integer.parseInt(entrada);
            Mesa mesa = mesas.stream()
                    .filter(m -> m.getNumero() == numeroMesa)
                    .findFirst()
                    .orElse(null);

            if (mesa == null) {
                areaResultado.setText("La mesa no existe.");
                return;
            }

            Pedido pedido = mesa.getPedido();
            if (!mesa.estaOcupada() || pedido == null) {
                areaResultado.setText("La mesa no tiene un pedido activo.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("✅ Pedido de la mesa ").append(mesa.getNumero()).append(":\n\n");
            for (Producto p : pedido.getProductos()) {
                sb.append("• ").append(p.descripcion()).append(" - $").append(p.getPrecio()).append("\n");
            }

            double total = pedidoLogica.calcularTotal(pedido);
            sb.append("\nTotal Provisional: $").append(total);
            sb.append("\nEstado: ").append(pedido.getEstado().name());

            areaResultado.setText(sb.toString());

        } catch (NumberFormatException ex) {
            areaResultado.setText("Ingrese un número válido.");
        }
    }

    public void limpiar() {
        campoNumeroMesa.setText("");
        areaResultado.setText("");
    }

}
