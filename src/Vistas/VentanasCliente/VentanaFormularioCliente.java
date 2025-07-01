package src.Vistas.VentanasCliente;

import java.awt.event.ActionEvent;
import java.util.Date;
import java.time.ZoneId;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import src.Controladores.ClienteControlador;
import src.Entidades.Cliente;

import com.toedter.calendar.JDateChooser;

public abstract class VentanaFormularioCliente extends JFrame {

    protected JTextField campoDni, campoNombre, campoCorreo, campoCelular, campoEdad;
    protected JButton botonBuscar;
    protected JDateChooser dateChooser; // Si se usa un selector de fecha
    protected ClienteControlador controlador;
    protected boolean encontrado = false;

    public VentanaFormularioCliente(String titulo) {
        initComponents();
        setSize(400, 300);
        setLocationRelativeTo(null);
        setTitle(titulo);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        configurar(); // Método abstracto para que cada subclase configure su propio comportamiento
    }

    public boolean isEncontrado() {
        return encontrado;
    }

    // Para establecer el controlador desde afuera
    public void setControlador(ClienteControlador controlador) {
        this.controlador = controlador;
    }

    public String getCampoDni() {
        return campoDni.getText().trim();
    }
    public String getCampoNombre() {
        return campoNombre.getText().trim();
    }
    public String getCampoCorreo() {
        return campoCorreo.getText().trim();
    }
    public String getCampoCelular() {
        return campoCelular.getText().trim();
    }
    public String getCampoEdad() {
        return campoEdad.getText().trim();
    }
    public JDateChooser getDateChooser() {
        return dateChooser;
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
        dateChooser = new JDateChooser();
        dateChooser.setBounds(120, 220, 150, 25);
        add(dateChooser);

        botonBuscar = new JButton("Buscar");
        botonBuscar.setBounds(280, 20, 80, 25);
        add(botonBuscar);

        botonBuscar.addActionListener(e -> {
            buscarCliente(e);
        });
    }

    /**
     * Método que las subclases deben implementar para agregar componentes
     * adicionales
     * o personalizar la ventana.
     */
    protected abstract void configurar();

    protected void buscarCliente(ActionEvent e) {
        if (controlador == null) {
            mostrarMensaje("Error interno: Controlador no está configurado.");
            encontrado = false;
            return;
        }

        try {
            String dni = campoDni.getText().trim();
            if (dni.isEmpty()) {
                mostrarMensaje("El campo DNI no puede estar vacío.");
                encontrado = false;
                return;
            }
            Cliente cliente = controlador.buscarClienteInteligente(dni);
            if (cliente != null) {
                mostrarCliente(cliente);
                habilitarCampos();
                encontrado = true; // Marcar como encontrado
            } else {
                mostrarMensaje("Cliente no encontrado.");
                limpiarCamposSinDNI(); // Limpiar campos excepto DNI

                encontrado = false; // Marcar como no encontrado
            }
        } catch (Exception ex) {
            mostrarMensaje("Error al buscar cliente: " + ex.getMessage());
            encontrado = false; // Marcar como no encontrado en caso de error
        }
    }

    private void addLabel(String texto, int y) {
        JLabel label = new JLabel(texto);
        label.setBounds(20, y, 100, 25);
        add(label);
    }

    private void addTextField(String texto, int y) {
        JTextField campo = new JTextField();
        campo.setBounds(120, y, 150, 25);
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
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public Cliente obtenerClienteDesdeCampos() {
        try {
            Cliente cliente = new Cliente();
            cliente.setDni(campoDni.getText().trim());
            cliente.setEdad(Byte.parseByte(campoEdad.getText().trim()));
            cliente.setNombre(campoNombre.getText().trim());
            cliente.setCorreo(campoCorreo.getText().trim());
            cliente.setCelular(campoCelular.getText().trim());

            // Obtén la fecha del JDateChooser
            Date fecha = dateChooser.getDate();
            if (fecha != null) {
                LocalDate fechaLocal = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                cliente.setIniSus(fechaLocal);
            } else {
                cliente.setIniSus(null);
            }
            return cliente;
        } catch (Exception ex) {
            mostrarMensaje("Verifica los campos: " + ex.getMessage());
            return null;
        }
    }

    public void mostrarCliente(Cliente cliente) {
        campoDni.setText(cliente.getDni());
        campoEdad.setText(String.valueOf(cliente.getEdad()));
        campoNombre.setText(cliente.getNombre());
        campoCorreo.setText(cliente.getCorreo());
        campoCelular.setText(cliente.getCelular());
        // Poner la fecha en el JDateChooser
        if (cliente.getIniSus() != null) {
            java.util.Date fecha = java.util.Date.from(
                    cliente.getIniSus().atStartOfDay(ZoneId.systemDefault()).toInstant());
            dateChooser.setDate(fecha);
        } else {
            dateChooser.setDate(null);
        }
    }

    public void limpiarCampos() {
        campoDni.setText("");
        limpiarCamposSinDNI();
    }

    public void limpiarCamposSinDNI() {
        campoEdad.setText("");
        campoNombre.setText("");
        campoCorreo.setText("");
        campoCelular.setText("");
        dateChooser.setDate(null);
    }

    public void habilitarCampos() {
        campoEdad.setEnabled(true);
        campoNombre.setEnabled(true);
        campoCorreo.setEnabled(true);
        campoCelular.setEnabled(true);
        dateChooser.setEnabled(true);
    }

    public void deshabilitarCampos() {
        JTextField[] campos = { campoEdad, campoNombre, campoCorreo, campoCelular };
        for (JTextField campo : campos) {
            campo.setEnabled(false);
            campo.setDisabledTextColor(campo.getForeground());
            campo.setOpaque(true);
            campo.setBackground(getBackground());
        }
        dateChooser.setEnabled(false);
    }
}
