package src.Vistas.VentanasCliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import src.Entidades.Cliente;

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
        add(btnEliminar);
        btnEliminar.addActionListener(this::eliminarCliente);
        btnEliminar.setEnabled(false); // Deshabilitar inicialmente
    }
    @Override
    protected void buscarCliente(ActionEvent e){
        super.buscarCliente(e);
        btnEliminar.setEnabled(encontrado); // Habilitar el botón de eliminar si se encuentra el cliente
    }
    
    private void eliminarCliente(ActionEvent e) {
        try {
            int confirm = JOptionPane.showConfirmDialog(this,
             "¿Deseas eliminar este registro?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION){
                mostrarMensaje("Operación cancelada por el usuario.");
                return; // Si el usuario cancela, no hacer nada
            }
            boolean eliminado = controlador.eliminarCliente(); // El controlador retorna si tuvo éxito
            btnEliminar.setEnabled(false); // Deshabilitar el botón después de eliminar
        } catch (Exception ex) {
            mostrarMensaje("Error al eliminar: " + ex.getMessage());
        }
    }
}
