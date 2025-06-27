package src.Vistas.VentanasCliente;


import java.util.List;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import src.Controladores.ClienteControlador;
import src.Entidades.Cliente;
import src.Vistas.IVista;

public class VentanaListarCliente extends JFrame implements IVista<Cliente> {
    private JTable tabla;
    private DefaultTableModel modelo;

    private JButton botonListar;

    private ClienteControlador controlador;

    public VentanaListarCliente() {
        setTitle("Listar Clientes");
        setSize(800, 800);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnas = {"DNI", "Edad", "Nombre", "Correo", "Fecha de Inicio", "Celular"};
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(50, 50, 700, 700);
        add(scroll);
            

        botonListar = new JButton("Listar Clientes");
        botonListar.setBounds(20, 20, 150, 25);
        add(botonListar);
        botonListar.addActionListener(e -> {
            if (controlador != null) {
                controlador.mostrarClientes();
            } else {
                mostrarMensaje("Controlador no asignado.");
            }
        });
    }
    public void setControlador(ClienteControlador controlador) {
        this.controlador = controlador;
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
    @Override
    public void listar(List<Cliente> clientes) {
        modelo.setRowCount(0); // Limpiar la tabla antes de listar
        for (Cliente c : clientes) {
            if (c != null && c.getDni() != null && !c.getDni().isEmpty()) {
                modelo.addRow(new Object[]{
                    c.getDni(),
                    c.getEdad(),
                    c.getNombre(),
                    c.getCorreo(),
                    c.getIniSus(),
                    c.getCelular()
                });
            }
        }
    } 
}
