/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.modelo;

/**
 *
 * @author jalm2
 */
public class Plato extends Producto {

    public Plato(String nombre, double precio) {
        super(nombre, precio);
    }

    @Override
    public String descripcion() {
        return getNombre();
    }
}
