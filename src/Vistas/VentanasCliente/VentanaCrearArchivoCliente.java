package src.Vistas.VentanasCliente;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import src.Controladores.ClienteControlador;

public class VentanaCrearArchivoCliente extends JFrame{
    private JButton botonCrearArchivo;
    public JTextField cantidad;

    private ClienteControlador controlador;
    
    public VentanaCrearArchivoCliente(){
        setTitle("Crear Archivo Cliente");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        initComponents();
    }
    private void initComponents() {

        cantidad = new JTextField();
        cantidad.setBounds(50, 50, 300, 30);
        
        botonCrearArchivo = new JButton("Crear Archivo");
        botonCrearArchivo.setBounds(150, 100, 120, 30);
        botonCrearArchivo.addActionListener(e -> onCrearArchivo());
        
        add(botonCrearArchivo);
        add(cantidad);
    }

    public void setControlador(ClienteControlador controlador) {
        this.controlador = controlador;
    }
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    private void onCrearArchivo() {
        if (controlador == null) {
            mostrarMensaje("Error interno: Controlador no está configurado.");
            return;
        }
        try {
            int cant = Integer.parseInt(cantidad.getText().trim());
            if (cant <= 0) throw new NumberFormatException();
            controlador.crearArchivoClientes(cant); // Método en el controlador
        } catch (NumberFormatException ex) {
            mostrarMensaje("Ingrese una cantidad válida.");
        }
    }
}
