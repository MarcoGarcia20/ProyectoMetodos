import Entidades.Cliente;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.io.IOException;
import java.util.Random;


public class Aplicacion {
    RandomAccessFile archivo = null;
    private static String ruta = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\Clientes.dat";
    private static final int longfija = 8 + 2 + 10 + 10 + 20 + 8 + 9;

    public Aplicacion() {
        try {
            archivo = new RandomAccessFile(ruta, "rw");
            Cliente cliente = new Cliente();
            cliente.crearClientesAleatorios(archivo, 10000); // Crear 10,000 clientes aleatorios
            // Crear el archivo si no existe
            
            // Si el archivo ya existe, no lo creamos de nuevo
            // archivo.setLength(0); // Descomentar si se quiere limpiar el archivo
            //cliente.leer(archivo); // Leer los registros
            cliente.mostrarDatos(archivo); // Mostrar los datos de los clientes
            
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
