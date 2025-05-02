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
    private int capacidad;
    private boolean ocupada;
    private Pedido pedido;
    private Mesero meseroAsignado;

    public Mesa(int numero, int capacidad) {
        this.numero = numero;
        this.capacidad = capacidad;
        this.ocupada = false;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public boolean estaOcupada() {
        return ocupada;
    }

    public void ocupar() {
        this.ocupada = true;
    }

    public void liberar() {
        this.ocupada = false;
        this.pedido = null;
    }

    public int getNumero() {
        return numero;
    }

    public void asignarPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Mesero getMeseroAsignado() {
        return meseroAsignado;
    }

    public void setMeseroAsignado(Mesero mesero) {
        this.meseroAsignado = mesero;
    }
}