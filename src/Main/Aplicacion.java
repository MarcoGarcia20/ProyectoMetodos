package src.Main;

import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.io.IOException;
import java.util.Random;

import src.Entidades.Cliente;
import src.Entidades.Reclamo;
import src.Persistencia.IndiceClienteArchivo;

public class Aplicacion {
    // RandomAccessFile archivo = null;
    // RandomAccessFile archivo2 = null;
    // private static String ruta = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\Clientes.dat";
    // private static String ruta2 = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\Reclamos.dat";

    public Aplicacion() {
        String archivoDatos = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\Clientes.dat";
        String archivoIndice = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\IndiceClientes.ind";

        try {
            IndiceClienteArchivo indiceArchivo = new IndiceClienteArchivo(archivoIndice);
            // 1. Al iniciar el sistema, verifica la bandera
            if (indiceArchivo.isBanderaIndiceActiva(archivoDatos)) {
                System.out.println("La bandera de modificación del índice está activa.");
                System.out.println("Reconstruyendo índice de clientes...");
                indiceArchivo.reconstruirIndice(archivoDatos);
                System.out.println("Índice reconstruido correctamente.");
            } else {
                System.out.println("La bandera de modificación está inactiva. El índice está sincronizado.");
            }

            // 2. Opcional: muestra los índices generados
            System.out.println("Contenido del archivo de índices:");
            indiceArchivo.cargarIndiceAMemoria().forEach(
                    idx -> System.out.println("DNI: " + idx.getDni() + ", Referencia: " + idx.getReferencia()));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
