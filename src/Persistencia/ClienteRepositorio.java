package src.Persistencia;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.HashMap;

import javax.swing.JOptionPane;

import src.Entidades.Cliente;

public class ClienteRepositorio {
    private final String ruta = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\";
    private RandomAccessFile archivo;
    private Map<String, Long> indiceDNI = new HashMap<>(); // Índice para buscar por DNI

    private MetodoOrdenamiento ultimoMetodoOrdenado = MetodoOrdenamiento.NINGUNO;

    public ClienteRepositorio() {
        try {
            construirIndice();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MetodoOrdenamiento getUltimoMetodoOrdenado() {
        return ultimoMetodoOrdenado;
    }

    /*
     * 1era Entrega Métodos de Acceso
     */

    // Métodos para abrir y cerrar el archivo
    public void abrirArchivo(String modo) {
        try {
            archivo = new RandomAccessFile(ruta + "ClientesPrueba.dat", modo);
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

    public void crearArchivo(int cantidad) throws IOException {
        File file = new File(ruta + "ClientesPrueba.dat");
        if (file.exists()) {
            file.delete(); // Eliminar si ya existe
        }
        RandomAccessFile archivo = new RandomAccessFile(file, "rw");
        try {
            // 1. Grabar registro cabecera (ejemplo: -1 para desordenado)
            archivo.writeInt(-1);
            // 2. Crear clientes aleatorios después de la cabecera
            Cliente cliente = new Cliente();
            cliente.crearClientesAleatorios(archivo, cantidad);
            System.out.println("Archivo de clientes creado con " + cantidad + " registros.");
        } finally {
            archivo.close();
        }
        construirIndice(); // Construir el índice después de crear el archivo
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

            // Implementación de eliminación en lista de disponibles
            // 1. Obtener el primer disponible de la cabecera
            archivo.seek(0);
            int primerDisponible = archivo.readInt();

            // 2. Marcar como inactivo y volver a escribir el registro
            cliente.setActivo(false);
            cliente.setSiguienteDisponible(primerDisponible); // Actualizar el siguiente disponible

            cliente.posicionar(archivo, numeroRegistro.intValue());
            cliente.escribir(archivo);

            // 3. Actualizar cabecera: nuevo primer disponible es el registro recién
            // eliminado
            archivo.seek(0);
            archivo.writeInt(numeroRegistro.intValue());
            indiceDNI.remove(dni); // Opcional: actualizar índice
            return true; // Retorna true si se eliminó correctamente
        } catch (Exception e) {
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

    // Construir el índice de clientes por DNI
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

    /*
     * 2da Entrega Métodos de Acceso
     */
    // Mantemiento de archivos
    public void compactarPorCopia() throws IOException {
        String rutaOriginal = ruta + "ClientesPrueba.dat";
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
                System.out.println("Leyendo cliente: " + cliente.getDni() + " Activo: " + cliente.isActivo());
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

    // Ordenar clientes por inserción (DNI)
    public void ordenarPorInsercion(List<Cliente> listaClientes) {
        for (int i = 1; i < listaClientes.size(); i++) {
            Cliente x = listaClientes.get(i); // Elemento actual a insertar
            int j = i - 1;

            // Convertimos el DNI a número para comparación
            long dniX = Long.parseLong(x.getDni().trim());

            // Mover elementos mayores que x hacia la derecha
            while (j >= 0 && dniX < Long.parseLong(listaClientes.get(j).getDni().trim())) {
                listaClientes.set(j + 1, listaClientes.get(j)); // Desplazar a la derecha
                j--;
            }

            listaClientes.set(j + 1, x); // Insertar en la posición correcta
        }
    }

    // Busca en la lista de clientes (ya ordenada por DNI)
    public Cliente busquedaBinariaEnLista(List<Cliente> lista, String dniBuscado) {
        long dniBuscadoNum = Long.parseLong(dniBuscado.trim());
        int a = 0, b = lista.size() - 1;
        while (a <= b) {
            int m = (a + b) / 2;
            long dniActual = Long.parseLong(lista.get(m).getDni().trim());
            if (dniActual == dniBuscadoNum) {
                return lista.get(m);
            } else if (dniBuscadoNum < dniActual) {
                b = m - 1;
            } else {
                a = m + 1;
            }
        }
        return null;
    }

    public Cliente busquedaBinariaPorRAM(String dniBuscado) throws IOException {
        ordenamientoClasificacionEnRAM(); // Ordena físicamente
        List<Cliente> lista = listarClientes(); // Carga lista ordenada
        return busquedaBinariaEnLista(lista, dniBuscado);
    }

    public Cliente busquedaBinariaPorNodos(String dniBuscado) throws IOException {
        ordenarPorNodos(); // Ordena físicamente por nodos
        List<Cliente> lista = listarClientes(); // Carga lista ordenada
        return busquedaBinariaEnLista(lista, dniBuscado);
    }

    public Cliente busquedaBinariaPorIndireccion(String dniBuscado) throws IOException {
        ordenarPorIndireccion(); // Ordena físicamente por indireccion
        List<Cliente> lista = listarClientes(); // Carga lista ordenada
        return busquedaBinariaEnLista(lista, dniBuscado);
    }

    public enum MetodoOrdenamiento {
        RAM,
        NODOS,
        INDIRECCION,
        NINGUNO
    }

    public void ordenarPorNodos() throws IOException {
        abrirArchivo("rw");
        try {
            // 1. Obtener la cantidad de registros
            long n = (archivo.length() - 4) / Cliente.LONGITUD_REGISTRO;
            if (n <= 1)
                return;
            // 2. Declarar un arreglo de registros (clientes) de ese tamaño
            Cliente[] registros = new Cliente[(int) n];
            // 3. Declarar un arreglo de nodos de ese tamaño
            class Nodo {
                String clave; // DNI
                int ref; // índice en el arreglo de registros
            }
            Nodo[] nodos = new Nodo[(int) n];
            // 4. Cargar registros en el arreglo de registros y crear los nodos
            for (int i = 0; i < n; i++) {
                Cliente c = new Cliente();
                c.posicionar(archivo, i);
                c.leer(archivo);
                registros[i] = c;

                nodos[i] = new Nodo();
                nodos[i].clave = (c.getDni() == null) ? "" : c.getDni().trim();
                nodos[i].ref = i;
            }
            // 5. Ordenar el arreglo de nodos (por inserción, según algoritmo original)
            for (int i = 1; i < nodos.length; i++) {
                Nodo x = nodos[i];
                int j = i - 1;
                while (j >= 0 && Long.parseLong(x.clave.isEmpty() ? "0" : x.clave) < Long
                        .parseLong(nodos[j].clave.isEmpty() ? "0" : nodos[j].clave)) {
                    nodos[j + 1] = nodos[j];
                    j--;
                }
                nodos[j + 1] = x;
            }
            // 6. Guardar registros en el archivo según orden de nodos (archivo temporal)
            File archivoTemp = new File("ClientesOrdenados.dat");
            RandomAccessFile rafTemp = new RandomAccessFile(archivoTemp, "rw");
            rafTemp.writeInt(1); // Bandera de ordenado en cabecera
            for (int i = 0; i < nodos.length; i++) {
                registros[nodos[i].ref].escribir(rafTemp);
            }
            rafTemp.close();
            // 7. Reemplazar el archivo original por el temporal
            archivo.close();
            File archivoOriginal = new File(ruta + "ClientesPrueba.dat"); // nombreArchivo: tu variable de ruta
            archivoOriginal.delete();
            archivoTemp.renameTo(archivoOriginal);
            // 8. (Opcional) Reabrir el archivo para uso posterior
            abrirArchivo("rw");

            ultimoMetodoOrdenado = MetodoOrdenamiento.NODOS;
        } finally {
            cerrarArchivo();
        }
    }

    public void ordenarPorIndireccion() throws IOException {
        abrirArchivo("rw");
        try {
            long n = (archivo.length() - 4) / Cliente.LONGITUD_REGISTRO;
            if (n <= 1)
                return;
            // 1. Cargar todos los registros
            Cliente[] registros = new Cliente[(int) n];
            String[] claves = new String[(int) n];
            int[] indices = new int[(int) n];
            for (int i = 0; i < n; i++) {
                Cliente c = new Cliente();
                c.posicionar(archivo, i);
                c.leer(archivo);
                registros[i] = c;
                claves[i] = c.getDni() == null ? "" : c.getDni().trim();
                indices[i] = i;
            }
            // 2. Ordenar el arreglo de índices usando claves
            // Ejemplo: por inserción (puedes usar cualquier algoritmo)
            for (int i = 1; i < indices.length; i++) {
                int x = indices[i];
                String claveX = claves[x];
                int j = i - 1;
                while (j >= 0 && Long.parseLong(claves[indices[j]].isEmpty() ? "0" : claves[indices[j]]) > Long
                        .parseLong(claveX.isEmpty() ? "0" : claveX)) {
                    indices[j + 1] = indices[j];
                    j--;
                }
                indices[j + 1] = x;
            }
            // 3. Grabar en archivo temporal según el orden de indices
            File archivoTemp = new File("ClientesOrdenadosIndireccion.dat");
            RandomAccessFile rafTemp = new RandomAccessFile(archivoTemp, "rw");
            rafTemp.writeInt(1); // bandera de ordenado (opcional)
            for (int i = 0; i < indices.length; i++) {
                registros[indices[i]].escribir(rafTemp);
            }
            rafTemp.close();
            // 4. Reemplaza el archivo original por el temporal
            archivo.close();
            File archivoOriginal = new File(ruta + "Clientes.dat");
            archivoOriginal.delete();
            archivoTemp.renameTo(archivoOriginal);

            abrirArchivo("rw");

            ultimoMetodoOrdenado = MetodoOrdenamiento.INDIRECCION;
        } finally {
            cerrarArchivo();
        }
    }

    // Ayuda: calcular posición física en bytes de un registro
    private long calcularPosicionRegistro(int nroRegistro) {
        // Suponiendo cabecera de 4 bytes (int) y registros de longitud fija
        return 4 + (long) nroRegistro * Cliente.LONGITUD_REGISTRO;
    }

    // Posicionar en el lugar adecuado para inserción un registro
    private int posicionarEnElLugarAdecuadoInsercion() throws IOException {
        // Leer cabecera (pt_cab)
        archivo.seek(0);
        int pt_cab = archivo.readInt();
        if (pt_cab == -1) {
            // No hay disponibles
            return -1;
        }
        // Tomar el primer disponible como posición de inserción
        Cliente regDisp = new Cliente();
        regDisp.posicionar(archivo, pt_cab);
        regDisp.leer(archivo);
        int nuevoCab = regDisp.getSiguienteDisponible();
        // Actualizar cabecera (ahora la cabeza es el siguiente disponible)
        archivo.seek(0);
        archivo.writeInt(nuevoCab);

        return pt_cab;
    }

    // Posicionar en el lugar adecuado para eliminar un registro
    private void posicionarEnElLugarAdecuadoEliminacion(int nrrEliminado) throws IOException {
        // Leer cabecera (pt_cab)
        archivo.seek(0);
        int pt_cab = archivo.readInt();
        // Caso 1: Lista vacía o el eliminado es el menor NRR
        if (pt_cab == -1 || nrrEliminado < pt_cab) {
            // El registro eliminado será la nueva cabeza
            Cliente eliminado = new Cliente();
            eliminado.posicionar(archivo, nrrEliminado);
            eliminado.leer(archivo);
            eliminado.setActivo(false);
            eliminado.setSiguienteDisponible(pt_cab);
            eliminado.posicionar(archivo, nrrEliminado);
            eliminado.escribir(archivo);
            // Actualizar cabecera
            archivo.seek(0);
            archivo.writeInt(nrrEliminado);
        } else {
            // Buscar el lugar adecuado en la lista
            int anterior = pt_cab;
            int actual;
            Cliente reg = new Cliente();
            reg.posicionar(archivo, anterior);
            reg.leer(archivo);
            actual = reg.getSiguienteDisponible();

            while (actual != -1 && actual < nrrEliminado) {
                anterior = actual;
                reg.posicionar(archivo, anterior);
                reg.leer(archivo);
                actual = reg.getSiguienteDisponible();
            }
            // Insertar el eliminado entre 'anterior' y 'actual'
            Cliente eliminado = new Cliente();
            eliminado.posicionar(archivo, nrrEliminado);
            eliminado.leer(archivo);
            eliminado.setActivo(false);
            eliminado.setSiguienteDisponible(actual);
            eliminado.posicionar(archivo, nrrEliminado);
            eliminado.escribir(archivo);
            // Actualizar el anterior para que apunte al eliminado
            reg.posicionar(archivo, anterior);
            reg.leer(archivo);
            reg.setSiguienteDisponible(nrrEliminado);
            reg.posicionar(archivo, anterior);
            reg.escribir(archivo);
        }
    }

    // Insertar un cliente en la lista de disponibles
    public boolean ingresarClienteListaDeDisponibles(Cliente cliente) throws IOException {
        try {
            abrirArchivo("rw");
            // Verifica si la clave ya existe
            if (indiceDNI.containsKey(cliente.getDni())) {
                return false;
            }

            int nrr = posicionarEnElLugarAdecuadoInsercion();

            if (nrr == -1) {
                // Insertar al final
                long registros = (archivo.length() - 4) / Cliente.LONGITUD_REGISTRO;
                cliente.posicionar(archivo, (int) registros);
                cliente.setActivo(true);
                cliente.setSiguienteDisponible(-1);
                cliente.escribir(archivo);
            } else {
                // Reutilizar espacio disponible
                cliente.posicionar(archivo, nrr);
                cliente.setActivo(true);
                cliente.setSiguienteDisponible(-1);
                cliente.escribir(archivo);
            }
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al guardar el cliente: " + e.getMessage());
            return false;
        } finally {
            cerrarArchivo();
        }
    }

    // Eliminar un cliente por DNI
    public boolean listaDeDisponiblesEliminarCliente(String dni) throws IOException {
        try {
            abrirArchivo("rw"); // asegúrate de abrir en modo escritura
            Long numeroRegistro = indiceDNI.get(dni);
            if (numeroRegistro == null)
                return false; // Si no existe el DNI, retorna false

            int nrrEliminado = numeroRegistro.intValue();

            Cliente cliente = new Cliente();
            cliente.posicionar(archivo, nrrEliminado); // Mover al registro correspondiente
            cliente.leer(archivo); // Lee el cliente actual
            if (!cliente.isActivo())
                return false; // Si ya está inactivo, no hacer nada

            // Llama al método que posiciona en el lugar adecuado
            posicionarEnElLugarAdecuadoEliminacion(nrrEliminado);

            indiceDNI.remove(dni); // Opcional: actualizar índice
            return true; // Retorna true si se eliminó correctamente
        } catch (Exception e) {
            return false;
        } finally {
            cerrarArchivo();
        }
    }

    public void compactacionPorCopia() throws IOException {
        String rutaOriginal = ruta + "ClientesPrueba.dat";
        String rutaNueva = ruta + "Clientes_temp.dat";
        // 1. Abrir archivos
        RandomAccessFile original = new RandomAccessFile(rutaOriginal, "r");
        RandomAccessFile nuevo = new RandomAccessFile(rutaNueva, "rw");
        try {
            // 2. Leer y copiar cabecera
            original.seek(0);
            int cabecera = original.readInt(); // Puedes tener más info si tu cabecera es más grande
            nuevo.seek(0);
            nuevo.writeInt(cabecera); // Copia cabecera original
            // Variable para actualizar la cabecera si es necesario
            int registrosActivos = 0;
            // 3. Leer primer registro
            original.seek(4); // asumiendo cabecera de 4 bytes
            long totalBytes = original.length();
            while (original.getFilePointer() < totalBytes) {
                Cliente cliente = new Cliente();
                cliente.leer(original);
                // 4. Si activo, escribe en archivo nuevo
                if (cliente.isActivo()) {
                    cliente.escribir(nuevo);
                    registrosActivos++;
                }
                // 5. leer siguiente registro
            }
            // 6. Actualizar cabecera en archivo nuevo si lo necesitas
            nuevo.seek(0);
            nuevo.writeInt(registrosActivos);
        } finally {
            // 7. Cerrar archivos
            original.close();
            nuevo.close();
        }
        // 8. Eliminar archivo original y 9. Renombrar nuevo
        File archivoOriginal = new File(rutaOriginal);
        File archivoNuevo = new File(rutaNueva);
        if (archivoOriginal.delete()) {
            if (!archivoNuevo.renameTo(archivoOriginal)) {
                throw new IOException("No se pudo renombrar el archivo nuevo.");
            }
        } else {
            throw new IOException("No se pudo eliminar el archivo original.");
        }
    }

    public void ordenamientoClasificacionEnRAM() throws IOException {
        // 1. Abrir el archivo en modo lectura y escritura
        abrirArchivo("rw");
        try {
            // 2. Obtener la cantidad de registros (suponiendo cabecera de 4 bytes)
            long n = (archivo.length() - 4) / Cliente.LONGITUD_REGISTRO;
            if (n <= 1)
                return; // No necesita ordenarse
            // 3. Declarar un arreglo de ese tamaño
            List<Cliente> listaClientes = new ArrayList<>();
            // 4. Cargar registros en el arreglo
            for (int i = 0; i < n; i++) {
                Cliente c = new Cliente();
                c.posicionar(archivo, i);
                c.leer(archivo);
                listaClientes.add(c);
            }
            // 5. Ordenar el arreglo (Ordenamiento por Inserción, por DNI)
            ordenarPorInsercion(listaClientes);
            // 6. Guardar el arreglo ordenado en el archivo (sobrescribiendo)
            archivo.seek(4); // Salta cabecera, empieza a escribir registros
            archivo.seek(4); // Salta la cabecera
            for (Cliente c : listaClientes) {
                c.escribir(archivo);
            }
            // 7. Activar la Bandera de ordenado en la cabecera (ejemplo: writeInt(1) en
            // offset 0)
            archivo.seek(0);
            archivo.writeInt(1); // 1 = ordenado; 0 = no ordenado

            ultimoMetodoOrdenado = MetodoOrdenamiento.RAM;

        } finally {
            cerrarArchivo();
        }
    }
}
