package src.Entidades;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.util.Random;

public class Reclamo implements Acceso {
    private String idReclamo; // 8
    private String dniCliente; // 8
    private String numeroLineaCliente; // 9
    private String descripcion; // 35
    private LocalDate fecha; // 8

    private Boolean estado; // 1
    private String solucion; // 30

    private boolean reclamoActivo = true; // Por defecto, el reclamo está activo
    private int siguienteDisponible = -1; // solo relevante si está inactivo

    public static final int LONGITUD_REGISTRO = 104;

    public Reclamo() {
    }

    public String getIdReclamo() {
        return idReclamo;
    }

    public void setIdReclamo(String idReclamo) {
        this.idReclamo = idReclamo;
    }

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public String getNumeroLineaCliente() {
        return numeroLineaCliente;
    }

    public void setNumeroLineaCliente(String numeroLineaCliente) {
        this.numeroLineaCliente = numeroLineaCliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getSolucion() {
        return solucion;
    }

    public void setSolucion(String solucion) {
        this.solucion = solucion;
    }

    @Override
    public void escribir(RandomAccessFile archivo) {
        try {
            escribirCampoFijo(archivo, idReclamo, 8); // idReclamo (8)
            escribirCampoFijo(archivo, dniCliente, 8); // dniCliente (8)
            escribirCampoFijo(archivo, numeroLineaCliente, 9); // numeroLineaCliente (9)
            escribirCampoFijo(archivo, descripcion, 35); // descripcion (35)
            archivo.writeLong(fecha != null ? fecha.toEpochDay() : 0); // fecha (8)
            archivo.writeBoolean(estado != null ? estado : false); // estado (1)
            escribirCampoFijo(archivo, solucion, 30); // solucion (30)
            archivo.writeBoolean(reclamoActivo); // reclamoActivo (1)
            archivo.writeInt(siguienteDisponible); // siguienteDisponible (4)
        } catch (IOException e) {
            System.err.println("Error al escribir reclamo: " + e.getMessage());
        }
    }

    @Override
    public void leer(RandomAccessFile archivo) {
        try {
            idReclamo = leerCampoFijo(archivo, 8);
            dniCliente = leerCampoFijo(archivo, 8);
            numeroLineaCliente = leerCampoFijo(archivo, 9);
            descripcion = leerCampoFijo(archivo, 35);
            long fechaLong = archivo.readLong();
            fecha = fechaLong != 0 ? LocalDate.ofEpochDay(fechaLong) : null;
            estado = archivo.readBoolean();
            solucion = leerCampoFijo(archivo, 30);
            reclamoActivo = archivo.readBoolean();
            siguienteDisponible = archivo.readInt();
        } catch (IOException e) {
            System.err.println("Error al leer reclamo: " + e.getMessage());
        }
    }

    @Override
    public void posicionar(RandomAccessFile archivo, int registro) throws IOException {
        if (registro < 0) {
            throw new IllegalArgumentException("El número de registros no puede ser negativo");
            }
            long posicionBytes = 4 + (long) registro * LONGITUD_REGISTRO; // SUMA 4 bytes de cabecera
            if (posicionBytes > archivo.length()) {
                throw new IOException("El número de registros excede el tamaño del archivo");
            }
            archivo.seek(posicionBytes);
    }

    @Override
    public boolean isActivo() {
        return reclamoActivo;
    }

    @Override
    public void setActivo(boolean activo) {
        this.reclamoActivo = activo;
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

    // Genera reclamos para los clientes activos en archivoClientes
    public static void crearReclamosPorCliente(RandomAccessFile archivoClientes, RandomAccessFile archivoReclamos,
            int reclamosPorCliente) throws IOException {
        archivoClientes.seek(4); // Salta cabecera si existe
        Random rand = new Random();
        while (archivoClientes.getFilePointer() < archivoClientes.length()) {
            Cliente cliente = new Cliente();
            cliente.leer(archivoClientes);
            if (cliente.isActivo() && cliente.getDni() != null && !cliente.getDni().isBlank()
                    && rand.nextDouble() < 0.7) {
                for (int i = 0; i < reclamosPorCliente; i++) {
                    Reclamo r = generarReclamoAleatorio(cliente.getDni(), cliente.getCelular());
                    r.escribir(archivoReclamos);
                }
            }
        }
    }

    private static Reclamo generarReclamoAleatorio(String dniCliente, String numeroLinea) {
        Random rand = new Random();
        String[] descripciones = {
                "Corte de servicio inesperado", "Facturacion erronea", "Problemas de conexion",
                "Cobro por servicio no solicitado", "Llamadas interrumpidas", "Internet lento"
        };
        String[] soluciones = {
                "Caso resuelto", "Derivado a soporte", "Requiere validacion", "Cliente no contactado", "En espera"
        };

        Reclamo r = new Reclamo();
        r.setIdReclamo("R" + String.format("%06d", rand.nextInt(999999)));
        r.setDniCliente(dniCliente);
        r.setNumeroLineaCliente(numeroLinea != null ? numeroLinea : "900000000");
        r.setDescripcion(descripciones[rand.nextInt(descripciones.length)]);
        r.setFecha(LocalDate.now().minusDays(rand.nextInt(30)));
        r.setEstado(rand.nextBoolean());
        r.setSolucion(soluciones[rand.nextInt(soluciones.length)]);

        return r;
    }

    // Crear N reclamos aleatorios (no ligados a clientes)
    public static void crearReclamosAleatorios(RandomAccessFile archivo, int cantidad) throws IOException {
        Random rand = new Random();
        String[] descripciones = {
                "Corte de servicio inesperado", "Facturacion erronea", "Problemas de conexion",
                "Maltrato de operador", "Cobro por servicio no solicitado", "Llamadas interrumpidas"
        };
        String[] soluciones = {
                "Revisado por tecnico", "Reembolso aplicado", "En investigacion",
                "Se requiere mas información", "Caso cerrado"
        };
        for (int i = 0; i < cantidad; i++) {
            Reclamo r = new Reclamo();
            r.setIdReclamo("R" + String.format("%08d", rand.nextInt(100000000)));
            r.setDniCliente(String.format("%08d", rand.nextInt(100_000_000)));
            r.setNumeroLineaCliente("9" + (10000000 + rand.nextInt(89999999)));
            r.setDescripcion(descripciones[rand.nextInt(descripciones.length)]);
            r.setFecha(LocalDate.now().minusDays(rand.nextInt(30)));
            r.setEstado(rand.nextBoolean());
            r.setSolucion(soluciones[rand.nextInt(soluciones.length)]);
            r.escribir(archivo);
        }
    }

    // Mostrar todos los reclamos
    public static void mostrarReclamos(RandomAccessFile archivo) {
        try {
            archivo.seek(4);
            while (archivo.getFilePointer() < archivo.length()) {
                Reclamo r = new Reclamo();
                r.leer(archivo);
                System.out.println("ID: " + r.getIdReclamo());
                System.out.println("DNI Cliente: " + r.getDniCliente());
                System.out.println("Nro Línea: " + r.getNumeroLineaCliente());
                System.out.println("Descripción: " + r.getDescripcion());
                System.out.println("Fecha: " + r.getFecha());
                System.out.println("Estado: " + (r.getEstado() != null && r.getEstado() ? "Resuelto" : "Pendiente"));
                System.out.println("Solución: " + r.getSolucion());
                System.out.println("Activo: " + r.isActivo());
                System.out.println("-------------------------------------");
            }
        } catch (IOException e) {
            System.err.println("Error mostrando reclamos: " + e.getMessage());
        }
    }

}
