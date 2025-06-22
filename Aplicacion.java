import Entidades.Cliente;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.io.IOException;
import java.util.Random;


public class Aplicacion {
    RandomAccessFile archivo = null;
    private static String ruta = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\Prueba.dat";
    private static final int longfija = 8 + 4 + 10 + 10 + 20 + 8 + 9 + 1 + 4; // Longitud fija de un registro de cliente

    public Aplicacion() {
        try {
            archivo = new RandomAccessFile(ruta, "rw");

            // 1. Escribir cabecera (primer disponible = -1)
            archivo.setLength(0); // Limpiar el archivo al iniciar la aplicación
            archivo.writeInt(-1); // Escribir un entero al inicio del archivo para indicar que está vacío

            // 2. Crear y escribir los clientes después de la cabecera
            Cliente cliente = new Cliente();
            cliente.crearClientesAleatorios(archivo, 10000); // Crear 10,000 clientes aleatorios
            // Crear el archivo si no existe
            
            // Si el archivo ya existe, no lo creamos de nuevo
            // 3. Mostrar los datos de los clientes (opcional)
            archivo.seek(4); // Saltar la cabecera para mostrar desde el primer cliente
            cliente.mostrarDatos(archivo);
            
            //Leer los registros
            


            // for(int i = 0; i < cliente.length; i++){
            //     cliente[i] = new Cliente();
            //     cliente[i].mostrarDatos(archivo);
            //     //System.out.println("Cliente " + i + " leído del archivo.");
            // }

            archivo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
