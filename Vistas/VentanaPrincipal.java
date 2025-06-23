package Vistas;

import java.awt.*;

import javax.swing.*;

import Controladores.ClienteControlador;
import Persistencia.ClienteRepositorio;
import Vistas.VentanasCliente.VentanaCompactarCliente;
import Vistas.VentanasCliente.VentanaCompararBusquedas;
import Vistas.VentanasCliente.VentanaConsultarCliente;
import Vistas.VentanasCliente.VentanaEliminarCliente;
import Vistas.VentanasCliente.VentanaIngresarCliente;
import Vistas.VentanasCliente.VentanaListarCliente;
import Vistas.VentanasCliente.VentanaModificarCliente;

public class VentanaPrincipal extends JFrame {
    private ClienteRepositorio clienteRepositorio = new ClienteRepositorio();
    
    public VentanaPrincipal() {
        initComponents();
        setSize(510, 500);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jMenuBar1 = new JMenuBar();
        JMenu jMenu1 = new JMenu("Archivos");
        JMenu jMenu2 = new JMenu("Mantenimiento");
        JMenu jMenu3 = new JMenu("Editar");
        JMenu jMenu4 = new JMenu("Ver");
        JMenu jMenu5 = new JMenu("Ayuda");
        
        // Crear el submenú "Cliente"
        JMenu clienteMenu = new JMenu("Cliente");

        JMenuItem ingresarMenuItem = new JMenuItem("Ingresar Cliente");
        JMenuItem consultarMenuItem = new JMenuItem("Consultar Cliente");
        JMenuItem listarMenuItem = new JMenuItem("Listar Clientes");
        JMenuItem modificarMenuItem = new JMenuItem("Modificar Cliente");
        JMenuItem eliminarMenuItem = new JMenuItem("Eliminar Cliente");

        JMenuItem compactarMenuItem = new JMenuItem("Compactar Archivo");
        JMenuItem compararBusquedasMenuItem = new JMenuItem("Comparar Búsquedas por DNI");

        // Agregar ActionListeners para cada opción del menú
        ingresarMenuItem.addActionListener(e -> {
            VentanaIngresarCliente ventanaCrear = new VentanaIngresarCliente();
            ClienteControlador controlador = new ClienteControlador(clienteRepositorio, ventanaCrear);
            ventanaCrear.setControlador(controlador);
            ventanaCrear.setVisible(true);
        });
        consultarMenuItem.addActionListener(e -> {
            VentanaConsultarCliente ventanaConsultarCliente = new VentanaConsultarCliente();
            ClienteControlador controlador = new ClienteControlador(clienteRepositorio, ventanaConsultarCliente);
            ventanaConsultarCliente.setControlador(controlador);
            ventanaConsultarCliente.setVisible(true);
        });
        listarMenuItem.addActionListener(e -> {
            VentanaListarCliente ventanaListar = new VentanaListarCliente();
            ClienteControlador controlador = new ClienteControlador(clienteRepositorio, ventanaListar);
            ventanaListar.setControlador(controlador);
            ventanaListar.setVisible(true);
        });
        modificarMenuItem.addActionListener(e -> {
            VentanaModificarCliente ventanaModificar = new VentanaModificarCliente();
            ClienteControlador controlador = new ClienteControlador(clienteRepositorio, ventanaModificar);
            ventanaModificar.setControlador(controlador);
            ventanaModificar.setVisible(true);
        });
        eliminarMenuItem.addActionListener(e -> {
            VentanaEliminarCliente ventanaEliminar = new VentanaEliminarCliente();
            ClienteControlador controlador = new ClienteControlador(clienteRepositorio, ventanaEliminar);
            ventanaEliminar.setControlador(controlador);
            ventanaEliminar.setVisible(true);
        });

        compactarMenuItem.addActionListener(e -> {
            VentanaCompactarCliente ventana = new VentanaCompactarCliente(clienteRepositorio);
            ventana.setVisible(true);
        });

        compararBusquedasMenuItem.addActionListener(e -> {
            VentanaCompararBusquedas ventana = new VentanaCompararBusquedas(clienteRepositorio);
            ventana.setVisible(true);
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
        jMenuBar1.add(jMenu5);

        // Agregar el item de compactar base de datos al menú "Mantenimiento"
        jMenu2.add(compactarMenuItem);
        jMenu2.add(compararBusquedasMenuItem);

        setJMenuBar(jMenuBar1);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // PANEL CENTRAL CON LOGO Y TÍTULO
       JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(new Color(240, 248, 255));
        panelCentral.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(13, 44, 84), 2, true),
            BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        // Logo
        JLabel logoLabel = new JLabel();
        ImageIcon icon = new ImageIcon(getClass().getResource("/Vistas/konecta_logo.png")); // o el otro logo
        // Escalar la imagen si lo deseas
        Image image = icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(image));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Título
        JLabel tituloLabel = new JLabel("Sistema de Reclamos Konecta");
        tituloLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        tituloLabel.setForeground(new Color(13, 44, 84));
        tituloLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Espacios
        JLabel bienvenida = new JLabel("Bienvenido(a), seleccione una opción del menú para continuar.");
        bienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        bienvenida.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelCentral.add(Box.createVerticalStrut(20));
        panelCentral.add(logoLabel);
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(tituloLabel);
        panelCentral.add(Box.createVerticalStrut(15));
        panelCentral.add(bienvenida);
        panelCentral.add(Box.createVerticalGlue());
        add(panelCentral, BorderLayout.CENTER);

        // Pie de página
        JLabel copyright = new JLabel("© 2025 Konecta. Todos los derechos reservados.");
        copyright.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        copyright.setHorizontalAlignment(SwingConstants.CENTER);
        copyright.setBorder(BorderFactory.createEmptyBorder(8,0,8,0));
        copyright.setOpaque(true);
        copyright.setBackground(new Color(220, 229, 241));
        add(copyright, BorderLayout.SOUTH);

        pack();
    }

    private JMenuBar jMenuBar1;
}
