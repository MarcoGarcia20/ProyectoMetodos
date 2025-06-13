package Persistencia;

import Entidades.Cliente;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class ClienteRepositorio {
    private final String ruta = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\Clientes.dat";
    private RandomAccessFile archivo;
    private Map<String, Long> indiceDNI = new HashMap<>(); // Índice para buscar por DNI

    public ClienteRepositorio() {
        try {
            construirIndice();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Métodos para abrir y cerrar el archivo
    public void abrirArchivo(String modo) {
        try {
            archivo = new RandomAccessFile(ruta, modo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al abrir el archivo: " + e.getMessage());
        }
    }

    public void cerrarArchivo() {
        if (archivo != null) {
            try {
                archivo.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al cerrar el archivo: " + e.getMessage());
            }
        }
    }
    //Métodos para agregar, buscar, eliminar, listar y actualizar clientes
    
    //Ingresar un cliente
    public void ingresarCliente(Cliente cliente) throws IOException{
        try {
            abrirArchivo("rw");
            archivo.seek(archivo.length()); // Mover al final del archivo
            cliente.escribir(archivo);
            construirIndice(); // Actualizar el índice después de agregar un cliente
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el cliente: " + e.getMessage());
        }
    }

    //Buscar un cliente por DNI
    public Cliente buscarClientePorDNI(String dni) throws IOException{
        Long numeroRegistro = indiceDNI.get(dni);
        if (numeroRegistro == null) return null; // Si no existe el DNI, retorna null
        try {
            abrirArchivo("r");
            Cliente cliente = new Cliente();
            cliente.posicionar(archivo, numeroRegistro.intValue()); // Mover al registro correspondiente
            cliente.leer(archivo);
            archivo.close();
            if (cliente.getDni().equals(dni)) {
                return cliente; // Retorna el cliente encontrado
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el cliente: " + e.getMessage());
        }
        return null;
    }
    public void modificarCliente(Cliente cliente) throws IOException {
        try {
            abrirArchivo("rw");
            Long numeroRegistro = indiceDNI.get(cliente.getDni());
            if (numeroRegistro == null) {
                JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
                return;
            }
            archivo.seek(numeroRegistro * Cliente.LONGITUD_REGISTRO); // Mover al registro del cliente
            cliente.escribir(archivo); // Escribir el cliente actualizado
            archivo.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar el cliente: " + e.getMessage());
        }
    }

    public void eliminarCliente(String dni) throws IOException {
        try {
            abrirArchivo("rw");
            Long numeroRegistro = indiceDNI.get(dni);
            if (numeroRegistro == null) {
                JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
                return;
            }
            archivo.seek(numeroRegistro * Cliente.LONGITUD_REGISTRO); // Mover al registro del cliente
            archivo.write(new byte[Cliente.LONGITUD_REGISTRO]); // Sobrescribir con bytes vacíos
            indiceDNI.remove(dni); // Eliminar del índice
            archivo.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el cliente: " + e.getMessage());
        }
    }

    //Listar todos los clientes
    public List<Cliente> listarClientes() throws IOException {
        List<Cliente> clientes = new ArrayList<>();
        try {
            abrirArchivo("r");
            while (archivo.getFilePointer() < archivo.length()) {
                Cliente cliente = new Cliente();
                cliente.leer(archivo);
                if (cliente.getDni() != null && !cliente.getDni().isEmpty()) {
                    clientes.add(cliente);
                }
            }
            archivo.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al listar los clientes: " + e.getMessage());
        }
        return clientes;
    }

    //Actualizar un cliente por DNI
    public boolean actualizarCliente(Cliente clienteActualizado) throws IOException {
        try {
            abrirArchivo("rw");
            long posicion = -1;
            while (archivo.getFilePointer() < archivo.length()) {
                Cliente cliente = new Cliente();
                posicion = archivo.getFilePointer();
                cliente.leer(archivo);
                if (cliente.getDni().equals(clienteActualizado.getDni())) {
                    archivo.seek(posicion); // Mover al inicio del registro encontrado
                    clienteActualizado.escribir(archivo); // Escribir el cliente actualizado
                    return true; // Retorna true si se actualizó correctamente
                }
            }
            archivo.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el cliente: " + e.getMessage());
        }
        return false; // Retorna false si no se encontró el cliente

    }

    public void construirIndice() throws IOException {
        indiceDNI.clear(); // Limpiar el índice antes de construirlo
        try {
            abrirArchivo("r");
            int numeroRegistro = 0;
            while (archivo.getFilePointer() < archivo.length()) {
                // Guardar la posición inicial del registro
                long posicionInicial = archivo.getFilePointer();
                // Leer solo el DNI del registro
                byte[] bufferDNI = new byte[8];
                archivo.readFully(bufferDNI);
                String dni = new String(bufferDNI, "ISO-8859-1").trim();
                //Almacenar en el índice
                indiceDNI.put(dni, (long) numeroRegistro);
                archivo.seek(posicionInicial + Cliente.LONGITUD_REGISTRO); // Mover el puntero al siguiente registro
                numeroRegistro++;
            }
            archivo.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al construir el índice: " + e.getMessage());
        }
    }

    
}