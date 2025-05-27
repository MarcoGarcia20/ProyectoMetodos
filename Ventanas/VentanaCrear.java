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

public class VentanaCrear extends JFrame {
    private JLabel dni, edad, nombre, correo, inisus, celular;
    private JTextField campoDni, campoEdad, campoNombre, campoCorreo, campoInisus, Celular;
    private JButton botonNuevo;

    public VentanaCrear() {
        setTitle("Crear cliente");
        setSize(350, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // <- Cierra solo esta ventana

        // Etiquetas y campos
        dni = new JLabel("DNI");
        dni.setBounds(20, 20, 200, 25);
        add(dni);
        campoDni = new JTextField();
        campoDni.setBounds(80, 20, 200, 25);
        add(campoDni);

        edad = new JLabel("Edad");
        edad.setBounds(20, 50, 200, 25);
        add(edad);
        campoEdad = new JTextField();
        campoEdad.setBounds(80, 50, 200, 25);
        add(campoEdad);

        nombre = new JLabel("Nombre");
        nombre.setBounds(20, 80, 200, 25);
        add(nombre);
        campoNombre = new JTextField();
        campoNombre.setBounds(80, 80, 200, 25);
        add(campoNombre);

        correo = new JLabel("Correo");
        correo.setBounds(20, 110, 200, 25);
        add(correo);
        campoCorreo = new JTextField();
        campoCorreo.setBounds(80, 110, 200, 25);
        add(campoCorreo);

        inisus = new JLabel("Ini. Sus");
        inisus.setBounds(20, 140, 200, 25);
        add(inisus);
        campoInisus = new JTextField();
        campoInisus.setBounds(80, 140, 200, 25);
        add(campoInisus);

        celular = new JLabel("Celular");
        celular.setBounds(20, 170, 200, 25);
        add(celular);
        Celular = new JTextField();
        Celular.setBounds(80, 170, 200, 25);
        add(Celular);

        // Botón Crear
        botonNuevo = new JButton("Crear");
        botonNuevo.setBounds(40, 220, 90, 25);
        add(botonNuevo);

    // Acción del botón "Crear"
    botonNuevo.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            // 1. Crear cliente desde campos del formulario
            Cliente cliente = new Cliente();
            cliente.setDni(validarDNI(campoDni.getText()));
            cliente.setEdad(validarEdad(campoEdad.getText()));
            cliente.setNombre(validarNombre(campoNombre.getText()));
            cliente.setCorreo(validarCorreo(campoCorreo.getText()));
            cliente.setIniSus(LocalDate.parse(campoInisus.getText())); // Usa el formato correcto (yyyy-MM-dd)
            cliente.setCelular(validarCelular(Celular.getText()));

            // 2. Abrir archivo en modo lectura-escritura binaria
            try (RandomAccessFile archivo = new RandomAccessFile("C:\\\\Users\\\\MARCO\\\\Metodos\\\\Sistema\\\\Archivos\\\\Sistema.dat", "rw")) {
                archivo.seek(archivo.length()); // Mover el puntero al final
                cliente.escribir(archivo);     // Llamar a tu método
            }

            JOptionPane.showMessageDialog(null, "Cliente guardado exitosamente.");
            dispose(); // Cierra solo esta ventana

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar: " + ex.getMessage());
        }
    }
    });

        setVisible(true);
    }

    private String validarDNI(String dni) {
        if (!dni.matches("\\d{8}")) {
            throw new IllegalArgumentException("DNI debe tener 8 dígitos");
        }
        return dni;
    }

    private int validarEdad(String edadStr) {
        try {
            int edad = Integer.parseInt(edadStr);
            if (edad < 18 || edad > 100) throw new IllegalArgumentException();
            return edad;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Edad debe ser un número entre 18-100");
        }
    }

    private String validarNombre(String nombre) {
        if (nombre.length() > 20) {
            throw new IllegalArgumentException("Nombre máximo 20 caracteres");
        }
        return nombre.trim();
    }

    private String validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("Correo no puede estar vacío");
        }
        if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Correo no válido");
        }
        return correo.trim();
    }

    private LocalDate validarIniSus(String iniSus) {
        try {
            return LocalDate.parse(iniSus);
        } catch (Exception e) {
            throw new IllegalArgumentException("Fecha de inicio debe tener formato yyyy-MM-dd");
        }
    }

    private String validarCelular(String celular) {
        if (!celular.matches("\\d{9}")) {
            throw new IllegalArgumentException("Celular debe tener 9 dígitos");
        }
        return celular;
    }
}
