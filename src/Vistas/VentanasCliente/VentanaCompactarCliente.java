package src.Vistas.VentanasCliente;

import javax.swing.*;

import src.Controladores.ClienteControlador;
import src.Persistencia.ClienteRepositorio;

import java.awt.*;
import java.awt.event.*;

public class VentanaCompactarCliente extends JFrame {

    private JButton btnCompactar;
    private JTextArea txtMensajes;

    private ClienteControlador controlador;

    public VentanaCompactarCliente(ClienteRepositorio repositorio) {
        setTitle("Compactar Archivo de Clientes");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        btnCompactar = new JButton("Compactar Archivo");
        txtMensajes = new JTextArea();
        txtMensajes.setEditable(false);

        btnCompactar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador != null) {
                    controlador.compactarArchivo();
                }
            }
        });

        add(btnCompactar, BorderLayout.NORTH);
        add(new JScrollPane(txtMensajes), BorderLayout.CENTER);
    }

    public void setControlador(ClienteControlador controlador) {
        this.controlador = controlador;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
}