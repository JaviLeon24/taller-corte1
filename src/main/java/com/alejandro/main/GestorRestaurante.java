/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.main;

import com.alejandro.modelo.Bebida;
import com.alejandro.modelo.Factura;
import com.alejandro.modelo.Mesa;
import com.alejandro.modelo.Producto;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author jalm2
 */
public class GestorRestaurante {

    private static final List<Mesa> mesas = new ArrayList<>();
    private static final List<Producto> menu = Arrays.asList(
            new Producto("Hamburguesa", 20000),
            new Producto("Pizza Personal", 8000),
            new Producto("Menú Corriente", 15000),
            new Producto("Sobrebarriga en Salsa", 25000),
            new Producto("Chuleta de Cerdo", 20000),
            new Bebida("Gaseosa", 4000, false),
            new Bebida("Cerveza Nacional", 6000, true),
            new Bebida("Cerveza Importada", 10000, true)
    );

    private static final Scanner scanner = new Scanner(System.in);

    static {
        for (int i = 1; i <= 15; i++) {
            mesas.add(new Mesa(i));
        }
    }

    public static void iniciar() {
        boolean continuar = true;
        while (continuar) {
            int numMesa = seleccionarMesa();
            if (numMesa == -1) {
                continue;
            }
            Mesa mesa = mesas.get(numMesa - 1);
            tomarPedido(mesa);
            generarFactura(mesa);
            System.out.println("¿Desea realizar otro pedido? (s/n): ");
            continuar = scanner.next().equalsIgnoreCase("s");
        }
    }

    private static int seleccionarMesa() {
        while (true) {
            try {
                System.out.print("Ingrese el número de la mesa (1-15): ");
                int numMesa = scanner.nextInt();
                if (numMesa < 1 || numMesa > 15) {
                    System.out.println("Número inválido.");
                    continue;
                }
                Mesa mesaSeleccionada = mesas.get(numMesa - 1);
                if (!mesaSeleccionada.isOcupada()) {
                    mesaSeleccionada.ocupar();
                    return numMesa;
                }
                System.out.println("Mesa ocupada. Seleccione otra.");
            } catch (InputMismatchException e) {
                System.out.println("Ingrese un número válido.");
                scanner.next();
            }
        }
    }

    private static void tomarPedido(Mesa mesa) {
        int opcion;
        do {
            System.out.println("Menú:");
            for (int i = 0; i < menu.size(); i++) {
                System.out.println((i + 1) + ". " + menu.get(i));
            }
            System.out.println("0. Finalizar pedido");
            System.out.print("Seleccione un producto: ");
            try {
                opcion = scanner.nextInt();
                if (opcion > 0 && opcion <= menu.size()) {
                    mesa.getPedido().agregarProducto(menu.get(opcion - 1));
                    System.out.println("Producto agregado.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Intente de nuevo.");
                scanner.next();
                opcion = -1;
            }
        } while (opcion != 0);
    }

    private static void generarFactura(Mesa mesa) {
        Factura factura = new Factura(mesa.getPedido(), mesa);
        factura.generarFactura();
        System.out.println("Pedido finalizado.");
    }
}
