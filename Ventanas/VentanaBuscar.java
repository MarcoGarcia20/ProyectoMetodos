package Ventanas;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VentanaBuscar extends JFrame {

    private JLabel dni;

    private JTextField campoDni;

    private JButton botonVerificar;
    //private JButton botonNuevo;
    private JLabel resultado;
    private HashSet<String> registros;

    public VentanaBuscar() {
        setTitle("Verificador de Números");
        setSize(350, 400);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        dni = new JLabel("DNI");
        dni.setBounds(20, 20, 200, 25);
        add(dni);
        campoDni = new JTextField();
        campoDni.setBounds(80, 20, 200, 25);
        add(campoDni);

        
        botonVerificar = new JButton("Verificar");
        botonVerificar.setBounds(230, 220, 90, 25);
        add(botonVerificar);


        resultado = new JLabel("Resultado");
        resultado.setBounds(20, 300, 300, 25);
        add(resultado);

        registros = new HashSet<>();
        cargarRegistros("C:\\\\Users\\\\MARCO\\\\Metodos\\\\Sistema\\\\Archivos\\\\Prueba1.txt");

        botonVerificar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String numero = campoDni.getText().trim();
                if (!numero.matches("\\d{9}")) {
                    resultado.setText("⚠ Ingrese un número de 9 dígitos.");
                    return;
                }

                if (registros.contains(numero)) {
                    
                    resultado.setText("✔ DNI registrado.");
                } else {
                    resultado.setText("✘ DNI NO encontrado.");
                }
            }
        });

        setVisible(true);
    }

    private void cargarRegistros(String rutaArchivo) {
        try {
            StringBuilder contenido = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea);
            }
            br.close();

            // Buscar todos los números de 9 dígitos (celulares)
            Pattern patron = Pattern.compile("\\b\\d{9}\\b");
            Matcher matcher = patron.matcher(contenido.toString());

            while (matcher.find()) {
                registros.add(matcher.group());
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + e.getMessage());
        }
    }
}
