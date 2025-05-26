/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.controlador;

import com.alejandro.modelo.Mesa;
import com.alejandro.modelo.Pedido;
import com.alejandro.vista.PanelMesa;
import com.alejandro.vista.PanelPedidos;
import com.alejandro.vista.VentanaPrincipal;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author jalm2
 */
public class MesaControlador {

    private final PanelMesa vistaMesa;
    private final VentanaPrincipal ventana;
    private final List<Mesa> mesas;
    private final ControladorInicioPedido controladorInicio;
    private final CocinaControlador cocinaControlador;

    public MesaControlador(PanelMesa vistaMesa, VentanaPrincipal ventana,
                           List<Mesa> mesas,
                           ControladorInicioPedido controladorInicio,
                           CocinaControlador cocinaControlador) {
        this.vistaMesa = vistaMesa;
        this.ventana = ventana;
        this.mesas = mesas;
        this.controladorInicio = controladorInicio;
        this.cocinaControlador = cocinaControlador;
        initEventos();
    }

    private void initEventos() {
        for (JButton btn : vistaMesa.getBotonesMesas()) {
            btn.addActionListener(e -> {
                int numeroMesa = Integer.parseInt(e.getActionCommand());

                Mesa mesa = mesas.stream()
                        .filter(m -> m.getNumero() == numeroMesa)
                        .findFirst()
                        .orElse(null);

                if (mesa == null) {
                    JOptionPane.showMessageDialog(vistaMesa, "La mesa no existe.");
                    return;
                }

                if (!mesa.estaOcupada() || mesa.getPedido() == null) {
                    JOptionPane.showMessageDialog(vistaMesa, "La mesa no tiene un pedido activo.");
                    return;
                }

                Pedido pedido = mesa.getPedido();

                PanelPedidos panelPedidos = new PanelPedidos(mesa, controladorInicio.obtenerMenu(), cocinaControlador);
                ventana.cambiarPanel(panelPedidos, "pedidoMesa" + numeroMesa);
            });
        }
    }
}