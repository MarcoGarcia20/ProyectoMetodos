package src.Vistas.VentanasCliente;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;

import src.Controladores.ClienteControlador;

public class VentanaCrearArchivoCliente extends JFrame{
    private JLabel texto;
    private JButton botonCrearArchivo;
    public JSpinner cantidad;

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
        texto = new JLabel("Ingrese la cantidad de clientes a crear:");
        texto.setBounds(50, 20, 300, 30);

        cantidad = new JSpinner();
        cantidad.setBounds(50, 50, 300, 30);
        
        botonCrearArchivo = new JButton("Crear Archivo");
        botonCrearArchivo.setBounds(150, 100, 120, 30);
        botonCrearArchivo.addActionListener(e -> onCrearArchivo());
        
        add(texto);
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
            int cant = Integer.parseInt(cantidad.getValue().toString());
            if (cant <= 0) throw new NumberFormatException();
            controlador.crearArchivoClientes(cant); // Método en el controlador
        } catch (NumberFormatException ex) {
            mostrarMensaje("Ingrese una cantidad válida.");
        }
    }
}
