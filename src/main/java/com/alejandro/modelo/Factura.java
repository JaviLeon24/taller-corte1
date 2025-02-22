/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.modelo;

/**
 *
 * @author jalm2
 */
public class Factura{

    private Pedido pedido;
    private Mesa mesa;

    public Factura(Pedido pedido, Mesa mesa) {
        this.pedido = pedido;
        this.mesa = mesa;
    }

    public void generarFactura() {
        System.out.println("--- Factura ---");
        pedido.mostrarPedido();
        System.out.println("Gracias por su compra!");
        mesa.liberar();
    }
}
