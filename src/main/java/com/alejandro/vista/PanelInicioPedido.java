/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.vista;

import com.alejandro.controlador.CocinaControlador;
import com.alejandro.controlador.ControladorInicioPedido;
import com.alejandro.modelo.Mesa;
import com.alejandro.modelo.Mesero;
import com.alejandro.modelo.Pedido;
import com.alejandro.modelo.Producto;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author jalm2
 */
public class PanelInicioPedido extends JPanel {

    private final JTextField campoPersonas;
    private final JComboBox<String> comboMeseros;
    private final JComboBox<String> comboMesas;
    private final JButton btnIniciarPedido;
    private final JButton btnAgregarProducto;
    private final JButton btnEliminarProducto;
    private final JButton btnConfirmarPedido;
    private final JList<String> listaMenu;
    private final JList<String> listaPedido;
    private final DefaultListModel<String> modeloMenu;
    private final DefaultListModel<String> modeloPedido;

    private final ControladorInicioPedido controlador;
    private final CocinaControlador cocinaControlador;
    private Pedido pedidoActual;
    private List<Producto> menu;
    private Map<Integer, Mesero> meseros;

    public PanelInicioPedido(ControladorInicioPedido controlador, CocinaControlador cocinaControlador) {
        this.controlador = controlador;
        this.cocinaControlador = cocinaControlador;

        setLayout(new BorderLayout());

        // Panel superior
        JPanel panelTop = new JPanel(new FlowLayout());
        campoPersonas = new JTextField(5);
        comboMeseros = new JComboBox<>();
        comboMesas = new JComboBox<>();
        comboMesas.setPreferredSize(new Dimension(150, 25));
        btnIniciarPedido = new JButton("Iniciar Pedido");

        panelTop.add(new JLabel("Personas:"));
        panelTop.add(campoPersonas);
        panelTop.add(new JLabel("Mesero:"));
        panelTop.add(comboMeseros);
        panelTop.add(new JLabel("Mesa:"));
        panelTop.add(comboMesas);
        panelTop.add(btnIniciarPedido);

        add(panelTop, BorderLayout.NORTH);

        // Listas
        modeloMenu = new DefaultListModel<>();
        modeloPedido = new DefaultListModel<>();
        listaMenu = new JList<>(modeloMenu);
        listaPedido = new JList<>(modeloPedido);

        JPanel panelListas = new JPanel(new GridLayout(1, 2));
        panelListas.add(new JScrollPane(listaMenu));
        panelListas.add(new JScrollPane(listaPedido));
        add(panelListas, BorderLayout.CENTER);

        // Botones
        btnAgregarProducto = new JButton("Agregar Producto");
        btnEliminarProducto = new JButton("Eliminar Producto");
        btnConfirmarPedido = new JButton("Confirmar Pedido");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregarProducto);
        panelBotones.add(btnEliminarProducto);
        panelBotones.add(btnConfirmarPedido);
        add(panelBotones, BorderLayout.SOUTH);

        setBotonesActivos(false);

        // Eventos
        btnIniciarPedido.addActionListener(e -> crearPedido());
        btnAgregarProducto.addActionListener(e -> agregarProducto());
        btnEliminarProducto.addActionListener(e -> eliminarProducto());
        btnConfirmarPedido.addActionListener(e -> confirmarPedido());

        // Detectar cuando el campo pierde el foco para actualizar mesas disponibles
        campoPersonas.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                actualizarMesasDisponibles();
            }
        });

        // Cargar datos
        this.menu = controlador.obtenerMenu();
        this.meseros = controlador.obtenerMeseros();
        actualizarMenu();
        actualizarMeseros();

        JOptionPane.showMessageDialog(this,
                "Bienvenido. Ingrese el número de personas, seleccione un mesero y una mesa, luego haga clic en 'Iniciar Pedido'.");
    }

    private void setBotonesActivos(boolean estado) {
        btnAgregarProducto.setEnabled(estado);
        btnEliminarProducto.setEnabled(estado);
        btnConfirmarPedido.setEnabled(estado);
    }

    private void actualizarMenu() {
        modeloMenu.clear();
        for (Producto p : menu) {
            modeloMenu.addElement(p.descripcion() + " - $" + p.getPrecio());
        }
    }

    private void actualizarMeseros() {
        comboMeseros.removeAllItems();
        for (Map.Entry<Integer, Mesero> entry : meseros.entrySet()) {
            comboMeseros.addItem(entry.getKey() + " - " + entry.getValue().getNombre());
        }
    }

    private void actualizarMesasDisponibles() {
        comboMesas.removeAllItems();
        try {
            int capacidad = Integer.parseInt(campoPersonas.getText().trim());
            List<Mesa> disponibles = controlador.obtenerMesasDisponibles(capacidad);
            if (disponibles.isEmpty()) {
                comboMesas.addItem("No hay mesas disponibles");
            } else {
                for (Mesa m : disponibles) {
                    comboMesas.addItem(m.getNumero() + " - Capacidad: " + m.getCapacidad());
                }
            }
        } catch (NumberFormatException e) {
            comboMesas.removeAllItems();
            comboMesas.addItem("Ingrese personas válidas");
        }
    }

    private void crearPedido() {
        try {
            int cantidad = Integer.parseInt(campoPersonas.getText().trim());

            String seleccionMesa = (String) comboMesas.getSelectedItem();
            if (seleccionMesa == null || !seleccionMesa.contains(" - ")) {
                JOptionPane.showMessageDialog(this, "Seleccione una mesa válida.");
                return;
            }

            int numeroMesa = Integer.parseInt(seleccionMesa.split(" - ")[0].trim());
            Mesa mesa = controlador.obtenerMesaPorNumero(numeroMesa);

            if (mesa == null || mesa.estaOcupada()) {
                JOptionPane.showMessageDialog(this, "La mesa seleccionada no está disponible.");
                return;
            }

            pedidoActual = new Pedido(mesa);
            mesa.asignarPedido(pedidoActual);

            // Obtener mesero
            String seleccionMesero = (String) comboMeseros.getSelectedItem();
            Mesero mesero = null;
            if (seleccionMesero != null && seleccionMesero.contains(" - ")) {
                int idMesero = Integer.parseInt(seleccionMesero.split(" - ")[0].trim());
                mesero = meseros.get(idMesero);
            }

            if (mesero == null) {
                mesero = controlador.asignarMeseroAleatorio();
                JOptionPane.showMessageDialog(this, "Mesero asignado automáticamente: " + mesero.getNombre());
            }

            mesero.asignarMesa(mesa);
            setBotonesActivos(true);

            JOptionPane.showMessageDialog(this,
                    "✅ Pedido creado en la mesa " + mesa.getNumero() + ". Puede comenzar a agregar productos.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido de personas.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear pedido: " + ex.getMessage());
            setBotonesActivos(false);
        }
    }

    private void agregarProducto() {
        if (pedidoActual == null) {
            JOptionPane.showMessageDialog(this, "Debe iniciar un pedido primero.");
            return;
        }

        int index = listaMenu.getSelectedIndex();
        if (index != -1) {
            controlador.agregarProducto(pedidoActual, menu.get(index));
            actualizarPedido();
        }
    }

    private void eliminarProducto() {
        int index = listaPedido.getSelectedIndex();
        if (index != -1) {
            controlador.eliminarProducto(pedidoActual, index);
            actualizarPedido();
        }
    }

    private void confirmarPedido() {
        if (pedidoActual == null || pedidoActual.getProductos().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pedido vacío o no creado.");
            return;
        }

        cocinaControlador.recibirPedido(pedidoActual);
        JOptionPane.showMessageDialog(this, "Pedido confirmado y enviado a cocina.");

        setBotonesActivos(false);
    }

    private void actualizarPedido() {
        modeloPedido.clear();
        for (Producto p : pedidoActual.getProductos()) {
            modeloPedido.addElement(p.descripcion() + " - $" + p.getPrecio());
        }
    }

    public void limpiar() {
        campoPersonas.setText("");
        comboMesas.setSelectedIndex(-1);
        comboMeseros.setSelectedIndex(-1);
        modeloPedido.clear();
        setBotonesActivos(false);
    }
}
