package Ventanas;

import java.awt.*;

import javax.swing.*;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        initComponents();
        setSize(500, 500);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jMenuBar1 = new JMenuBar();
        JMenu jMenu1 = new JMenu("Archivo");
        JMenu jMenu2 = new JMenu("Editar");
        JMenu jMenu3 = new JMenu("Ver");
        JMenu jMenu4 = new JMenu("Ayuda");
        
        // Crear el submenú "Cliente"
        JMenu clienteMenu = new JMenu("Cliente");

        JMenuItem ingresarMenuItem = new JMenuItem("Ingresar Cliente");
        JMenuItem consultarMenuItem = new JMenuItem("Consultar Cliente");
        JMenuItem listarMenuItem = new JMenuItem("Listar Clientes");
        JMenuItem modificarMenuItem = new JMenuItem("Modificar Cliente");
        JMenuItem eliminarMenuItem = new JMenuItem("Eliminar Cliente");

        // Agregar ActionListeners para cada opción del menú
        ingresarMenuItem.addActionListener(e -> {
            VentanaIngresarCliente ventanaCrear = new VentanaIngresarCliente();
            ventanaCrear.setVisible(true);
        });
        consultarMenuItem.addActionListener(e -> {
            VentanaConsultarCliente ventanaBuscar = new VentanaConsultarCliente();
            ventanaBuscar.setVisible(true);
        });
        listarMenuItem.addActionListener(e -> {
            VentanaListarCliente ventanaListar = new VentanaListarCliente();
            ventanaListar.setVisible(true);
        });
        modificarMenuItem.addActionListener(e -> {
            VentanaModificarCliente ventanaModificar = new VentanaModificarCliente();
            ventanaModificar.setVisible(true);
        });
        eliminarMenuItem.addActionListener(e -> {
            VentanaEliminarCliente ventanaEliminar = new VentanaEliminarCliente();
            ventanaEliminar.setVisible(true);
        });

        // Agregar los items al submenú "Cliente"
        clienteMenu.add(ingresarMenuItem);
        clienteMenu.add(consultarMenuItem);
        clienteMenu.add(listarMenuItem);
        clienteMenu.add(modificarMenuItem);
        clienteMenu.add(eliminarMenuItem);

        // Agregar el submenú "Cliente" al menú "Archivo"
        jMenu1.add(clienteMenu);

        jMenuBar1.add(jMenu1);
        jMenuBar1.add(jMenu2);
        jMenuBar1.add(jMenu3);
        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Sistema de Reclamos Konecta");
        setResizable(false);

        jLabel1.setText("Ventana Principal");

        pack();
    }

    private JMenuBar jMenuBar1;
    private JLabel jLabel1 = new JLabel("Ventana Principal");
}
