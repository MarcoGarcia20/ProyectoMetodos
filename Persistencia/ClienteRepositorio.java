package Persistencia;

import Entidades.Cliente;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class ClienteRepositorio {
    private final String ruta = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\";
    private RandomAccessFile archivo;
    private Map<String, Long> indiceDNI = new HashMap<>(); // Índice para buscar por DNI

    public ClienteRepositorio() {
        try {
            construirIndice();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Métodos para abrir y cerrar el archivo
    public void abrirArchivo(String modo) {
        try {
            archivo = new RandomAccessFile(ruta+"Clientes.dat", modo);
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
    // Métodos para agregar, buscar, eliminar, listar y actualizar clientes

    // Ingresar un cliente
    public void ingresarCliente(Cliente cliente) throws IOException {
        try {
            abrirArchivo("rw");
            // 1. Leer cabecera (primer disponible)
            archivo.seek(0);
            int primerDisponible = archivo.readInt();
            // 2. ¿Hay disponibles?
            if (primerDisponible == -1) {
                // No hay registros disponibles, agregar al final del archivo
                long registros = (archivo.length() - 4) / Cliente.LONGITUD_REGISTRO;
                cliente.posicionar(archivo, (int) registros);
                cliente.setActivo(true);
                cliente.setSiguienteDisponible(-1);
                cliente.escribir(archivo);
            } else {
                // Hay espacio disponible, reutilizarlo
                Cliente regDisp = new Cliente();
                regDisp.posicionar(archivo, primerDisponible);
                regDisp.leer(archivo);
                int siguienteDisponible = regDisp.getSiguienteDisponible();

                cliente.posicionar(archivo, primerDisponible);
                cliente.setActivo(true);
                cliente.setSiguienteDisponible(-1);
                cliente.escribir(archivo);

                // Actualizar cabecera con el nuevo primer disponible
                archivo.seek(0);
                archivo.writeInt(siguienteDisponible);
            }
            construirIndice(); // Actualizar el índice después de agregar un cliente
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el cliente: " + e.getMessage());
        }
    }

    // Búsqueda secuencial de un cliente por DNI
    public Cliente busquedaSecuencial(String dni) throws IOException {
        abrirArchivo("r");
        try {
            int numeroRegistro = (int) (archivo.length() / Cliente.LONGITUD_REGISTRO);
            for (int i = 0; i < numeroRegistro; i++) {
                Cliente cliente = new Cliente();
                cliente.posicionar(archivo, i); // Mover al registro correspondiente
                cliente.leer(archivo);
                if (cliente.isActivo() && cliente.getDni().equals(dni)) {
                    cerrarArchivo(); // Cerrar el archivo después de la búsqueda
                    return cliente; // Cliente encontrado, salir del método
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el cliente: " + e.getMessage());
        } finally {
            cerrarArchivo(); // Asegurarse de cerrar el archivo al final
        }
        return null;
    }

    // Modificar un cliente
    public void modificarCliente(Cliente cliente) throws IOException {
        try {
            abrirArchivo("rw");
            Long numeroRegistro = indiceDNI.get(cliente.getDni());
            if (numeroRegistro == null) {
                JOptionPane.showMessageDialog(null, "Cliente no encontrado.");
                return;
            }
            cliente.posicionar(archivo, numeroRegistro.intValue());
            Cliente actual = new Cliente();
            actual.leer(archivo);
            if (!actual.isActivo()) {
                JOptionPane.showMessageDialog(null, "El cliente no está activo.");
                return;
            }
            archivo.seek(archivo.getFilePointer() - Cliente.LONGITUD_REGISTRO);
            cliente.setActivo(true); // por si acaso
            cliente.setSiguienteDisponible(-1); // por si acaso
            cliente.escribir(archivo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al modificar el cliente: " + e.getMessage());
        } finally {
            cerrarArchivo();
        }
    }

    // Eliminar un cliente por DNI
    public boolean eliminarCliente(String dni) throws IOException {
        try {
            abrirArchivo("rw");
            Long numeroRegistro = indiceDNI.get(dni);
            if (numeroRegistro == null)
                return false; // Si no existe el DNI, retorna false

            Cliente cliente = new Cliente();
            cliente.posicionar(archivo, numeroRegistro.intValue()); // Mover al registro correspondiente
            cliente.leer(archivo); // Lee el cliente actual
            if (!cliente.isActivo())
                return false; // Si ya está inactivo, no hacer nada

            // Obtener el primer disponible de la cabecera
            archivo.seek(0);
            int primerDisponible = archivo.readInt();

            // Marcar como inactivo y volver a escribir el registro
            cliente.setActivo(false);
            cliente.setSiguienteDisponible(primerDisponible); // Actualizar el siguiente disponible

            cliente.posicionar(archivo, numeroRegistro.intValue());
            cliente.escribir(archivo);

            // Actualizar cabecera
            archivo.seek(0);
            archivo.writeInt(numeroRegistro.intValue());
            indiceDNI.remove(dni); // Opcional: actualizar índice
            return true; // Retorna true si se eliminó correctamente
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el cliente: " + e.getMessage());
            return false; // Retorna false si hubo un error
        } finally {
            cerrarArchivo();
        }
    }

    // Listar todos los clientes
    public List<Cliente> listarClientes() throws IOException {
        List<Cliente> clientes = new ArrayList<>();
        try {
            abrirArchivo("r");
            long registros = (archivo.length() - 4) / Cliente.LONGITUD_REGISTRO;
            for (int i = 0; i < registros; i++) {
                Cliente cliente = new Cliente();
                cliente.posicionar(archivo, i); // PASA EL NÚMERO DE REGISTRO, NO LA POSICIÓN EN BYTES
                cliente.leer(archivo);
                if (cliente.isActivo()
                        && cliente.getDni() != null
                        && !cliente.getDni().isEmpty()) {
                    clientes.add(cliente);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al listar los clientes: " + e.getMessage());
        } finally {
            cerrarArchivo();
        }
        return clientes;
    }

    // Actualizar un cliente por DNI
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
            long registros = (archivo.length() - 4) / Cliente.LONGITUD_REGISTRO;
            for (int i = 0; i < registros; i++) {
                Cliente cliente = new Cliente();
                cliente.posicionar(archivo, i); // Usa tu método, que ya suma la cabecera
                cliente.leer(archivo);
                if (cliente.isActivo()) {
                    indiceDNI.put(cliente.getDni(), (long) i);
                }
            }
            cerrarArchivo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al construir el índice: " + e.getMessage());
        }
    }

    // Mantemiento de archivos
    public void compactarPorCopia() throws IOException {
        String rutaOriginal = ruta + "Clientes.dat";
        String rutaNueva = ruta + "Clientes_temp.dat";

        try (
                RandomAccessFile original = new RandomAccessFile(rutaOriginal, "r");
                RandomAccessFile nuevo = new RandomAccessFile(rutaNueva, "rw");) {
            // 1. Escribir cabecera limpia
            nuevo.seek(0);
            nuevo.writeInt(-1);

            // 2. Copiar solo los activos
            long totalBytes = original.length();
            original.seek(4); // Salta cabecera
            while (original.getFilePointer() < totalBytes) {
                Cliente cliente = new Cliente();
                cliente.leer(original);
                if (cliente.isActivo()) {
                    cliente.escribir(nuevo);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error durante la compactación: " + e.getMessage());
            return;
        }

        // 3. Reemplazar archivos
        File archivoOriginal = new File(rutaOriginal);
        File archivoNuevo = new File(rutaNueva);
        if (archivoOriginal.delete()) {
            if (!archivoNuevo.renameTo(archivoOriginal)) {
                throw new IOException("No se pudo renombrar el archivo nuevo.");
            }
        } else {
            throw new IOException("No se pudo eliminar el archivo original.");
        }

        // 4. Reconstruir índice
        construirIndice();
    }

    // Ayuda: calcular posición física en bytes de un registro
    private long calcularPosicionRegistro(int nroRegistro) {
        // Suponiendo cabecera de 4 bytes (int) y registros de longitud fija
        return 4 + (long) nroRegistro * Cliente.LONGITUD_REGISTRO;
    }

}
