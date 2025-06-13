package Vistas;

import Entidades.Cliente;
import javax.swing.*;
import java.awt.event.*;
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
        botonModificar.setEnabled(false); // Deshabilitar inicialmente
    }
    @Override
    protected void buscarCliente(ActionEvent e){
        super.buscarCliente(e);
        botonModificar.setEnabled(true); // Habilitar el botón de modificar si se encuentra el cliente
    }

    private void modificarCliente(ActionEvent e) {
        try {
            int confirm = JOptionPane.showConfirmDialog(this,
             "¿Deseas modificar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION){
                return; // Si el usuario cancela, no hacer nada
            } else {
                controlador.modificarCliente();
                mostrarMensaje("✔ Registro modificado.");
                botonModificar.setEnabled(false);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + ex.getMessage());
        }
    }    
}