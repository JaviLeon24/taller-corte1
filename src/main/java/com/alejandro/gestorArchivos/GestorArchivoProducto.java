/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.gestorArchivos;

import com.alejandro.modelo.Bebida;
import com.alejandro.modelo.Plato;
import com.alejandro.modelo.Producto;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jalm2
 */
public class GestorArchivoProducto {

    /**
     * Lee los productos desde un archivo CSV y hace uso de los constructores de PLATO y BEBIDA según el caso encontrado en dicha lectura
     * @param ruta
     * @return 
     * @throws java.io.IOException
    */
    public static List<Producto> leerProductosDesdeArchivo(String ruta) throws IOException {
        List<Producto> productos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea = br.readLine();
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length < 3) {
                    continue;
                }
                String tipo = partes[0];
                String nombre = partes[1];
                double precio = Double.parseDouble(partes[2]);
                if ("Plato".equalsIgnoreCase(tipo)) {
                    productos.add(new Plato(nombre, precio));
                } else if ("Bebida".equalsIgnoreCase(tipo)) {
                    boolean alcoholica = Boolean.parseBoolean(partes[3]);
                    productos.add(new Bebida(nombre, precio, alcoholica));
                }
            }
        }
        return productos;
    }

    /**
     * Guarda los pedidos por mesa cuando se genera el recibo correspondiente en un archivo CSV, dicho CSV contiene la información de cada producto.
     * @param rutaCarpeta
     * @param nombreArchivo
     * @param productos
     * @throws IOException 
     */
    public static void guardarPedido(String rutaCarpeta, String nombreArchivo, List<Producto> productos) throws IOException {
        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists()) {
            if (!carpeta.mkdirs()) {
                throw new IOException("No se pudo crear la carpeta: " + rutaCarpeta);
            }
        }

        File archivo = new File(carpeta, nombreArchivo);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write("TIPO;NOMBRE;PRECIO;ALCOHOLICA");
            bw.newLine();

            for (Producto producto : productos) {
                if (producto instanceof Plato p) {
                    bw.write("Plato;" + p.getNombre() + ";" + p.getPrecio() + ";");
                } else if (producto instanceof Bebida b) {
                    bw.write("Bebida;" + b.getNombre() + ";" + b.getPrecio() + ";" + b.esAlcoholica());
                }
                bw.newLine();
            }
        }
    }

}
