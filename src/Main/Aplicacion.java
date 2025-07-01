package src.Main;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.io.IOException;
import java.util.Random;

import src.Entidades.Cliente;
import src.Entidades.Reclamo;


public class Aplicacion {
    RandomAccessFile archivo = null;
    RandomAccessFile archivo2 = null;
    private static String ruta = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\ClientesPrueba.dat";
    private static String ruta2 = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\Reclamos.dat";
    private static final int longfija = 8 + 4 + 10 + 10 + 20 + 8 + 9 + 1 + 4; // Longitud fija de un registro de cliente

    public Aplicacion() {
        try {
            archivo = new RandomAccessFile(ruta, "rw");
            archivo2 = new RandomAccessFile(ruta2, "rw");

            // 1. Escribir cabecera (primer disponible = -1)
            archivo.writeInt(-1); // Escribir un entero al inicio del archivo para indicar que está vacío

            // // 2. Crear y escribir los clientes después de la cabecera
            Cliente cliente = new Cliente();
            cliente.crearClientesAleatorios(archivo, 10000); // Crear 10,000 clientes aleatorios
            // // Crear el archivo si no existe

            // Reclamo reclamo = new Reclamo();
            // reclamo.crearReclamosPorCliente(archivo, archivo2, 1); // Crear 1 reclamo para los clientes

            // Si el archivo ya existe, no lo creamos de nuevo
            // 3. Mostrar los datos de los clientes (opcional)
            cliente.mostrarDatos(archivo);
            // reclamo.mostrarReclamos(archivo2);
            
            //Leer los registros
            archivo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
