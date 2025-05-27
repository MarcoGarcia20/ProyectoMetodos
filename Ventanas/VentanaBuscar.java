package Ventanas;
import Entidades.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VentanaBuscar extends JFrame {
    // Atributos de la Ventana Buscar
    private final String ruta = "C:\\\\Users\\\\MARCO\\\\Metodos\\\\Sistema\\\\Archivos\\\\Prueba.txt";
    private static final int longfija = 8 + 2 + 20 + 20 + 8 + 9;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField campoDni;
    private JButton botonBuscar;

    private JLabel resultado;
    private HashSet<String> registros;

    public VentanaBuscar() {
        setTitle("Buscar Cliente");
        setSize(600, 600);
        setLayout(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        setLocationRelativeTo(null);

        //Creando y configurando la tabla
        String [] columnas = {"DNI", "Edad","Nombre", "Correo", "Fecha de Inicio", "Celular"};
        modelo = new DefaultTableModel(columnas,0);
        tabla = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(20, 20, 500, 150);
        add(scroll);
        // Configuración de la tabla
        // En el constructor, después de crear la tabla
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        configurarAnchoColumnas();

        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setDefaultEditor(Object.class, null); // Hacer la tabla no editable
        tabla.setSelectionBackground(new java.awt.Color(173, 216, 230)); // Color de fondo al seleccionar una fila
        tabla.setSelectionForeground(new java.awt.Color(0, 0, 0)); // Color del texto al seleccionar una fila
        tabla.setShowGrid(true); // Mostrar líneas de cuadrícula
        tabla.setGridColor(new java.awt.Color(200, 200, 200)); // Color de las líneas de cuadrícula
        tabla.setRowSelectionAllowed(true); // Permitir selección de filas
        tabla.setColumnSelectionAllowed(true); // Permitir selección de columnas
        tabla.setCellSelectionEnabled(false); // No permitir selección de celdas individuales
        tabla.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14)); // Fuente de la tabla
        tabla.setForeground(new java.awt.Color(0, 0, 0)); // Color del texto de la tabla
        tabla.setBackground(new java.awt.Color(255, 255, 255)); // Color de fondo de la tabla
        tabla.setBorder(BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200))); // Borde de la tabla
        tabla.setIntercellSpacing(new java.awt.Dimension(1, 1)); // Espacio entre celdas
        



        //Componentes de búsqueda
        campoDni = new JTextField();
        campoDni.setBounds(20, 200, 200, 25);
        add(campoDni);


        botonBuscar = new JButton("Buscar");
        botonBuscar.setBounds(230, 220, 90, 25);
        add(botonBuscar);


        resultado = new JLabel("Resultado");
        resultado.setBounds(20, 300, 300, 25);
        add(resultado);

        registros = new HashSet<>();
        cargarDesdeArchivo();
        
        botonBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String numero = campoDni.getText().trim();
                buscarCliente(e);
                if (registros.contains(numero)) {
                    
                    resultado.setText("✔ DNI registrado.");
                } else {
                    resultado.setText("✘ DNI NO encontrado.");
                }
            }
        });

        setVisible(true);
    }

    private void buscarCliente(ActionEvent e) {
        String dniBuscado = campoDni.getText().trim();
    for (int i = 0; i < modelo.getRowCount(); i++) {
        if (dniBuscado.equals(modelo.getValueAt(i, 0).toString())) {
            tabla.setRowSelectionInterval(i, i);
            //mostrarDetallesCliente(i);
            return;
        }
    }
    JOptionPane.showMessageDialog(this, "Cliente no encontrado");
    }

    private void cargarDesdeArchivo() {
        RandomAccessFile archivo = null;
        String ruta = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\Sistema.dat";
        try {
            archivo = new RandomAccessFile(ruta, "r");
            modelo.setRowCount(0); // Limpiar tabla
            registros.clear();
        
        while (archivo.getFilePointer() < archivo.length()) {
            Cliente c = new Cliente();
            long pos = archivo.getFilePointer();
            c.leer(archivo);
            
            // Verificar lectura correcta
            if (c.getDni() == null || c.getDni().isEmpty()) break;
            
            modelo.addRow(new Object[]{
                c.getDni(),
                c.getEdad(),
                c.getNombre(),
                c.getCorreo(),
                c.getIniSus(),
                c.getCelular()
            });
            registros.add(c.getDni());
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + e.getMessage());
        } finally {
            try {
                if (archivo != null) archivo.close();
            } catch (IOException ex) {
                // Ignorar
            }
        }
    }

    private void configurarAnchoColumnas() {
    // Obtener el modelo de columnas
    TableColumnModel columnModel = tabla.getColumnModel();
    
    // Establecer anchos personalizados (en píxeles)
    columnModel.getColumn(0).setPreferredWidth(80);  // DNI (8 caracteres)
    columnModel.getColumn(1).setPreferredWidth(50);   // Edad (2 dígitos)
    columnModel.getColumn(2).setPreferredWidth(120);  // Nombre (10 caracteres)
    columnModel.getColumn(3).setPreferredWidth(250);  // Correo (20 caracteres)
    columnModel.getColumn(4).setPreferredWidth(100);  // Fecha (8 caracteres)
    columnModel.getColumn(5).setPreferredWidth(90);   // Celular (9 dígitos)
    
    // Opcional: Establecer anchos mínimos/máximos
    columnModel.getColumn(0).setMinWidth(70);
    columnModel.getColumn(4).setMinWidth(200);
}
}

//Metodo sobre todo el archivo, recorrido secuencial

