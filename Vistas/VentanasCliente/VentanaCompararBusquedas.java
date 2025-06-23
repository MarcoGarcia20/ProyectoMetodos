package Vistas.VentanasCliente;

import Controladores.ClienteControlador;
import Persistencia.ClienteRepositorio;
import Entidades.Cliente;

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
        long sumaSecuencial = 0;
        long sumaBinaria = 0;
        int encontradosSecuencial = 0;
        int encontradosBinaria = 0;

        List<Cliente> listaOrdenada;
        try {
            listaOrdenada = repositorio.listarClientes();
            repositorio.ordenarPorInsercion(listaOrdenada);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer u ordenar clientes: " + ex.getMessage());
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("DNI\tSecuencial(ns)\tBinaria(ns)\tSecuencial\tBinaria\n");
        sb.append("---------------------------------------------------------------\n");

        for (String dni : dnis) {
            dni = dni.trim();
            long inicioSec = System.nanoTime();
            Cliente cliSec = null;
            try {
                cliSec = repositorio.busquedaSecuencial(dni);
            } catch (IOException ex) {
            }
            long finSec = System.nanoTime();

            long inicioBin = System.nanoTime();
            Cliente cliBin = repositorio.busquedaBinariaEnLista(listaOrdenada, dni);
            long finBin = System.nanoTime();

            long tiempoSec = finSec - inicioSec;
            long tiempoBin = finBin - inicioBin;
            sumaSecuencial += tiempoSec;
            sumaBinaria += tiempoBin;
            if (cliSec != null)
                encontradosSecuencial++;
            if (cliBin != null)
                encontradosBinaria++;

            sb.append(dni).append("\t")
                    .append(tiempoSec).append("\t")
                    .append(tiempoBin).append("\t")
                    .append(cliSec != null ? "✔" : "✘").append("\t\t")
                    .append(cliBin != null ? "✔" : "✘").append("\n");
        }
        int n = dnis.length;
        sb.append("\nPromedio Secuencial: ").append(sumaSecuencial / n).append(" ns");
        sb.append("\nPromedio Binaria: ").append(sumaBinaria / n).append(" ns");
        sb.append("\nEncontrados Secuencial: ").append(encontradosSecuencial).append("/").append(n);
        sb.append("\nEncontrados Binaria: ").append(encontradosBinaria).append("/").append(n);

        txtResultados.setText(sb.toString());
    }
}
