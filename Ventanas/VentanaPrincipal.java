package Ventanas;

import java.awt.*;
import javax.swing.JMenuBar;
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

        JMenuItem crearMenuItem = new JMenuItem("Crear");
        JMenuItem buscarMenuItem = new JMenuItem("Buscar");
        JMenuItem modificarMenuItem = new JMenuItem("Modificar");
        JMenuItem eliminarMenuItem = new JMenuItem("Eliminar");

        
        // Agregar ActionListeners para cada opción del menú
        crearMenuItem.addActionListener(e -> {
            VentanaCrear ventanaCrear = new VentanaCrear();
            ventanaCrear.setVisible(true);
        });
        buscarMenuItem.addActionListener(e -> 
        {
            VentanaBuscar ventanaBuscar = new VentanaBuscar();
            ventanaBuscar.setVisible(true);
        });

        modificarMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Opción Modificar seleccionada"));
        eliminarMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Opción Eliminar seleccionada"));

        jMenu1.add(crearMenuItem);
        jMenu1.add(buscarMenuItem);
        jMenu1.add(modificarMenuItem);
        jMenu1.add(eliminarMenuItem);

        jMenuBar1.add(jMenu1);
        jMenuBar1.add(jMenu2);
        jMenuBar1.add(jMenu3);
        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);
        

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Ventana Principal");

        setResizable(false);

        jLabel1.setText("Ventana Principal");

        // Layout code omitted for brevity

        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
    private JMenuBar jMenuBar1;
    private JLabel jLabel1 = new JLabel("Ventana Principal");

}
