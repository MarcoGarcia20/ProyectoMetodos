package Ventanas;

import Entidades.Cliente;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;

public class VentanaModificarCliente extends VentanaFormularioCliente {

    private JButton botonModificar;
    private long posicionRegistro = -1; // Para guardar la posición del registro encontrado

    public VentanaModificarCliente() {
        super("Modificar Cliente");
    }

    @Override
    protected void configurar() {
        botonModificar = new JButton("Modificar");
        botonModificar.setBounds(280, 220, 90, 25);
        add(botonModificar);
        botonModificar.addActionListener(this::modificarCliente);
    }
    @Override
    protected void buscarCliente(ActionEvent e) {
        super.buscarCliente(e);
        if (getEncontrado()) {
            // Guardar posición del registro encontrado
            posicionRegistro = indiceDNI.get(campoDni.getText().trim());
            botonModificar.setEnabled(true);  // Habilitar modificación
        } else {
            botonModificar.setEnabled(false);
        }
    }

    private void modificarCliente(ActionEvent e) {
        try {
            int confirm = JOptionPane.showConfirmDialog(this,
             "¿Deseas modificar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            Cliente c = obtenerClienteDesdeCampos();

            archivo = new RandomAccessFile(ruta, "rw");
            archivo.seek(posicionRegistro * Cliente.LONGITUD_REGISTRO); // Mover el puntero al registro encontrado
            c.escribir(archivo);
            
            JOptionPane.showMessageDialog(this, "✔ Registro modificado.");
            botonModificar.setEnabled(false);
            archivo.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
        }
    }    
}