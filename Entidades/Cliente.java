package Entidades;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.util.Random;

public class Cliente implements Acceso {
    private String dni;
    private int edad;
    private String nombre;
    private String correo;
    private LocalDate iniSus;
    private String celular;
    public Cliente() {
    }
    
    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getEdad(){
        return edad;
    }

    public void setEdad(int ed){
        this.edad = ed;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public LocalDate getIniSus() {
        return iniSus;
    }
    public void setIniSus(LocalDate iniSus) {
        this.iniSus = iniSus;
    }
    public String getCelular() {
        return celular;
    }
    public void setCelular(String celular) {
        this.celular = celular;
    }

    @Override
    public void escribir(RandomAccessFile archivo) {
        try {
            escribirCampoFijo(archivo, validarDNI(getDni()),8);
            archivo.writeInt(edad);
            escribirCampoFijo(archivo, validarNombre(getNombre()), 20);
            escribirCampoFijo(archivo, validarCorreo(getCorreo()), 20);
            archivo.writeLong(getIniSus().toEpochDay());
            escribirCampoFijo(archivo, validarCelular(getCelular()), 9);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void leer(RandomAccessFile archivo) {
        try {
            this.dni = leerCampo(archivo, 8);
            this.edad = archivo.readInt();
            this.nombre = leerCampo(archivo, 20);
            this.correo = leerCampo(archivo, 20);
            this.iniSus = LocalDate.ofEpochDay(archivo.readLong());
            this.celular = leerCampo(archivo, 9);
        } catch (Exception e) {
            e.getMessage();
        }
    }
    private static String leerCampo(RandomAccessFile archivo, int longitud) throws IOException {
        byte[] buffer = new byte[longitud];
        archivo.readFully(buffer);
        return new String(buffer, "ISO-8859-1").trim();
    }
    
    public void crearClientesAleatorios(RandomAccessFile archivo, int cantidad) throws IOException {
    Random rand = new Random();
    String[] nombres = {"Juan", "Maria", "Luis", "Ana", "Carlos", "Sofia", "Pedro", "Laura", "Diego", "Elena", "Javier", "Isabel", "Andres", "Carmen", "Raul", "Patricia", "Miguel", "Lucia", "Alberto", "Sara"};
    String[] apellidos = {"Garcia", "Rodriguez", "Martinez", "Lopez", "Perez", "Gonzalez", "Sanchez", "Romero", "Fernandez", "Torres", "Diaz", "Moreno", "Alvarez", "Jimenez", "Ruiz", "Hernandez", "Castro", "Ortiz", "Gutierrez", "Molina"};
    try {
        for (int i = 0; i < cantidad; i++) {
            String dni = String.format("%08d", rand.nextInt(100_000_000));
            setDni(dni);

            int edad = 20 + rand.nextInt(51); // 20 a 70 años
            //String edadStr = String.format("%02d", edad);
            setEdad(edad);
            
            // Seleccionar nombre y apellido realistas
            String nombre = nombres[rand.nextInt(nombres.length)];
            String apellido = apellidos[rand.nextInt(apellidos.length)];
            String nombreCompleto = (nombre + " " + apellido).trim();
            if (nombreCompleto.length() > 20) {
                nombreCompleto = nombreCompleto.substring(0, 20);
            }
            setNombre(nombreCompleto);


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
            setCorreo(correo);

            LocalDate fecha = LocalDate.now();
            //long fechaLong = fecha.toEpochDay();
            setIniSus(fecha);
            String celular = String.format("%09d", 900000000 + rand.nextInt(100_000_000));
            setCelular(celular);

            
            escribir(archivo);
            
        }
    } catch (Exception e) {
        System.err.println("Error al crear clientes aleatorios: " + e.getMessage());
        }
    }

    public void mostrarDatos(RandomAccessFile archivo) {
    try {
        archivo.seek(0); // Asegurarse de empezar desde el principio del archivo
        while (archivo.getFilePointer() < archivo.length()) {
            Cliente c = new Cliente();
                c.leer(archivo);
                
                System.out.println("_________________________________");
                System.out.println("DNI: " + c.getDni());
                System.out.println("Edad: " + c.getEdad());
                System.out.println("Nombre: " + c.getNombre());
                System.out.println("Correo: " + c.getCorreo());
                System.out.println("Fecha Suscripción: " + c.getIniSus());
                System.out.println("Celular: " + c.getCelular());
        }
    } catch (Exception e) {
        System.err.println("Error displaying data: " + e.getMessage());
    }
    }

    private void escribirCampoFijo(RandomAccessFile archivo, String valor, int longitud) throws IOException {
    String campo = valor;
    if (campo.length() > longitud) {
        campo = campo.substring(0, longitud);
    }
    while (campo.length() < longitud) {
        campo += " ";
    }
    archivo.write(campo.getBytes("ISO-8859-1")); // Escritura fija
}


    private String validarDNI(String dni) {
        if (!dni.matches("\\d{8}")) throw new IllegalArgumentException("DNI inválido");
        return dni;
    }

    private int validarEdad(int edad) {
        if (edad < 0 || edad > 120) throw new IllegalArgumentException("Edad inválida");
        return edad;
    }

    private String validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty() || nombre.length() > 20)
            throw new IllegalArgumentException("Nombre inválido");
        return nombre;
    }

    private String validarCorreo(String correo) {
        if (correo == null || !correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
            throw new IllegalArgumentException("Correo inválido");
        return correo;
    }

    private LocalDate validarIniSus(LocalDate iniSus) {
        if (iniSus == null || iniSus.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Fecha de suscripción inválida");
        return iniSus;
    }

    private String validarCelular(String celular) {
        if (celular == null || !celular.matches("\\d{9}"))
            throw new IllegalArgumentException("Celular inválido");
        return celular;
    }
}


