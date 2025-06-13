package Vistas;

import Entidades.Cliente;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class VentanaIngresarCliente extends VentanaFormularioCliente{

    private JButton botonIngresar;

    public VentanaIngresarCliente() {
        super("Ingresar Cliente");
    }

    @Override
    protected void configurar() {
        botonIngresar = new JButton("Ingresar");
        botonIngresar.setBounds(280, 220, 90, 25);
        add(botonIngresar);
        botonIngresar.addActionListener(this::ingresarCliente);
        botonIngresar.setEnabled(false); // Deshabilitar el botón de ingresar
    }
    @Override
    protected void buscarCliente(ActionEvent e){
        super.buscarCliente(e);
        botonIngresar.setEnabled(!isEncontrado());
        if (isEncontrado()) {
            mostrarMensaje("Cliente ya existe, no se puede ingresar nuevamente.");
    }

    }
    private void ingresarCliente(ActionEvent e) {
        try {
            controlador.ingresarCliente();
            JOptionPane.showMessageDialog(this, "Cliente ingresado correctamente");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    public String validarDNI(String dni) throws Exception {
        if (!dni.matches("\\d{8}")) {
            throw new IllegalArgumentException("DNI debe tener 8 dígitos");
        }
        return dni;
    }

    public int validarEdad(String edadStr) throws Exception {
        try {
            int edad = Integer.parseInt(edadStr);
            if (edad < 18 || edad > 100)
                throw new IllegalArgumentException("Edad debe ser un número entre 18-100");
            return edad;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Edad debe ser un número entre 18-100");
        }
    }

    public String validarNombre(String nombre) throws Exception {
        if (nombre.length() > 20) {
            throw new IllegalArgumentException("Nombre máximo 20 caracteres");
        }
        return nombre.trim();
    }

    public String validarCorreo(String correo) throws Exception {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("Correo no puede estar vacío");
        }
        if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Correo no válido");
        }
        return correo.trim();
    }

    public String validarCelular(String celular) throws Exception {
        if (!celular.matches("\\d{9}")) {
            throw new IllegalArgumentException("Celular debe tener 9 dígitos");
        }
        return celular;
    }

    public LocalDate validarFechaInicio(String fechaInicio) throws Exception {
        try {
            return LocalDate.parse(fechaInicio);
        } catch (Exception e) {
            throw new IllegalArgumentException("Fecha de inicio debe tener formato yyyy-MM-dd");
        }
    } 
}
