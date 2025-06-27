package src.Vistas.VentanasReclamo;

import javax.swing.JButton;

public class VentanaIngresarReclamo extends VentanaFormularioReclamo {
    private JButton botonIngresar;

    public VentanaIngresarReclamo() {
        super("Ingresar Reclamo");
    }

    @Override
    protected void configurar() {
        // Configuración específica para la ventana de ingreso de reclamos
        botonIngresar = new JButton("Ingresar Reclamo");
        botonIngresar.setBounds(200, 350, 150, 30);
        add(botonIngresar);
    }
}
