/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.controlador;

import com.alejandro.modelo.Mesa;
import com.alejandro.modelo.Mesero;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jalm2
 */
public class MeseroControlador {

    private final Map<Integer, Mesero> meseros;

    public MeseroControlador(Map<Integer, Mesero> meseros) {
        this.meseros = meseros;
    }

    public Map<Integer, Mesero> obtenerMeseros() {
        return meseros;
    }

    public List<Mesa> obtenerMesasPorMesero(int id) {
        Mesero mesero = meseros.get(id);
        return mesero != null ? mesero.getMesasAsignadas() : List.of();
    }
}