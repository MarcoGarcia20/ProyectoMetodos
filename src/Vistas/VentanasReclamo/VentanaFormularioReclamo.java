package src.Vistas.VentanasReclamo;

import java.awt.*;
import java.util.Date;
import java.time.ZoneId;
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import javax.swing.*;

import src.Controladores.ReclamoControlador;
import src.Entidades.Reclamo;

import com.toedter.calendar.JDateChooser;


public abstract class VentanaFormularioReclamo extends JFrame {
    protected JTextField campoIdReclamo, campoDniCliente, campoNroLinea, campoEstado;
    protected JTextArea campoDescripcion, campoSolucion;
    protected JDateChooser campoFecha;

    protected JButton botonBuscar;
    protected ReclamoControlador controlador;
    protected boolean encontrado = false;

    public VentanaFormularioReclamo(String titulo) {
        setTitle(titulo);
        setSize(450, 480);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        configurar(); // Método abstracto para que cada subclase configure su propio comportamiento
    }
    public void isEncontrado(boolean encontrado) {
        this.encontrado = encontrado;
    }

    /** Permite inyectar el controlador */
    public void setControlador(ReclamoControlador controlador) {
        this.controlador = controlador;
    }

    private void initComponents() {
        int y = 20;

        addLabel("ID Reclamo", y);
        campoIdReclamo = addTextField(y);

        y += 40;
        addLabel("DNI Cliente", y);
        campoDniCliente = addTextField(y);

        y += 40;
        addLabel("Nro Línea", y);
        campoNroLinea = addTextField(y);

        y += 40;
        addLabel("Descripción", y);
        campoDescripcion = addTextArea(y);

        y += 60;
        addLabel("Fecha (YYYY-MM-DD)", y);
        campoFecha = new JDateChooser();
        campoFecha.setBounds(200, y, 200, 25);
        add(campoFecha);

        y += 40;
        addLabel("Estado (true/false)", y);
        campoEstado = addTextField(y);

        y += 40;
        addLabel("Solución", y);
        campoSolucion = addTextArea(y);

        y += 70;
        botonBuscar = new JButton("Buscar");
        botonBuscar.setBounds(30, y, 100, 30);
        add(botonBuscar);

        botonBuscar.addActionListener(e -> {
            buscarReclamo(e);
        });
    }

    /** Añade un JLabel a la ventana */
    protected void addLabel(String texto, int y) {
        JLabel label = new JLabel(texto);
        label.setBounds(30, y, 160, 25);
        add(label);
    }

    /** Añade un JTextField a la ventana */
    protected JTextField addTextField(int y) {
        JTextField campo = new JTextField();
        campo.setBounds(200, y, 200, 25);
        add(campo);
        return campo;
    }

    /** Añade un JTextArea a la ventana */
    protected JTextArea addTextArea(int y) {
        JTextArea campo = new JTextArea();
        JScrollPane scroll = new JScrollPane(campo);
        scroll.setBounds(200, y, 200, 50);
        add(scroll);
        return campo;
    }

    /**
     * Método abstracto para que cada subclase configure su propio comportamiento
     */
    protected abstract void configurar();

    protected void buscarReclamo(ActionEvent e) {
        if (controlador == null) {
            mostrarMensaje("Controlador no asignado.");
            encontrado = false;
        }
        try {
            String idReclamo = campoIdReclamo.getText().trim();
            if (idReclamo.isEmpty()) {
                mostrarMensaje("El campo ID Reclamo no puede estar vacío.");
                encontrado = false;
                return;
            }
            Reclamo reclamo = controlador.consultarReclamo(idReclamo);
            if (reclamo != null) {
                mostrarReclamo(reclamo);
                mostrarMensaje("✔ Reclamo encontrado.");
                habilitarCampos();
                encontrado = true; // Marcar como encontrado
            } else {
                mostrarMensaje("Reclamo no encontrado.");
                limpiarCamposSinId(); // Limpiar campos excepto ID
                encontrado = false; // Marcar como no encontrado
            }
        } catch (Exception ex) {
            mostrarMensaje("Error al buscar reclamo: " + ex.getMessage());
            encontrado = false; // Marcar como no encontrado en caso de error
        }
    }

    /**
     * Métodos utilitarios para subclases (ejemplo: obtener datos, limpiar campos,
     * etc.)
     */
    public String getIdReclamo() {
        return campoIdReclamo.getText().trim();
    }

    public String getDniCliente() {
        return campoDniCliente.getText().trim();
    }

    public String getNroLinea() {
        return campoNroLinea.getText().trim();
    }

    public String getDescripcion() {
        return campoDescripcion.getText().trim();
    }

    public String getFecha() {
        Date fecha = campoFecha.getDate();
        return fecha != null ? fecha.toString() : "";
    }

    public String getEstado() {
        return campoEstado.getText().trim();
    }

    public String getSolucion() {
        return campoSolucion.getText().trim();
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public Reclamo obtenerReclamosDesdeCampos() {
        try {
            Reclamo reclamo = new Reclamo();
            reclamo.setIdReclamo(getIdReclamo());
            reclamo.setDniCliente(getDniCliente());
            reclamo.setNumeroLineaCliente(getNroLinea());
            reclamo.setDescripcion(getDescripcion());
            reclamo.setEstado(Boolean.parseBoolean(getEstado()));
            reclamo.setSolucion(getSolucion());
            return reclamo;
        } catch (Exception e) {
            mostrarMensaje("Error al obtener datos: " + e.getMessage());
            return null;
        }
    }

    public void mostrarReclamo(Reclamo reclamo) {
        campoIdReclamo.setText(reclamo.getIdReclamo());
        campoDniCliente.setText(reclamo.getDniCliente());
        campoNroLinea.setText(reclamo.getNumeroLineaCliente());
        campoDescripcion.setText(reclamo.getDescripcion());
        // Poner la fecha en el JDateChooser
        if (reclamo.getFecha() != null) {
            java.util.Date fecha = java.util.Date.from(
                    reclamo.getFecha().atStartOfDay(ZoneId.systemDefault()).toInstant());
            campoFecha.setDate(fecha);
        } else {
            campoFecha.setDate(null);
        }
        campoEstado.setText(reclamo.getEstado() != null && reclamo.getEstado() ? "true" : "false");
        campoSolucion.setText(reclamo.getSolucion());
    }

    public void limpiarCamposSinId() {
        campoDniCliente.setText("");
        campoNroLinea.setText("");
        campoDescripcion.setText("");
        campoFecha.setDate(null);
        campoEstado.setText("");
        campoSolucion.setText("");
    }

    public void limpiarCampos() {
        campoIdReclamo.setText("");
        limpiarCamposSinId();
    }

    public void habilitarCampos() {
        campoIdReclamo.setEnabled(false);
        campoDniCliente.setEnabled(false);
        campoNroLinea.setEnabled(true);
        campoDescripcion.setEnabled(true);
        campoFecha.setEnabled(true);
        campoEstado.setEnabled(true);
        campoSolucion.setEnabled(true);
    }

    public void deshabilitarCampos() {
        campoIdReclamo.setEnabled(false);
        campoDniCliente.setEnabled(false);
        campoNroLinea.setEnabled(false);
        campoDescripcion.setEnabled(false);
        campoFecha.setEnabled(false);
        campoEstado.setEnabled(false);
        campoSolucion.setEnabled(false);
    }

    
}
