/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.util;

import com.alejandro.modelo.Mesa;
import com.alejandro.modelo.Mesero;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jalm2
 */
public class Serializador {

    private static final String ARCHIVO_ESTADO = "data/estado_restaurante.ser";

    public static void guardarEstado(List<Mesa> mesas, Map<Integer, Mesero> meseros) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(ARCHIVO_ESTADO))) {
            out.writeObject(mesas);
            out.writeObject(meseros);
        } catch (IOException e) {
            System.err.println("Error al guardar el estado del restaurante: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static Object[] cargarEstado() {
        File archivo = new File(ARCHIVO_ESTADO);
        if (!archivo.exists()) {
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            List<Mesa> mesas = (List<Mesa>) in.readObject();
            Map<Integer, Mesero> meseros = (Map<Integer, Mesero>) in.readObject();
            return new Object[]{mesas, meseros};
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar el estado del restaurante: " + e.getMessage());
            return null;
        }
    }
}