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

    public IndiceClienteArchivo(String nombreArchivo) throws IOException {
        this.nombreArchivo = nombreArchivo;
        this.indiceArchivo = new RandomAccessFile(nombreArchivo, "rw");
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
        try {
            indiceArchivo.seek(0); // Asegúrate de empezar desde el principio
            long numRegs = indiceArchivo.length() / REGISTRO_SIZE;
            for (int i = 0; i < numRegs; i++) {
                IndiceCliente idx = leerIndice(indiceArchivo);
                indices.add(idx);
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
            indiceArchivo.setLength(0); // Borra todo el archivo
            // 2. Para cada nodo del arreglo índice:
            for (IndiceCliente idx : arregloIndices) {
                escribirIndice(indiceArchivo, idx);
            }
            // 3. El archivo se cierra al finalizar el sistema
        } catch (IOException e) {
            System.err.println("Error reescribiendo el archivo de índices: " + e.getMessage());
        }

        // 4. Actualizar bandera en cabecera de archivo de datos
        actualizarBanderaIndice(archivoDatos, false);
    }

    public void reconstruirIndice(String archivoDatos) {
        ArrayList<IndiceCliente> arregloIndices = new ArrayList<>();
        try {
            if (indiceArchivo.length() == 0) {
                System.out.println("El archivo de datos está vacío. No se puede reconstruir el índice.");
                return;
            }
            long numRegistros = indiceArchivo.length() / Cliente.LONGITUD_REGISTRO;

            for (int i = 0; i < numRegistros; i++) {
                Cliente cliente = new Cliente();
                cliente.posicionar(indiceArchivo, i);
                cliente.leer(indiceArchivo);

                if (cliente.isActivo()) {
                    IndiceCliente indice = new IndiceCliente(cliente.getDni(), i * Cliente.LONGITUD_REGISTRO);
                    arregloIndices.add(indice);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al reconstruir índice: " + e.getMessage());
        }
        // Ordenar arreglo de índices por DNI
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
            indiceArchivo.seek(0); // Bandera en el primer byte
            indiceArchivo.writeBoolean(modificado);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
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

    public int busquedaBinariaIndice(ArrayList<IndiceCliente> indices,
            String dniBuscado) {
        int izquierda = 0, derecha = indices.size() - 1;
        while (izquierda <= derecha) {
            int medio = (izquierda + derecha) / 2;
            String dniMedio = indices.get(medio).getDni().trim();
            int cmp = dniBuscado.trim().compareTo(dniMedio);
            if (cmp == 0) {
                return medio; // Retorna la posición en el arreglo de índices
            } else if (cmp < 0) {
                derecha = medio - 1;
            } else {
                izquierda = medio + 1;
            }
        }
        return -1; // No encontrado
    }

    // Uso completo del procedimiento:
    public Cliente buscarClientePorIndice(ArrayList<IndiceCliente> indices,
            String dniBuscado, RandomAccessFile archivoDatos, 
            int TAM_REGISTRO) throws IOException {
        int pos = busquedaBinariaIndice(indices, dniBuscado);
        if (pos != -1) {
            long offset = indices.get(pos).getReferencia();
            archivoDatos.seek(offset);
            Cliente cliente = new Cliente();
            cliente.leer(archivoDatos);
            return cliente;
        }
        return null; // No encontrado
    }
}
