/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.main;

import com.alejandro.gestorArchivos.GestorArchivoProducto;
import com.alejandro.logica.implementacion.CocinaImpl;
import com.alejandro.manejoExcepciones.MesaNoDisponibleException;
import com.alejandro.logica.CocinaLogica;
import com.alejandro.logica.PedidoLogica;
import com.alejandro.logica.implementacion.PedidoImpl;
import com.alejandro.modelo.EstadoPedido;
import com.alejandro.modelo.Mesa;
import com.alejandro.modelo.Mesero;
import com.alejandro.modelo.Pedido;
import com.alejandro.modelo.Producto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author jalm2
 */
public class GestorRestaurante {

    private static List<Producto> menu = new ArrayList<>();
    private static final List<Mesa> mesas = new ArrayList<>();
    private static final PedidoLogica pedidoLogica = new PedidoImpl();
    private static final Map<Integer, Mesero> meseros = new HashMap<>();

    static {
        for (int i = 1; i <= 10; i++) {
            mesas.add(new Mesa(i, 4));
        }
        for (int i = 11; i <= 15; i++) {
            mesas.add(new Mesa(i, 6));
        }
        for (int i = 16; i <= 20; i++) {
            mesas.add(new Mesa(i, 8));
        }

        meseros.put(1, new Mesero("Laura"));
        meseros.put(2, new Mesero("Carlos"));
        meseros.put(3, new Mesero("Sofía"));
    }

    public static void iniciar() {
        Scanner scanner = new Scanner(System.in);
        try {
            menu = GestorArchivoProducto.leerProductosDesdeArchivo("data/menu.csv");
        } catch (IOException e) {
            System.err.println("Error al cargar el menú: " + e.getMessage());
            return;
        }

        while (true) {
            try {
                System.out.println("\nSeleccione una opción:");
                System.out.println("1. Tomar pedido");
                System.out.println("2. Generar Recibo");
                System.out.println("3. Consultar pedido de una mesa");
                System.out.println("4. Agregar producto a pedido");
                System.out.println("5. Eliminar producto de pedido");
                System.out.println("6. Consultar mesas por mesero");
                System.out.println("7. Ver pedidos listos para entrega");
                System.out.println("8. Marcar pedido como listo (solo cocina)");
                System.out.println("9. Salir\n");
                int opcion = scanner.nextInt();
                switch (opcion) {
                    case 1 ->
                        tomarPedido(scanner);
                    case 2 ->
                        generarRecibo(scanner);
                    case 3 ->
                        consultarPedido(scanner);
                    case 4 ->
                        agregarProductoAPedido(scanner);
                    case 5 ->
                        eliminarProductoPedido(scanner);
                    case 6 ->
                        consultarMesasPorMeseros();
                    case 7 ->
                        mostrarPedidosListos();
                    case 8 ->
                        marcarPedidoComoListo(scanner);
                    case 9 -> {
                        return;
                    }
                    default ->
                        System.out.println("Opción inválida.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Debe ingresar un número válido.");
                scanner.nextLine();
            }

        }

    }

    /**
     * Método para tomar el pedido en cada una de las mesas, en caso de no tener disponibilidad de mesas se lanzará una excepción
     * Se mostrará la capacidad de las mesas para ubicar correctamente a las personas
     * Es necesario confirmar el ID del mesero que tomará la orden, en caso de no ser seleccionado se generará la orden para un mesero aleatorio
     * La toma de pedido cuenta con una confirmación, en caso de que el cliente quiera agregar o eliminar un producto antes de comenzar su preparación
     * @param scanner 
     */
    private static void tomarPedido(Scanner scanner) {
        try {
            System.out.println("\nIngrese la cantidad de personas:");
            int capacidadRequerida = scanner.nextInt();
            List<Mesa> mesasDisponibles = mesas.stream().filter(m -> m.getCapacidad() >= capacidadRequerida && !m.estaOcupada()).toList();

            if (mesasDisponibles.isEmpty()) {
                throw new MesaNoDisponibleException("No hay mesas disponibles.");
            }

            mesasDisponibles.forEach(m -> System.out.println("\nMesa " + m.getNumero() + " (Capacidad: " + m.getCapacidad() + ")"));

            System.out.println("Seleccione número de mesa:");
            int numeroMesa = scanner.nextInt();
            Mesa mesa = mesasDisponibles.stream().filter(m -> m.getNumero() == numeroMesa).findFirst().orElse(null);

            if (mesa == null) {
                throw new MesaNoDisponibleException("Mesa no válida o no disponible.");
            }

            mesa.ocupar();
            Pedido pedido = new Pedido(mesa);
            mesa.asignarPedido(pedido);

            System.out.println("\nSeleccione el ID del mesero que tomará el pedido:");
            for (Map.Entry<Integer, Mesero> entry : meseros.entrySet()) {
                System.out.println("ID: " + entry.getKey() + " - " + entry.getValue().getNombre());
            }
            
            int meseroId = scanner.nextInt();
            Mesero mesero = meseros.get(meseroId);
            if (mesero == null) {
                System.out.println("Mesero no válido. Se asignará uno aleatoriamente.");
                mesero = meseros.get(new ArrayList<>(meseros.keySet()).get(new Random().nextInt(meseros.size())));
            }
            mesero.asignarMesa(mesa);
            System.out.println("\nMesero asignado: " + mesero.getNombre());

            mostrarMenu();

            int opcion;
            do {
                System.out.println("Seleccione producto (0 para finalizar):");
                opcion = scanner.nextInt();
                if (opcion > 0 && opcion <= menu.size()) {
                    pedidoLogica.agregarProducto(pedido ,menu.get(opcion - 1));
                }
            } while (opcion != 0);

            OUTER:
            while (true) {
                System.out.println("\nConfirmación de pedido:");
                for (int i = 0; i < pedido.getProductos().size(); i++) {
                    System.out.println((i + 1) + ". " + pedido.getProductos().get(i).descripcion());
                }
                System.out.println("**DIGITE UNA OPCIÓN PARA LA CONFIRMACIÓN**\n1. Finalizar pedido\n2. Agregar producto\n3. Eliminar producto");
                int eleccion = scanner.nextInt();
                switch (eleccion) {
                    case 1 -> {
                        CocinaLogica cocina = new CocinaImpl();
                        cocina.recibirPedido(pedido);
                        System.out.println("Estado del pedido: " + pedido.getEstado());
                        break OUTER;
                    }
                    case 2 ->
                        agregarProductoPedidoConfirmacion(scanner, pedido);
                    case 3 -> {
                        System.out.println("Ingrese el número del producto a eliminar:");
                        int eliminar = scanner.nextInt();
                        pedidoLogica.eliminarProducto(pedido, eliminar - 1);
                    }
                    default -> {
                    }
                }
            }
        } catch (MesaNoDisponibleException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.err.println("Debe ingresar un número válido.");
            scanner.nextLine();
        }
    }

    /**
     * Muestra el menú disponible, el cual se extrae de la lectura de archivo implementada para el menú del restaurante
     */
    private static void mostrarMenu() {
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ". " + menu.get(i).descripcion() + " - $" + menu.get(i).getPrecio());
        }
    }

    /**
     * Método para agregar productos a un pedido en curso, el pedido debe estar en estado EN_PREPARACION para que pueda ser editado, de lo contrario lanzará un mensaje para indicar que no se puede modificar
     * 
     * @param scanner 
     */
    private static void agregarProductoAPedido(Scanner scanner) {
        try {
            System.out.println("Ingrese el número de mesa para agregar productos:");
            int numeroMesa = scanner.nextInt();
            
            Mesa mesa = buscarMesaPorNumero(numeroMesa);

            validarMesaConPedido(mesa);

            Pedido pedido = mesa.getPedido();
            if (!pedidoLogica.puedeModificar(pedido)) {
                System.out.println("El pedido no se puede modificar en este estado: " + pedido.getEstado());
                return;
            }

            mostrarMenu();
            System.out.println("Seleccione producto a agregar:");
            int opcion = scanner.nextInt();
            if (opcion > 0 && opcion <= menu.size()) {
                pedidoLogica.agregarProducto(pedido, menu.get(opcion - 1));
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    /**
     * Método que permite confirmar la adición de un producto en un pedido
     * @param scanner
     * @param pedido 
     */
    private static void agregarProductoPedidoConfirmacion(Scanner scanner, Pedido pedido) {
        System.out.println("MENÚ:");
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ". " + menu.get(i).descripcion() + " - $" + menu.get(i).getPrecio());
        }
        System.out.println("Seleccione un producto para agregar:");
        try {
            int opcion = scanner.nextInt();
            if (opcion > 0 && opcion <= menu.size()) {
                pedidoLogica.agregarProducto(pedido, menu.get(opcion - 1));
                System.out.println("Producto agregado correctamente.");
            } else {
                System.out.println("Selección inválida.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Debe ingresar un número válido.");
            scanner.nextLine();
        }

    }

    /**
     * Método que permite eliminar productos de un pedido, dicho pedido debe estar en estado EN_PREPARACION, de lo contrario lanzará un mensaje para indicar que no se puede modificar el pedido
     * @param scanner 
     */
    private static void eliminarProductoPedido(Scanner scanner) {
        try {
            System.out.println("Ingrese el número de mesa:");
            int numeroMesa = scanner.nextInt();
            
            Mesa mesa = buscarMesaPorNumero(numeroMesa);

            validarMesaConPedido(mesa);

            Pedido pedido = mesa.getPedido();
            if (!pedidoLogica.puedeModificar(pedido)) {
                System.out.println("El pedido no se puede modificar en este estado: " + pedido.getEstado());
                return;
            }

            List<Producto> productos = pedido.getProductos();
            for (int i = 0; i < productos.size(); i++) {
                System.out.println((i + 1) + ". " + productos.get(i).descripcion());
            }
            System.out.println("Seleccione el número del producto a eliminar:");
            int indice = scanner.nextInt();
            pedidoLogica.eliminarProducto(pedido, indice - 1);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Método que permite consulta un pedido
     * @param scanner 
     */
    private static void consultarPedido(Scanner scanner) {
        try {
            System.out.println("Ingrese el número de mesa para consultar el pedido:");
            int numeroMesa = scanner.nextInt();

            Mesa mesa = buscarMesaPorNumero(numeroMesa);

            validarMesaConPedido(mesa);

            Pedido pedido = mesa.getPedido();
            System.out.println("\nPedido de la mesa " + mesa.getNumero() + ":");
            pedido.getProductos().forEach(p -> System.out.println(p.descripcion() + " - $" + p.getPrecio()));
            System.out.println("Total provisional: $" + pedidoLogica.calcularTotal(pedido));
            System.out.println("Estado: " + pedido.getEstado() + "\n");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    /**
     * Método que genera un recibo de pago
     * El pedido debe estar en estado LISTO para que se pueda generar un recibo
     * Luego de la validación anterior, se intentará crear un nuevo archivo para registrar el pedido (persistencia)
     * @param scanner 
     */
    private static void generarRecibo(Scanner scanner) {
        try {
            System.out.println("Ingrese el número de mesa para generar el recibo:");
            int numeroMesa = scanner.nextInt();
            
            Mesa mesa = buscarMesaPorNumero(numeroMesa);

            validarMesaConPedido(mesa);

            Pedido pedido = mesa.getPedido();

            if (pedido.getEstado() != EstadoPedido.LISTO) {
                System.out.println("El pedido aún no está listo. No se puede generar el recibo");
                return;
            }

            System.out.println("\nRecibo para la mesa " + mesa.getNumero() + ":");
            pedido.getProductos().forEach(p -> System.out.println(p.descripcion() + " - $" + p.getPrecio()));
            System.out.println("Total: $" + pedidoLogica.calcularTotal(pedido));
            try {
                String carpeta = "data/pedidos";
                String nombreArchivo = "pedido_mesa_" + mesa.getNumero() + "_" + System.currentTimeMillis() + ".csv";
                GestorArchivoProducto.guardarPedido(carpeta, nombreArchivo, pedido.getProductos());
                System.out.println("Pedido guardado en archivo.\n");
            } catch (IOException e) {
                System.err.println("Error al guardar el pedido: " + e.getMessage());
            }

            mesa.liberar();
            Mesero mesero = meseros.values().stream()
                    .filter(m -> m.getMesasAsignadas().contains(mesa))
                    .findFirst()
                    .orElse(null);

            if (mesero != null) {
                mesero.getMesasAsignadas().remove(mesa);
            }
            
            CocinaLogica cocina = new CocinaImpl();
            cocina.removerPedido(pedido);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    /**
     * Método que permite consultar los pedidos por los meseros disponibles
     */
    private static void consultarMesasPorMeseros() {
        for (Map.Entry<Integer, Mesero> entry : meseros.entrySet()) {
            int id = entry.getKey();
            Mesero mesero = entry.getValue();
            System.out.println("\nMesero ID " + id + ": " + mesero.getNombre());
            List<Mesa> asignadas = mesero.getMesasAsignadas();
            if (asignadas.isEmpty()) {
                System.out.println("  - No tiene mesas asignadas.");
            } else {
                for (Mesa mesa : asignadas) {
                    System.out.println("  - Mesa " + mesa.getNumero() + ", estado del pedido " + mesa.getPedido().getEstado());
                }
            }
        }
    }

    /**
     * Método que muestra los pedidos listos para entregar a las respectivas mesas
     */
    private static void mostrarPedidosListos() {
        CocinaLogica cocina = new CocinaImpl();
        List<Pedido> listos = cocina.getPedidosListos();
        if (listos.isEmpty()) {
            System.out.println("\nNo hay pedidos listos.");
        } else {
            System.out.println("\nPedidos listos para entrega:");
            for (Pedido p : listos) {
                System.out.println("Mesa " + p.getMesa().getNumero() + ": " + p.getProductos().size() + " productos");
            }
        }
    }

    /**
     * Método que permite a la cocina marcar un pedido en estado LISTO
     * @param scanner 
     */
    private static void marcarPedidoComoListo(Scanner scanner) {
        try {
            System.out.println("Ingrese número de mesa cuyo pedido está listo:");
            int mesaId = scanner.nextInt();
            CocinaLogica cocina = new CocinaImpl();
            cocina.marcarPedidoComoListo(mesaId);
        } catch (InputMismatchException e) {
            System.err.println("Debe ingresar un número válido.");
            scanner.nextLine();
        }
    }

    /**
     * Método general para validar cuando una mesa no existe o no tiene un pedido asociado
     * @param mesa
     * @throws MesaNoDisponibleException 
     */
    private static void validarMesaConPedido(Mesa mesa) throws MesaNoDisponibleException {
        if (mesa == null) {
            throw new MesaNoDisponibleException("La mesa no existe.");
        } else if (mesa.getPedido() == null) {
            throw new MesaNoDisponibleException("La mesa no tiene un pedido.");
        }
    }
    
    /**
     * Método que permite consultar una mesa por su número o identificador
     * @param numeroMesa
     * @return 
     */
    private static Mesa buscarMesaPorNumero(int numeroMesa) { 
        Mesa mesa = mesas.stream().filter(m -> m.getNumero() == numeroMesa).findFirst().orElse(null);
        return mesa;
    }
}
