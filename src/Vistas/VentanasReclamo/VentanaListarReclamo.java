package src.Vistas.VentanasReclamo;

import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import src.Entidades.Reclamo;
import src.Controladores.ReclamoControlador;
import src.Vistas.IVista;

public class VentanaListarReclamo extends JFrame implements IVista<Reclamo> {
    private JTable tabla;
    private DefaultTableModel modelo;

    private JButton botonListar;

    private ReclamoControlador controlador;

    public VentanaListarReclamo() {
        setTitle("Listar Reclamos");
        setSize(900, 600);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnas = {
            "ID", "DNI Cliente", "Nro Línea", "Descripción", "Fecha", "Estado", "Solución", "Activo"
        };
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 60, 850, 450);
        add(scroll);

        botonListar = new JButton("Listar Reclamos");
        botonListar.setBounds(20, 20, 160, 25);
        add(botonListar);

        botonListar.addActionListener(e -> {
            if (controlador != null) {
                controlador.mostrarReclamos();
            } else {
                mostrarMensaje("Controlador no asignado.");
            }
        });
    }

    public void setControlador(ReclamoControlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    @Override
    public void listar(List<Reclamo> reclamos) {
        modelo.setRowCount(0); // Limpiar la tabla antes de listar
        for (Reclamo r : reclamos) {
            if (r != null && r.getIdReclamo() != null && !r.getIdReclamo().isEmpty()) {
                modelo.addRow(new Object[]{
                    r.getIdReclamo(),
                    r.getDniCliente(),
                    r.getNumeroLineaCliente(),
                    r.getDescripcion(),
                    r.getFecha(),
                    r.getEstado() != null && r.getEstado() ? "Resuelto" : "Pendiente",
                    r.getSolucion(),
                    r.isActivo() ? "Sí" : "No"
                });
            }
        }
    }
}
