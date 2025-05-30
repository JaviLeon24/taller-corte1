/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.vista;

import com.alejandro.controlador.CocinaControlador;
import com.alejandro.controlador.ControladorInicioPedido;
import com.alejandro.controlador.MeseroControlador;
import com.alejandro.controlador.ReciboControlador;
import com.alejandro.logica.CocinaLogica;
import com.alejandro.logica.PedidoLogica;
import com.alejandro.logica.implementacion.CocinaImpl;
import com.alejandro.logica.implementacion.PedidoImpl;
import com.alejandro.modelo.Mesa;
import com.alejandro.modelo.Mesero;
import com.alejandro.util.Serializador;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author jalm2
 */
public class VentanaPrincipal extends JFrame {

    private final JPanel panelContenido;
    private final CardLayout layout;
    private final PanelMeseroResumen panelMeseros;
    private final PanelCocina panelCocina;
    private final List<Mesa> mesas;
    private final Map<Integer, Mesero> meseros;
    private final PanelConsultaMesa panelConsultaMesa;
    private final PanelPedidosListos panelPedidosListos;
    private final PanelRecibo panelRecibo;
    private final PanelInicioPedido panelInicio;

    public VentanaPrincipal() {
        setTitle("Gestión del Restaurante");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        // Restaurar estado o iniciar por defecto
        Object[] estado = Serializador.cargarEstado();
        if (estado != null) {
            mesas = (List<Mesa>) estado[0];
            meseros = (Map<Integer, Mesero>) estado[1];
        } else {
            mesas = crearMesasIniciales();
            meseros = crearMeserosDesdeArchivo();
        }

        // Instancias compartidas
        PedidoLogica pedidoLogica = new PedidoImpl(mesas);
        CocinaLogica cocinaLogica = new CocinaImpl(pedidoLogica);
        CocinaControlador cocinaControlador = new CocinaControlador(cocinaLogica);

        // Controladores
        ControladorInicioPedido controladorInicio = new ControladorInicioPedido(
                mesas, meseros, cocinaControlador, pedidoLogica);
        MeseroControlador meseroControlador = new MeseroControlador(meseros);
        ReciboControlador reciboControlador = new ReciboControlador(mesas, meseros, cocinaControlador);

        // Paneles
        panelInicio = new PanelInicioPedido(controladorInicio, cocinaControlador);
        panelCocina = new PanelCocina(cocinaControlador);
        panelMeseros = new PanelMeseroResumen(meseroControlador);
        panelRecibo = new PanelRecibo(reciboControlador);
        panelPedidosListos = new PanelPedidosListos(cocinaControlador);
        panelConsultaMesa = new PanelConsultaMesa(mesas);

        layout = new CardLayout();
        panelContenido = new JPanel(layout);

        panelContenido.add(panelInicio, "inicio");
        panelContenido.add(panelCocina, "cocina");
        panelContenido.add(panelMeseros, "meseros");
        panelContenido.add(panelRecibo, "recibo");
        panelContenido.add(panelPedidosListos, "listos");
        panelContenido.add(panelConsultaMesa, "consulta");

        setJMenuBar(crearMenu());
        add(panelContenido);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Serializador.guardarEstado(mesas, meseros);
                System.exit(0);
            }
        });
    }

    private JMenuBar crearMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Navegación");

        JMenuItem itemInicio = new JMenuItem("Inicio Pedido");
        itemInicio.addActionListener(e -> {
            panelInicio.limpiar();
            mostrarPanel("inicio");
        });

        JMenuItem itemCocina = new JMenuItem("Cocina");
        itemCocina.addActionListener((ActionEvent e) -> {
            panelCocina.cargarPedidos();
            mostrarPanel("cocina");
        });

        JMenuItem itemMeseros = new JMenuItem("Resumen por Meseros");
        itemMeseros.addActionListener(e -> {
            panelMeseros.refrescarVista();
            layout.show(panelContenido, "meseros");
        });
        menu.add(itemMeseros);

        JMenuItem itemRecibo = new JMenuItem("Recibo");
        itemRecibo.addActionListener(e -> {
            panelRecibo.refrescarPedidos();
            mostrarPanel("recibo");
        });

        JMenuItem itemListos = new JMenuItem("Pedidos Listos");
        itemListos.addActionListener((ActionEvent e) -> {
            panelPedidosListos.cargarPedidosListos();
            mostrarPanel("listos");
        });

        JMenuItem itemConsulta = new JMenuItem("Consulta Mesa");
        itemConsulta.addActionListener(e -> {
            panelConsultaMesa.limpiar();
            mostrarPanel("consulta");
        });

        menu.add(itemInicio);
        menu.add(itemCocina);
        menu.add(itemMeseros);
        menu.add(itemRecibo);
        menu.add(itemListos);
        menu.add(itemConsulta);

        menuBar.add(menu);
        return menuBar;
    }

    private void mostrarPanel(String nombre) {
        layout.show(panelContenido, nombre);
    }

    public void cambiarPanel(JPanel panel, String nombre) {
        panelContenido.add(panel, nombre);
        layout.show(panelContenido, nombre);
    }

    private List<Mesa> crearMesasIniciales() {
        List<Mesa> lista = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            lista.add(new Mesa(i, 4));
        }
        for (int i = 11; i <= 15; i++) {
            lista.add(new Mesa(i, 6));
        }
        for (int i = 16; i <= 20; i++) {
            lista.add(new Mesa(i, 8));
        }
        return lista;
    }

    private Map<Integer, Mesero> crearMeserosDesdeArchivo() {
        Map<Integer, Mesero> mapa = new HashMap<>();
        List<String> nombres = com.alejandro.gestorArchivos.GestorArchivoProducto.cargarMeseros();

        for (int i = 0; i < nombres.size(); i++) {
            mapa.put(i + 1, new Mesero(nombres.get(i)));
        }
        return mapa;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}
