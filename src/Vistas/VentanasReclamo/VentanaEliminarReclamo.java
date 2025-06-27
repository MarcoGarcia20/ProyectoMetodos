package src.Vistas.VentanasReclamo;

import javax.swing.JButton;

public class VentanaEliminarReclamo extends VentanaFormularioReclamo {
    private JButton botonEliminar;

    public VentanaEliminarReclamo() {
        super("Eliminar Reclamo");
    }

    @Override
    protected void configurar() {
        // Configuración específica para la ventana de eliminación de reclamos
        botonEliminar = new JButton("Eliminar Reclamo");
        botonEliminar.setBounds(200, 350, 150, 30);
        add(botonEliminar);
    }
}
