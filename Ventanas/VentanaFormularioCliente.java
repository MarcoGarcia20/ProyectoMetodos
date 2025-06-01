package Ventanas;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Entidades.Cliente;

public abstract class VentanaFormularioCliente extends JFrame {
    private final Validador<Cliente> validador = new ValidarCliente();

    protected String ruta = "C:\\\\Users\\\\MARCO\\\\Metodos\\\\Sistema\\\\Archivos\\\\Clientes.dat";
    protected RandomAccessFile archivo = null;
    protected JTextField campoDni, campoNombre, campoCorreo, campoCelular, campoEdad, campoInisus;
    protected JButton botonBuscar;
    protected boolean encontrado = false;
    protected Map<String, Long> indiceDNI = new HashMap<>();

    public VentanaFormularioCliente(String titulo) {
        initComponents();
        setSize(400, 300);
        setLocationRelativeTo(null);
        setTitle(titulo);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        configurar();
        construirIndice(); // Construir el índice al iniciar la ventana
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        addLabel("DNI", 20);
        addTextField("DNI", 20);

        addLabel("Edad", 60);
        addTextField("Edad", 60);

        addLabel("Nombre", 100);
        addTextField("Nombre", 100);

        addLabel("Correo", 140);
        addTextField("Correo", 140);

        addLabel("Celular", 180);
        addTextField("Celular", 180);

        addLabel("Inicio Sus", 220);
        addTextField("Inicio Sus", 220);

        botonBuscar = new JButton("Buscar");
        botonBuscar.setBounds(280,20,80,25);
        add(botonBuscar);
        botonBuscar.addActionListener(this::buscarCliente);
    }

    private void addLabel(String texto, int y){
        JLabel label = new JLabel(texto);
        label.setBounds(20,y,100,25);
        add(label);
    }

    private void addTextField(String texto, int y){
        JTextField campo = new JTextField();
        campo.setBounds(120,y,150,25);
        add(campo);
        switch (texto) {
            case "DNI":
                campoDni = campo;
                break;
            case "Edad":
                campoEdad = campo;
                break;
            case "Nombre":
                campoNombre = campo;
                break;
            case "Correo":
                campoCorreo = campo;
                break;
            case "Celular":
                campoCelular = campo;
                break;
            case "Inicio Sus":
                campoInisus = campo;
                break;
        }
    }
    protected boolean getEncontrado(){
        return encontrado;
    }

    protected Cliente obtenerClienteDesdeCampos() {
        Cliente cliente = new Cliente();
        cliente.setDni(campoDni.getText().trim());
        cliente.setEdad(Integer.parseInt(campoEdad.getText().trim()));
        cliente.setNombre(campoNombre.getText().trim());
        cliente.setCorreo(campoCorreo.getText().trim());
        cliente.setCelular(campoCelular.getText().trim());
        cliente.setIniSus(LocalDate.parse(campoInisus.getText().trim())); // Asegúrate de que el formato sea correcto
        return cliente;
    }
    private void construirIndice(){
        try {
            archivo = new RandomAccessFile(ruta, "r");
            indiceDNI.clear(); // Limpiar el índice antes de construirlo
            int numeroRegistro = 0;
            while (archivo.getFilePointer() < archivo.length()) {
                // Guardar la posición inicial del registro
                long posicionInicial = archivo.getFilePointer();
                // Leer solo el DNI del registro
                byte[] bufferDNI = new byte[8];
                archivo.readFully(bufferDNI);
                String dni = new String(bufferDNI, "ISO-8859-1").trim();
                //Almacenar en el índice
                indiceDNI.put(dni, (long) numeroRegistro);

                archivo.seek(posicionInicial + Cliente.LONGITUD_REGISTRO); // Mover el puntero al siguiente registro
                numeroRegistro++;
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al construir el índice: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (archivo != null) {
                    archivo.close();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al cerrar el archivo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    protected void buscarCliente(ActionEvent e) {
        String buscarDni = campoDni.getText().trim();
        encontrado = false;
        // Verificar si el DNI existe en el índice
        if (!indiceDNI.containsKey(buscarDni)) {
            JOptionPane.showMessageDialog(this, 
            "Cliente no encontrado", 
            "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        long numeroRegistro = indiceDNI.get(buscarDni);
        try{
            archivo = new RandomAccessFile(ruta, "r");
            Cliente cliente = new Cliente();
            cliente.posicionar(archivo, (int) numeroRegistro);
            // Leer los datos del cliente
            cliente.leer(archivo);

            campoEdad.setText(String.valueOf(cliente.getEdad()));
            campoNombre.setText(cliente.getNombre());
            campoCorreo.setText(cliente.getCorreo());
            campoCelular.setText(cliente.getCelular());
            campoInisus.setText(cliente.getIniSus().toString());
            campoDni.setText(cliente.getDni());
            encontrado = true;

            JOptionPane.showMessageDialog(this, "Cliente encontrado", "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar el cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    protected void limpiarCampos() {
        campoDni.setText("");
        campoEdad.setText("");
        campoNombre.setText("");
        campoCorreo.setText("");
        campoCelular.setText("");
        campoInisus.setText("");
    }

    protected abstract void configurar();   
}
