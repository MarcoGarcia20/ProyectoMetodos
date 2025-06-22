package Vistas.VentanasCliente;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Persistencia.ClienteRepositorio;

public class VentanaCompactarCliente extends JFrame {

    private JButton btnCompactar;
    private JTextArea txtMensajes;

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
                try {
                    repositorio.compactarPorCopia();
                    txtMensajes.setText("¡Compactación exitosa!");
                } catch (Exception ex) {
                    txtMensajes.setText("Error: " + ex.getMessage());
                }
            }
        });

        add(btnCompactar, BorderLayout.NORTH);
        add(new JScrollPane(txtMensajes), BorderLayout.CENTER);
    }
}