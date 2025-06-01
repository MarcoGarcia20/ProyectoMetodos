package Ventanas;
import Entidades.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.*;
import java.io.IOException;
import java.io.RandomAccessFile;

public class VentanaEliminarCliente extends VentanaFormularioCliente{
    private JButton btnEliminar;
    private long posicionRegistro = -1; // Para guardar la posición del registro encontrado

    public VentanaEliminarCliente() {
        super("Eliminar Cliente");
    }
    @Override
    protected void configurar() {
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(280, 220, 90, 25);
        btnEliminar.addActionListener(this::eliminarRegistro);
        add(btnEliminar);
        btnEliminar.setEnabled(false); // Deshabilitar inicialmente
        desabilitarCampos();
    }
    @Override
    protected void buscarCliente(ActionEvent e) {
        super.buscarCliente(e);
        if (getEncontrado()) {
            // Guardar posición del registro encontrado
            posicionRegistro = indiceDNI.get(campoDni.getText().trim());
            btnEliminar.setEnabled(true);  // Habilitar eliminación
        }
    }

    private void eliminarRegistro(ActionEvent e) {
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                archivo = new RandomAccessFile(ruta, "rw");
                archivo.seek(posicionRegistro * Cliente.LONGITUD_REGISTRO); // Mover el puntero al registro encontrado
                archivo.write(String.format("%-8s", "").getBytes("ISO-8859-1")); // Escribir un registro vacío (eliminar)
                indiceDNI.remove(campoDni.getText().trim()); // Eliminar del índice
                JOptionPane.showMessageDialog(this, "✔ Registro eliminado.");
                limpiarCampos();
                btnEliminar.setEnabled(false);
                archivo.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al posicionar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
