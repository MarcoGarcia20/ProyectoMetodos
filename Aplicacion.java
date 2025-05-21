import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.io.IOException;
import java.util.Random;


public class Aplicacion {
    RandomAccessFile archivo = null;
    String ruta = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\Prueba1.txt";

    public Aplicacion() {
        try {
            Cliente cliente[] = new Cliente[10000];
            // Crear el archivo si no existe
            archivo = new RandomAccessFile(ruta, "rw");
            // Si el archivo ya existe, no lo creamos de nuevo
            // archivo.setLength(0); // Descomentar si se quiere limpiar el archivo
            //crearClientesAleatorios(archivo, 10000);
            // Leer los registros
            for(int i = 0; i < cliente.length; i++){
                cliente[i] = new Cliente();
                cliente[i].mostrarDatos(archivo);
                //System.out.println("Cliente " + i + " leído del archivo.");
            }
            archivo.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int longfija = 8 + 2 + 10 + 10 + 20 + 8 + 9;

public void crearClientesAleatorios(RandomAccessFile archivo, int cantidad) {
    Random rand = new Random();
    String[] nombres = {"Juan", "Maria", "Luis", "Ana", "Carlos", "Sofia", "Pedro", "Laura", "Diego", "Elena"};
    String[] apellidos = {"Garcia", "Rodriguez", "Martinez", "Lopez", "Perez", "Gonzalez", "Sanchez", "Romero", "Fernandez", "Torres"};
    try {
        for (int i = 0; i < cantidad; i++) {
            String dni = String.format("%08d", rand.nextInt(100_000_000));
            int edad = 20 + rand.nextInt(31); // 20 a 50 años
            String edadStr = String.format("%02d", edad);
            
            // Seleccionar nombre y apellido realistas
            String nombre = nombres[rand.nextInt(nombres.length)];
            String apellido = apellidos[rand.nextInt(apellidos.length)];

            // Generar correo válido de 20 caracteres
            String baseCorreo = (nombre.trim() + apellido.trim()).toLowerCase();
            int maxBaseLength = 10; // Parte antes de @gmail.com (10 caracteres)
            
            if (baseCorreo.length() > maxBaseLength) {
                baseCorreo = baseCorreo.substring(0, maxBaseLength);
            } else if (baseCorreo.length() < maxBaseLength) {
                int remaining = maxBaseLength - baseCorreo.length();
                for (int j = 0; j < remaining; j++) {
                    baseCorreo += rand.nextInt(10); // Añade dígitos si es necesario
                }
            }
            String correo = baseCorreo + "@gmail.com";

            LocalDate fecha = LocalDate.now();
            long fechaLong = fecha.toEpochDay();
            String celular = String.format("%09d", 900000000 + rand.nextInt(100_000_000));

            // Escribir campos
            writeFixedString(archivo, dni, 8);
            writeFixedString(archivo, edadStr, 2);
            writeFixedString(archivo, nombre, 10);
            writeFixedString(archivo, apellido, 10);
            writeFixedString(archivo, correo, 20);
            archivo.writeLong(fechaLong);
            writeFixedString(archivo, celular, 9);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

// Métodos auxiliares sin cambios
private static String padRight(String s, int n) {
    if (s.length() >= n) return s.substring(0, n);
    return String.format("%-" + n + "s", s);
}

private static void writeFixedString(RandomAccessFile raf, String s, int length) throws IOException {
    String fixed = padRight(s, length);
    raf.writeBytes(fixed);
}
}