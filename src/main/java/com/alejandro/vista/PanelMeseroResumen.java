/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.vista;

import com.alejandro.controlador.MeseroControlador;
import com.alejandro.modelo.Mesa;
import com.alejandro.modelo.Mesero;
import com.alejandro.modelo.Pedido;
import java.awt.BorderLayout;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author jalm2
 */
public class PanelMeseroResumen extends JPanel {
    private final MeseroControlador controlador;
    private Map<Integer, Mesero> meseros;
    private final JComboBox<String> comboMeseros;
    private final DefaultListModel<String> modelo;
    private final JList<String> listaMesas;

    public PanelMeseroResumen(MeseroControlador controlador) {
        this.controlador = controlador;
        this.meseros = controlador.obtenerMeseros();

        setLayout(new BorderLayout());

        comboMeseros = new JComboBox<>();
        listaMesas = new JList<>(modelo = new DefaultListModel<>());

        add(comboMeseros, BorderLayout.NORTH);
        add(new JScrollPane(listaMesas), BorderLayout.CENTER);

        comboMeseros.addActionListener(e -> actualizarLista());
    }

    public void refrescarVista() {
        comboMeseros.removeAllItems();
        this.meseros = controlador.obtenerMeseros();
        for (Map.Entry<Integer, Mesero> entry : meseros.entrySet()) {
            comboMeseros.addItem(entry.getKey() + " - " + entry.getValue().getNombre());
        }
        actualizarLista();
    }

    private void actualizarLista() {
        modelo.clear();
        int index = comboMeseros.getSelectedIndex();
        if (index != -1) {
            int meseroId = index + 1;
            List<Mesa> mesas = controlador.obtenerMesasPorMesero(meseroId);
            if (mesas.isEmpty()) {
                modelo.addElement("No tiene mesas asignadas.");
            } else {
                for (Mesa mesa : mesas) {
                    Pedido pedido = mesa.getPedido();
                    String estado = (pedido != null) ? pedido.getEstado().name() : "Sin pedido";
                    modelo.addElement("Mesa " + mesa.getNumero() + " - Estado: " + estado);
                }
            }
        }
    }
}
