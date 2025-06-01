package Ventanas;

import Entidades.Cliente;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
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
    }
    private void ingresarCliente(ActionEvent e) {
        try {
            if (!getEncontrado()) {
            // Ingresar al cliente desde campos del formulario
                Cliente cliente = new Cliente();
                cliente.setDni(validarDNI(campoDni.getText()));
                cliente.setEdad(validarEdad(campoEdad.getText()));
                cliente.setNombre(validarNombre(campoNombre.getText()));
                cliente.setCorreo(validarCorreo(campoCorreo.getText()));
                cliente.setIniSus(LocalDate.parse(campoInisus.getText())); // Usa el formato correcto (yyyy-MM-dd)
                cliente.setCelular(validarCelular(campoCelular.getText()));

                archivo = new RandomAccessFile(ruta, "rw");
                archivo.seek(archivo.length()); // Mover el puntero al final del archivo
                cliente.escribir(archivo); // Llamar al método escribir de Cliente
                archivo.close();

                JOptionPane.showMessageDialog(null, "Cliente guardado exitosamente.");
                botonIngresar.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Cliente ya se encuentra registrado.");
                }
            } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error al guardar: " + ex.getMessage());
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
