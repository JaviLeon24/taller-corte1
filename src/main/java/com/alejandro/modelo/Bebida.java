/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.modelo;

/**
 *
 * @author jalm2
 */
public class Bebida extends Producto {
    private boolean alcoholica;

    public Bebida(String nombre, double precio, boolean alcoholica) {
        super(nombre, precio);
        this.alcoholica = alcoholica;
    }

    public boolean esAlcoholica() {
        return alcoholica;
    }
}
