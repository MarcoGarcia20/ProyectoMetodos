package src.Persistencia;

import src.Entidades.Indices.IndiceCliente;
import java.io.*;
import java.util.ArrayList;

public class IndiceClienteArchivo {
    private String nombreArchivo;
    private ArrayList<IndiceCliente> indices;

    public IndiceClienteArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
        this.indices = new ArrayList<>();
        cargarDesdeArchivo();
    }

    // Cargar los índices en memoria desde archivo
    @SuppressWarnings("unchecked")
    public void cargarDesdeArchivo() {
        File file = new File(nombreArchivo);
        if (!file.exists()) {
            indices = new ArrayList<>();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            indices = (ArrayList<IndiceCliente>) ois.readObject();
        } catch (Exception e) {
            indices = new ArrayList<>();
            System.err.println("No se pudo cargar el archivo de índices: " + e.getMessage());
        }
    }

    // Guardar los índices en archivo
    public void guardarEnArchivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(indices);
        } catch (IOException e) {
            System.err.println("No se pudo guardar el archivo de índices: " + e.getMessage());
        }
    }

    // Agregar un nuevo índice (y sincronizar)
    public void agregarIndice(IndiceCliente indice) {
        indices.add(indice);
        guardarEnArchivo();
    }

    // Eliminar un índice por clave
    public void eliminarIndicePorDni(String dni) {
        indices.removeIf(ind -> ind.getDni().equals(dni));
        guardarEnArchivo();
    }

    // Buscar referencia por DNI
    public Long buscarReferenciaPorDni(String dni) {
        for (IndiceCliente ind : indices) {
            if (ind.getDni().equals(dni)) {
                return ind.getReferencia();
            }
        }
        return null; // no encontrado
    }

    // Actualizar referencia (por ejemplo, si cambió la posición en el archivo de datos)
    public void actualizarReferencia(String dni, long nuevaReferencia) {
        for (IndiceCliente ind : indices) {
            if (ind.getDni().equals(dni)) {
                ind.setReferencia(nuevaReferencia);
                guardarEnArchivo();
                break;
            }
        }
    }

    // Obtener todos los índices
    public ArrayList<IndiceCliente> getIndices() {
        return indices;
    }
}
