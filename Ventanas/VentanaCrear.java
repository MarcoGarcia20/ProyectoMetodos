package Ventanas;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class VentanaCrear extends JFrame {
    private JLabel dni;
    private JLabel edad;
    private JLabel nombre;
    private JLabel correo;
    private JLabel inisus;
    private JLabel celular;
    private JTextField campoDni;
    private JTextField campoEdad;
    private JTextField campoNombre;
    private JTextField campoCorreo;
    private JTextField campoInisus;
    private JTextField Celular;
    private JButton botonVerificar;
    private JButton botonNuevo;
    private JLabel resultado;

    public VentanaCrear(){
        setTitle("Crear cliente");
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

        edad = new JLabel("Edad");
        edad.setBounds(20, 50, 200, 25);
        add(edad);
        campoEdad = new JTextField();
        campoEdad.setBounds(80, 50, 200, 25);
        add(campoEdad);

        nombre= new JLabel("Nombre");
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

        botonNuevo = new JButton("Crear");
        botonNuevo.setBounds(40, 220, 90, 25);
        add(botonNuevo);

        // resultado = new JLabel("Resultado");
        // resultado.setBounds(20, 300, 300, 25);
        // add(resultado);
    }
}