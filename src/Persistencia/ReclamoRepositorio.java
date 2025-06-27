package src.Persistencia;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.Entidades.Reclamo;

public class ReclamoRepositorio {
    private final String ruta = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\";
    private RandomAccessFile archivo;
    private Map<String, Long> indiceDNI = new HashMap<>(); // Índice para buscar por DNI

    public ReclamoRepositorio() {
        try {
            archivo = new RandomAccessFile(ruta + "reclamos.dat", "rw");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void abrirArchivo(String modo) {
        try {
            archivo = new RandomAccessFile(ruta + "reclamos.dat", modo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void cerrarArchivo() {
        try {
            if (archivo != null) {
                archivo.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Reclamo busquedaSecuencial(String idReclamo) {
        try {
            abrirArchivo("r");
            long registros = (archivo.length() - 4) / Reclamo.LONGITUD_REGISTRO;
            for (int i = 0; i < registros; i++) {
                Reclamo reclamo = new Reclamo();
                reclamo.posicionar(archivo, i); // posición de registro
                reclamo.leer(archivo);
                if (reclamo.getIdReclamo().equals(idReclamo)) {
                    System.out.println("Reclamo encontrado: " + reclamo);
                    return reclamo;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cerrarArchivo();
        }
        return null;
    }

    public List<Reclamo> listarReclamos() {
        List<Reclamo> reclamos = new ArrayList<>();
        try {
            abrirArchivo("r");
            long registros = (archivo.length() - 4) / Reclamo.LONGITUD_REGISTRO;
            for (int i = 0; i < registros; i++) {
                Reclamo reclamo = new Reclamo();
                reclamo.posicionar(archivo, i); // posición de registro
                reclamo.leer(archivo);
                if (reclamo.isActivo()
                        && reclamo.getIdReclamo() != null
                        && !reclamo.getIdReclamo().isEmpty()) {
                    reclamos.add(reclamo);
                }
            }
        } catch (Exception e) {
            // Puedes usar JOptionPane, logger, o solo imprimir el error
            e.printStackTrace();
        } finally {
            cerrarArchivo();
        }
        return reclamos;
    }

}
