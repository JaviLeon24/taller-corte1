/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.main;

import com.alejandro.modelo.Bebida;
import com.alejandro.modelo.Mesa;
import com.alejandro.modelo.Pedido;
import com.alejandro.modelo.Plato;
import com.alejandro.modelo.Producto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author jalm2
 */
public class GestorRestaurante {

    private static final List<Producto> menu = Arrays.asList(
            new Plato("Hamburguesa", 20000),
            new Plato("Pizza Personal", 8000),
            new Plato("Menú Corriente", 15000),
            new Plato("Sobrebarriga en Salsa", 25000),
            new Plato("Chuleta de Cerdo", 20000),
            new Bebida("Gaseosa", 4000, false),
            new Bebida("Cerveza Nacional", 6000, true),
            new Bebida("Cerveza Importada", 10000, true)
    );

    private static final List<Mesa> mesas = new ArrayList<>();

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
    }

    public static void iniciar() {
        Scanner scanner = new Scanner(System.in);
        OUTER:
        while (true) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Tomar pedido");
            System.out.println("2. Generar factura");
            System.out.println("3. Consultar pedido de una mesa");
            System.out.println("4. Agregar producto a pedido");
            System.out.println("5. Eliminar producto de pedido");
            System.out.println("6. Salir");
            int opcion = scanner.nextInt();
            switch (opcion) {
                case 1 ->
                    tomarPedido(scanner);
                case 2 ->
                    generarFactura(scanner);
                case 3 ->
                    consultarPedido(scanner);
                case 4 ->
                    agregarProductoAPedido(scanner);
                case 5 ->
                    eliminarProductoPedido(scanner);
                case 6 -> {
                    break OUTER;
                }
                default ->
                    System.out.println("Opción inválida.");
            }
        }
    }

    private static void tomarPedido(Scanner scanner) {
        System.out.println("Ingrese la cantidad de personas:");
        int capacidadRequerida = scanner.nextInt();

        List<Mesa> mesasDisponibles = mesas.stream()
                .filter(m -> m.getCapacidad() >= capacidadRequerida && !m.estaOcupada())
                .toList();

        if (mesasDisponibles.isEmpty()) {
            System.out.println("No hay mesas disponibles para esa capacidad.");
            return;
        }

        System.out.println("Mesas disponibles:");
        mesasDisponibles.forEach(m -> System.out.println("Mesa " + m.getNumero() + " (Capacidad: " + m.getCapacidad() + ")"));

        System.out.println("Seleccione una mesa por su número:");
        int numeroMesa = scanner.nextInt();
        Mesa mesa = mesasDisponibles.stream().filter(m -> m.getNumero() == numeroMesa).findFirst().orElse(null);

        if (mesa == null) {
            System.out.println("Selección inválida.");
            return;
        }

        mesa.ocupar();
        Pedido pedido = new Pedido(mesa);
        mesa.asignarPedido(pedido);

        System.out.println("MENÚ:");
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ". " + menu.get(i).descripcion() + " - $" + menu.get(i).getPrecio());
        }

        int opcion;
        do {
            System.out.println("Seleccione un producto (0 para finalizar):");
            opcion = scanner.nextInt();
            if (opcion > 0 && opcion <= menu.size()) {
                pedido.agregarProducto(menu.get(opcion - 1));
            }
        } while (opcion != 0);

        while (true) {
            System.out.println("Confirmación de pedido:");
            for (int i = 0; i < pedido.getProductos().size(); i++) {
                System.out.println((i + 1) + ". " + pedido.getProductos().get(i).descripcion());
            }
            System.out.println("**DIGITE UNA OPCIÓN PARA LA CONFIRMACIÓN**\n 1. Finalizar pedido\n2. Agregar producto\n3. Eliminar producto");
            int eleccion = scanner.nextInt();
            if (eleccion == 1) {
                break;
            }
            if (eleccion == 2) {
                agregarProductoPedidoConfirmacion(scanner, pedido);
            }
            if (eleccion == 3) {
                System.out.println("Ingrese el número del producto a eliminar:");
                int eliminar = scanner.nextInt();
                pedido.eliminarProducto(eliminar - 1);
            }
        }

    }

    private static void agregarProductoAPedido(Scanner scanner) {
        System.out.println("Ingrese el número de mesa para agregar productos:");
        int numeroMesa = scanner.nextInt();

        Mesa mesa = mesas.stream().filter(m -> m.getNumero() == numeroMesa).findFirst().orElse(null);

        if (mesa == null || !mesa.estaOcupada() || mesa.getPedido() == null) {
            System.out.println("No hay pedidos en esta mesa.");
            return;
        }

        Pedido pedido = mesa.getPedido();

        System.out.println("Menú:");
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ". " + menu.get(i).descripcion() + " - $" + menu.get(i).getPrecio());
        }

        int opcion;
        do {
            System.out.println("Seleccione un producto para agregar (0 para finalizar):");
            opcion = scanner.nextInt();
            if (opcion > 0 && opcion <= menu.size()) {
                pedido.agregarProducto(menu.get(opcion - 1));
                System.out.println("Producto agregado correctamente.");
            }
        } while (opcion != 0);

        System.out.println("Productos agregados al pedido.");
    }

    private static void agregarProductoPedidoConfirmacion(Scanner scanner, Pedido pedido) {
        System.out.println("MENÚ:");
        for (int i = 0; i < menu.size(); i++) {
            System.out.println((i + 1) + ". " + menu.get(i).descripcion() + " - $" + menu.get(i).getPrecio());
        }
        System.out.println("Seleccione un producto para agregar:");
        int opcion = scanner.nextInt();
        if (opcion > 0 && opcion <= menu.size()) {
            pedido.agregarProducto(menu.get(opcion - 1));
            System.out.println("Producto agregado correctamente.");
        } else {
            System.out.println("Selección inválida.");
        }
    }

    private static void eliminarProductoPedido(Scanner scanner) {
        System.out.println("Ingrese el número de mesa:");
        int numeroMesa = scanner.nextInt();
        Mesa mesa = mesas.stream().filter(m -> m.getNumero() == numeroMesa).findFirst().orElse(null);

        if (mesa == null || !mesa.estaOcupada() || mesa.getPedido() == null) {
            System.out.println("No hay pedidos en esta mesa.");
            return;
        }

        Pedido pedido = mesa.getPedido();
        List<Producto> productos = pedido.getProductos();
        for (int i = 0; i < productos.size(); i++) {
            System.out.println(i + ". " + productos.get(i).descripcion());
        }

        System.out.println("Seleccione el número del producto a eliminar:");
        int indice = scanner.nextInt();
        pedido.eliminarProducto(indice);
    }

    private static void consultarPedido(Scanner scanner) {
        System.out.println("Ingrese el número de mesa para consultar el pedido:");
        int numeroMesa = scanner.nextInt();

        Mesa mesa = mesas.stream().filter(m -> m.getNumero() == numeroMesa).findFirst().orElse(null);

        if (mesa == null || !mesa.estaOcupada() || mesa.getPedido() == null) {
            System.out.println("No hay pedidos en esta mesa.");
            return;
        }

        Pedido pedido = mesa.getPedido();
        System.out.println("Pedido de la mesa " + mesa.getNumero() + ":");
        pedido.getProductos().forEach(p -> System.out.println(p.descripcion() + " - $" + p.getPrecio()));
        System.out.println("Total provisional: $" + pedido.calcularTotal());
    }

    private static void generarFactura(Scanner scanner) {
        System.out.println("Ingrese el número de mesa para generar la factura:");
        int numeroMesa = scanner.nextInt();
        Mesa mesa = mesas.stream().filter(m -> m.getNumero() == numeroMesa).findFirst().orElse(null);

        if (mesa == null || !mesa.estaOcupada() || mesa.getPedido() == null) {
            System.out.println("No hay pedidos en esta mesa.");
            return;
        }

        Pedido pedido = mesa.getPedido();
        System.out.println("Factura para la mesa " + mesa.getNumero() + ":");
        pedido.getProductos().forEach(p -> System.out.println(p.descripcion() + " - $" + p.getPrecio()));
        System.out.println("Total: $" + pedido.calcularTotal());
        mesa.liberar();
    }
}
