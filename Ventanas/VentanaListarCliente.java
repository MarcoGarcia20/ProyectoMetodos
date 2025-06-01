package Ventanas;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Entidades.Cliente;

public class VentanaListarCliente extends JFrame implements Datos {
    private final String ruta = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\Clientes.dat";
    private RandomAccessFile archivo = null;

    private JTable tabla;
    private DefaultTableModel modelo;

    private JButton botonListar;

    private HashSet<String> registros;

    public VentanaListarCliente() {
        try {
            archivo = new RandomAccessFile(ruta, "r");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al abrir el archivo: " + e.getMessage());
        }

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
        registros = new HashSet<>();

        botonListar.addActionListener(e -> cargarDesdeArchivo(archivo));
    }

    @Override
    public void cargarDesdeArchivo(RandomAccessFile archivo) {
        try {
            archivo = new RandomAccessFile(ruta, "r");
            modelo.setRowCount(0); // Limpiar tabla
            registros.clear();
        
        while (archivo.getFilePointer() < archivo.length()) {
            Cliente c = new Cliente();
            //long pos = archivo.getFilePointer();
            c.leer(archivo);
            
            // Verificar lectura correcta
            if (!c.getDni().isEmpty()) {
                modelo.addRow(new Object[]{
                c.getDni(),
                c.getEdad(),
                c.getNombre(),
                c.getCorreo(),
                c.getIniSus(),
                c.getCelular()
            });
            }            
            registros.add(c.getDni());
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes: " + e.getMessage());
        } finally {
            try {
                if (archivo != null) archivo.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al cerrar el archivo: " + ex.getMessage());
            }
        }
    }

    @Override
    public void configurarTabla(JTable tabla) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'configurarTabla'");
    }
}
