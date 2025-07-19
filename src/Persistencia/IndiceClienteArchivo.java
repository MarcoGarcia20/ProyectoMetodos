package src.Persistencia;

import src.Entidades.Cliente;
import src.Entidades.Indices.IndiceCliente;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class IndiceClienteArchivo {
    private String nombreArchivo = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\IndiceClientes.ind"; // Ruta del
                                                                                                       // archivo
                                                                                                       // decíndices
    RandomAccessFile indiceArchivo = null;
    public static final int DNI_LENGTH = 8; // Tamaño fijo para DNI
    public static final int REGISTRO_SIZE = DNI_LENGTH + 8; // 8 bytes para DNI + 8 bytes para referencia (long)
    Cliente cliente;

    public IndiceClienteArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public void abrirArchivoDeIndices() {
        try {
            indiceArchivo = new RandomAccessFile(nombreArchivo, "rw");
        } catch (IOException e) {
            System.err.println("No se pudo abrir el archivo de índices: " + e.getMessage());
        }
    }

    // Guardar todos los índices al archivo .ind (sobrescribe)
    public void guardarTodos(ArrayList<IndiceCliente> indices) {
        try {
            abrirArchivoDeIndices();
            indiceArchivo.setLength(0); // Limpiar archivo
            for (IndiceCliente idx : indices) {
                escribirIndice(indiceArchivo, idx);
            }
        } catch (IOException e) {
            System.err.println("No se pudo guardar el archivo de índices: " + e.getMessage());
        }
    }

    // Leer todos los índices desde archivo .ind
    public ArrayList<IndiceCliente> cargarIndiceAMemoria() {
        ArrayList<IndiceCliente> indices = new ArrayList<>();
        File file = new File(nombreArchivo);
        if (!file.exists())
            return indices;
        try (RandomAccessFile raf = new RandomAccessFile(nombreArchivo, "r")) {
            long numRegs = raf.length() / REGISTRO_SIZE; // Determinar tamaño del índice
            for (int i = 0; i < numRegs; i++) {
                IndiceCliente idx = leerIndice(raf); // Leer registro de índice
                indices.add(idx); // Cargar registro en arreglo de nodos
            }
        } catch (IOException e) {
            System.err.println("Error al cargar índice: " + e.getMessage());
        }
        return indices;
    }

    // Búsqueda binaria en el archivo de índices (asumiendo que está ordenado por
    // DNI)
    public Long buscarReferenciaPorDni(String dni) {
        try (RandomAccessFile raf = new RandomAccessFile(nombreArchivo, "r")) {
            long left = 0;
            long right = raf.length() / REGISTRO_SIZE - 1;
            while (left <= right) {
                long mid = (left + right) / 2;
                raf.seek(mid * REGISTRO_SIZE);
                IndiceCliente idx = leerIndice(raf);
                int cmp = dni.compareTo(idx.getDni());
                if (cmp == 0) {
                    return idx.getReferencia();
                } else if (cmp < 0) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        } catch (IOException e) {
            System.err.println("Error en búsqueda binaria de índice: " + e.getMessage());
        }
        return null;
    }

    // Método de reescritura del índice (sobrescribe archivo .ind)
    public void reescribirIndice(ArrayList<IndiceCliente> arregloIndices, String archivoDatos) {
        // 1. Abrir archivo índice en modo OUTPUT (reescritura)
        try {
            abrirArchivoDeIndices();
            indiceArchivo.setLength(0); // Borra todo el archivo
            // 2. Para cada nodo del arreglo índice:
            for (IndiceCliente idx : arregloIndices) {
                escribirIndice(indiceArchivo, idx);
            }
            // 3. El archivo se cierra automáticamente por try-with-resources
        } catch (IOException e) {
            System.err.println("Error reescribiendo el archivo de índices: " + e.getMessage());
        }

        // 4. Actualizar bandera en cabecera de archivo de datos
        actualizarBanderaIndice(archivoDatos, false);
    }

    public void reconstruirIndice(String archivoDatos) {
        ArrayList<IndiceCliente> arregloIndices = new ArrayList<>();

        try (RandomAccessFile rafDatos = new RandomAccessFile(archivoDatos, "r")) {
            if (rafDatos.length() == 0) {
                System.out.println("El archivo de datos está vacío. No se puede reconstruir el índice.");
                return;
            }
            long numRegistros = rafDatos.length() / Cliente.LONGITUD_REGISTRO;

            for (int i = 0; i < numRegistros; i++) {
                Cliente cliente = new Cliente();
                cliente.posicionar(rafDatos, i); // Si tienes este método para posicionar en el registro i
                cliente.leer(rafDatos); // Si tienes este método para leer todo el registro

                if (cliente.isActivo()) { // Usa el método correcto para verificar si está activo
                    IndiceCliente indice = new IndiceCliente(cliente.getDni(), i * Cliente.LONGITUD_REGISTRO);
                    arregloIndices.add(indice);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al reconstruir índice: " + e.getMessage());
        }

        // Ordenar arreglo de índices por DNI (puedes ajustar el comparador si el DNI es
        // numérico)
        arregloIndices.sort((a, b) -> a.getDni().compareTo(b.getDni()));

        // Reescribir archivo de índices (sobrescribe el archivo actual)
        this.reescribirIndice(arregloIndices, archivoDatos);
    }

    // Método utilitario para escribir un índice
    private void escribirIndice(RandomAccessFile raf, IndiceCliente idx) throws IOException {
        String dniFijo = String.format("%-" + DNI_LENGTH + "s", idx.getDni());
        raf.write(dniFijo.getBytes());
        raf.writeLong(idx.getReferencia());
    }

    private IndiceCliente leerIndice(RandomAccessFile raf) throws IOException {
        byte[] dniBytes = new byte[DNI_LENGTH];
        raf.readFully(dniBytes);
        String dni = new String(dniBytes).trim();
        long referencia = raf.readLong();
        return new IndiceCliente(dni, referencia);
    }

    // Eliminar un índice por DNI (reescribe el archivo sin ese índice)
    public void eliminarIndicePorDni(String dni) {
        ArrayList<IndiceCliente> indices = cargarIndiceAMemoria();
        indices.removeIf(idx -> idx.getDni().equals(dni));
        guardarTodos(indices);
    }

    // Agregar un nuevo índice (debes mantener el archivo ordenado por DNI)
    public void agregarIndice(IndiceCliente indice) {
        ArrayList<IndiceCliente> indices = cargarIndiceAMemoria();
        indices.add(indice);
        indices.sort((a, b) -> a.getDni().compareTo(b.getDni())); // Ordenar por DNI
        guardarTodos(indices);
    }

    // Actualizar referencia
    public void actualizarReferencia(String dni, long nuevaReferencia) {
        ArrayList<IndiceCliente> indices = cargarIndiceAMemoria();
        for (IndiceCliente idx : indices) {
            if (idx.getDni().equals(dni)) {
                idx.setReferencia(nuevaReferencia);
                break;
            }
        }
        guardarTodos(indices);
    }

    private void actualizarBanderaIndice(String archivoDatos, boolean modificado) {
        File file = new File(archivoDatos);
        if (!file.exists())
            return;
        try {
            abrirArchivoDeIndices();
            indiceArchivo.seek(0); // Bandera en el primer byte
            indiceArchivo.writeBoolean(modificado);
        } catch (IOException e) {
            System.err.println("No se pudo actualizar la bandera del índice: " + e.getMessage());
        }
    }

    public boolean isBanderaIndiceActiva(String archivoDatos) {
        File file = new File(archivoDatos);
        if (!file.exists())
            return false;
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(0); // Suponiendo que la bandera está en el primer byte
            return raf.readBoolean();
        } catch (IOException e) {
            System.err.println("No se pudo leer la bandera del índice: " + e.getMessage());
            return false;
        }
    }
}
