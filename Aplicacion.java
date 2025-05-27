import Entidades.Cliente;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.io.IOException;
import java.util.Random;


public class Aplicacion {
    RandomAccessFile archivo = null;
    private static String ruta = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\Sistema.dat";
    private static final int longfija = 8 + 2 + 10 + 10 + 20 + 8 + 9;

    public Aplicacion() {
        try {
            archivo = new RandomAccessFile(ruta, "rw");
            //Cliente cliente = new Cliente();
            //cliente.crearClientesAleatorios(archivo, 10000); // Crear 10,000 clientes aleatorios
            // Crear el archivo si no existe
            
            // Si el archivo ya existe, no lo creamos de nuevo
            // archivo.setLength(0); // Descomentar si se quiere limpiar el archivo
            //cliente.leer(archivo); // Leer los registros
            //cliente.mostrarDatos(archivo); // Mostrar los datos de los clientes
            
            //Leer los registros
            


            // for(int i = 0; i < cliente.length; i++){
            //     cliente[i] = new Cliente();
            //     cliente[i].mostrarDatos(archivo);
            //     //System.out.println("Cliente " + i + " leído del archivo.");
            // }

            // analizarFragmentacion(archivo);
            archivo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //FRAGMENTACION

    public static void analizarFragmentacion(RandomAccessFile archivo) {
        try  {
            archivo = new RandomAccessFile(ruta, "r");
            long totalRegistros = archivo.length() / longfija;

            int registrosEliminados = 0;
            int fragmentacionInterna = 0;
            int fragmentacionExterna = 0;

            for (int i = 0; i < totalRegistros; i++) {
                archivo.seek(i * longfija);
                byte estado = archivo.readByte(); // '*' si está eliminado

                if (estado == '*') {
                    registrosEliminados++;
                    fragmentacionExterna += longfija;
                    continue;
                }

                fragmentacionInterna += calcularFragmentacionInterna(archivo);
            }

            System.out.println("Fragmentación Interna Total: " + fragmentacionInterna + " bytes");
            System.out.println("Fragmentación Externa Total: " + fragmentacionExterna + " bytes");
            System.out.println("Registros eliminados: " + registrosEliminados);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int calcularFragmentacionInterna(RandomAccessFile archivo) throws Exception {
        int total = 0;

        total += contarEspacios(leerCampo(archivo, 8));    // DNI
        total += contarEspacios(leerCampo(archivo, 2));    // Edad
        total += contarEspacios(leerCampo(archivo, 20));   // Nombre
        total += contarEspacios(leerCampo(archivo, 20));   // Correo

        archivo.skipBytes(8); // Fecha (long)
        total += contarEspacios(leerCampo(archivo, 9));    // Celular

        return total;
    }

    private static byte[] leerCampo(RandomAccessFile raf, int length) throws Exception {
        byte[] buffer = new byte[length];
        raf.readFully(buffer);
        return buffer;
    }

    private static int contarEspacios(byte[] campo) {
        int count = 0;
        for (byte b : campo) {
            if (b == ' ') count++;
        }
        return count;
    }
}
