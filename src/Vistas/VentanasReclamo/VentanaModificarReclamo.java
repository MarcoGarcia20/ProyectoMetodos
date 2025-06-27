package src.Vistas.VentanasReclamo;

import javax.swing.JButton;

public class VentanaModificarReclamo extends VentanaFormularioReclamo {
    private JButton botonModificar;

    public VentanaModificarReclamo() {
        super("Modificar Reclamo");
    }

    @Override
    protected void configurar() {
        // Configuración específica para la ventana de modificación de reclamos
        botonModificar = new JButton("Modificar Reclamo");
        botonModificar.setBounds(200, 350, 150, 30);
        add(botonModificar);
    }
}
