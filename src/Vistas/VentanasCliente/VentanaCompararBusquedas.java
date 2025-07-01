package src.Vistas.VentanasCliente;

import src.Controladores.ClienteControlador;
import src.Entidades.Cliente;
import src.Persistencia.ClienteRepositorio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class VentanaCompararBusquedas extends JFrame {
    private JTextArea txtResultados;
    private JButton btnComparar;
    private JTextField txtDNIs;
    private ClienteRepositorio repositorio;

    public VentanaCompararBusquedas(ClienteRepositorio repositorio) {
        this.repositorio = repositorio;
        setTitle("Comparar Búsqueda Secuencial vs Binaria");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        txtResultados = new JTextArea();
        txtResultados.setEditable(false);

        txtDNIs = new JTextField();
        txtDNIs.setToolTipText("Ingrese los DNIs separados por coma (,)");

        btnComparar = new JButton("Comparar Búsquedas");

        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.add(new JLabel("DNIs (separados por coma): "), BorderLayout.WEST);
        panelTop.add(txtDNIs, BorderLayout.CENTER);
        panelTop.add(btnComparar, BorderLayout.EAST);

        add(panelTop, BorderLayout.NORTH);
        add(new JScrollPane(txtResultados), BorderLayout.CENTER);

        btnComparar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compararBusquedas();
            }
        });
    }

    private void compararBusquedas() {
        String input = txtDNIs.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese al menos un DNI.");
            return;
        }
        String[] dnis = input.split(",");
        int n = dnis.length;

        // Encabezados de la tabla
        String[] columnNames = { "Método", "Ordenamiento (ms)", "Búsqueda Secuencial (ms)", "Búsqueda Binaria (ms)" };
        Object[][] data = new Object[3][4];

        // RAM
        long tIniOrden = System.nanoTime();
        try {
            repositorio.ordenamientoClasificacionEnRAM();
        } catch (IOException ex) {
            mostrarError("RAM", ex);
            return;
        }
        long tFinOrden = System.nanoTime();
        double tiempoRAM = (tFinOrden - tIniOrden) / 1_000_000.0;
        double tiempoSecRAM = 0, tiempoBinRAM = 0;
        try {
            List<Cliente> listaRAM = repositorio.listarClientes();
            for (String dni : dnis) {
                String d = dni.trim();
                long t1 = System.nanoTime();
                repositorio.busquedaSecuencial(d);
                long t2 = System.nanoTime();
                tiempoSecRAM += (t2 - t1) / 1_000_000.0;
                long t3 = System.nanoTime();
                repositorio.busquedaBinariaEnLista(listaRAM, d);
                long t4 = System.nanoTime();
                tiempoBinRAM += (t4 - t3) / 1_000_000.0;
            }
        } catch (IOException ex) {
            mostrarError("RAM", ex);
        }
        data[0] = new Object[] { "RAM", String.format("%.3f", tiempoRAM), String.format("%.3f", tiempoSecRAM / n),
                String.format("%.3f", tiempoBinRAM / n) };

        // Nodos
        long tIniNodos = System.nanoTime();
        try {
            repositorio.ordenarPorNodos();
        } catch (IOException ex) {
            mostrarError("Nodos", ex);
            return;
        }
        long tFinNodos = System.nanoTime();
        double tiempoNodos = (tFinNodos - tIniNodos) / 1_000_000.0;
        double tiempoSecNodos = 0, tiempoBinNodos = 0;
        try {
            List<Cliente> listaNodos = repositorio.listarClientes();
            for (String dni : dnis) {
                String d = dni.trim();
                long t1 = System.nanoTime();
                repositorio.busquedaSecuencial(d);
                long t2 = System.nanoTime();
                tiempoSecNodos += (t2 - t1) / 1_000_000.0;
                long t3 = System.nanoTime();
                repositorio.busquedaBinariaEnLista(listaNodos, d);
                long t4 = System.nanoTime();
                tiempoBinNodos += (t4 - t3) / 1_000_000.0;
            }
        } catch (IOException ex) {
            mostrarError("Nodos", ex);
        }
        data[1] = new Object[] { "Nodos", String.format("%.3f", tiempoNodos), String.format("%.3f", tiempoSecNodos / n),
                String.format("%.3f", tiempoBinNodos / n) };

        // Indirección
        long tIniInd = System.nanoTime();
        try {
            repositorio.ordenarPorIndireccion();
        } catch (IOException ex) {
            mostrarError("Indirección", ex);
            return;
        }
        long tFinInd = System.nanoTime();
        double tiempoInd = (tFinInd - tIniInd) / 1_000_000.0;
        double tiempoSecInd = 0, tiempoBinInd = 0;
        try {
            List<Cliente> listaInd = repositorio.listarClientes();
            for (String dni : dnis) {
                String d = dni.trim();
                long t1 = System.nanoTime();
                repositorio.busquedaSecuencial(d);
                long t2 = System.nanoTime();
                tiempoSecInd += (t2 - t1) / 1_000_000.0;
                long t3 = System.nanoTime();
                repositorio.busquedaBinariaEnLista(listaInd, d);
                long t4 = System.nanoTime();
                tiempoBinInd += (t4 - t3) / 1_000_000.0;
            }
        } catch (IOException ex) {
            mostrarError("Indirección", ex);
        }
        data[2] = new Object[] { "Indirección", String.format("%.3f", tiempoInd),
                String.format("%.3f", tiempoSecInd / n), String.format("%.3f", tiempoBinInd / n) };

        JTable tabla = new JTable(data, columnNames);
        tabla.setEnabled(false);
        tabla.setRowHeight(28);

        // Panel de scroll para la tabla
        JScrollPane scrollPane = new JScrollPane(tabla);
        txtResultados.setText(""); // Limpiar el área de texto si quieres
        // Quitar cualquier componente anterior y añadir la tabla
        getContentPane().removeAll();
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.add(new JLabel("DNIs (separados por coma): "), BorderLayout.WEST);
        panelTop.add(txtDNIs, BorderLayout.CENTER);
        panelTop.add(btnComparar, BorderLayout.EAST);
        add(panelTop, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void mostrarError(String metodo, Exception ex) {
        JOptionPane.showMessageDialog(this, "Error en método " + metodo + ": " + ex.getMessage());
    }
}
