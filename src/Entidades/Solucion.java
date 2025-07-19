package src.Entidades;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
public class Solucion implements Acceso{
    private String idSolucion; // 8
    private String descripcion; // 20
    private LocalDate fechaSolucion; // 8
    private String responsable; // 20

    private boolean solucionActivo; // 1
    private int siguienteDisponible; // 4

    public static final int LONGITUD_REGISTRO = 61; // Longitud total del registro

    public Solucion() {
    }
    public String getIdSolucion() {
        return idSolucion;
    }
    public void setIdSolucion(String idSolucion) {
        this.idSolucion = idSolucion;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public LocalDate getFechaSolucion() {
        return fechaSolucion;
    }
    public void setFechaSolucion(LocalDate fechaSolucion) {
        this.fechaSolucion = fechaSolucion;
    }
    public String getResponsable() {
        return responsable;
    }
    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    

    @Override
    public void escribir(RandomAccessFile archivo) {
        try {
            archivo.writeUTF(getDescripcion());
            archivo.writeUTF(getResponsable());
            archivo.writeLong(getFechaSolucion().toEpochDay());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void leer(RandomAccessFile archivo) {
        try {
            archivo.seek(0);
            archivo.readUTF();
        } catch (Exception e) {
            e.getMessage();
        }
    }
    @Override
    public void posicionar(RandomAccessFile archivo, int posicion) throws IOException {
        if (posicion < 0) {
            throw new IllegalArgumentException("El número de registros no puede ser negativo");
            }
            long posicionBytes = 4 + (long) posicion * LONGITUD_REGISTRO; // SUMA 4 bytes de cabecera
            if (posicionBytes > archivo.length()) {
                throw new IOException("El número de registros excede el tamaño del archivo");
            }
            archivo.seek(posicionBytes);
    }
    @Override
    public boolean isActivo() {
        return solucionActivo;
    }
    @Override
    public void setActivo(boolean activo) {
        this.solucionActivo = activo;
    }
    // Utilidad para escribir un campo de longitud fija
    private void escribirCampoFijo(RandomAccessFile archivo, String valor, int longitud) throws IOException {
        if (valor == null)
            valor = "";
        StringBuilder sb = new StringBuilder(valor);
        if (sb.length() > longitud)
            sb.setLength(longitud);
        while (sb.length() < longitud)
            sb.append(" ");
        archivo.write(sb.toString().getBytes("ISO-8859-1"));
    }
    // Utilidad para leer un campo de longitud fija
    private String leerCampoFijo(RandomAccessFile archivo, int longitud) throws IOException {
        byte[] buffer = new byte[longitud];
        archivo.readFully(buffer);
        return new String(buffer, "ISO-8859-1").trim();
    }

    public static void mostrarSoluciones(RandomAccessFile archivo) {
        try {
            archivo.seek(4);
            while (archivo.getFilePointer() < archivo.length()) {
                Solucion s = new Solucion();
                s.leer(archivo);
                System.out.println("ID Solucion: " + s.getIdSolucion());
                System.out.println("Descripción: " + s.getDescripcion());
                System.out.println("Fecha: " + s.getFechaSolucion());
                System.out.println("Responsable : " + s.getResponsable());
                System.out.println("Activo: " + s.isActivo());
                System.out.println("-------------------------------------");
            }
        } catch (IOException e) {
            System.err.println("Error mostrando reclamos: " + e.getMessage());
        }
    }
}
