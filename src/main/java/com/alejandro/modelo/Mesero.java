/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jalm2
 */
public class Mesero implements Serializable {

    private String nombre;
    private List<Mesa> mesasAsignadas;

    public Mesero(String nombre) {
        this.nombre = nombre;
        this.mesasAsignadas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void asignarMesa(Mesa mesa) {
        mesasAsignadas.add(mesa);
        mesa.setMeseroAsignado(this);
    }

    public List<Mesa> getMesasAsignadas() {
        return mesasAsignadas;
    }
}
