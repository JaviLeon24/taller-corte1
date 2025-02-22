/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.gestion;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jalm2
 */
public class Inventario {
    private Map<String, Integer> stock;

    public Inventario() {
        this.stock = new HashMap<>();
    }

    public void agregarIngrediente(String ingrediente, int cantidad) {
        stock.put(ingrediente, stock.getOrDefault(ingrediente, 0) + cantidad);
    }

    public boolean verificarStock(String ingrediente, int cantidad) {
        return stock.getOrDefault(ingrediente, 0) >= cantidad;
    }
}
