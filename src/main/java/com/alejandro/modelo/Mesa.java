/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.modelo;

/**
 *
 * @author jalm2
 */
public class Mesa {
    private int numero;
    private Pedido pedido;
    private boolean ocupada;

    public Mesa(int numero) {
        this.numero = numero;
        this.pedido = new Pedido();
        this.ocupada = false;
    }

    public int getNumero() {
        return numero;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void ocupar() {
        this.ocupada = true;
    }

    public void liberar() {
        this.ocupada = false;
    }
}
