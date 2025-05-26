/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.alejandro.vista;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author jalm2
 */
public class PanelMesa extends JPanel {

    private final List<JButton> botonesMesas = new ArrayList<>();

    public PanelMesa() {
        setLayout(new GridLayout(4, 5, 10, 10));
        for (int i = 1; i <= 20; i++) {
            JButton btnMesa = new JButton("Mesa " + i);
            btnMesa.setActionCommand(String.valueOf(i));
            botonesMesas.add(btnMesa);
            add(btnMesa);
        }
    }

    public List<JButton> getBotonesMesas() {
        return botonesMesas;
    }
}
