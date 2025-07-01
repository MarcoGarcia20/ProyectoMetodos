package src.Entidades;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import javax.swing.JOptionPane;

public class Cliente implements Acceso {
    private String dni;
    private byte edad;
    private String nombre;
    private String correo;
    private LocalDate iniSus;
    private String celular;

    private boolean clienteActivo = true; // Por defecto, el cliente está activo
    private int siguienteDisponible = -1; // solo relevante si está inactivo

    public static final int LONGITUD_REGISTRO = 8 + 1 + 20 + 20 + 8 + 9 + 1 + 4; // = 71

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

    public byte getEdad() {
        return edad;
    }

    public void setEdad(byte ed) {
        this.edad = (byte) ed;
    }

    public void setEdad(int edad) {
        this.edad = (byte) edad;
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

    public int getSiguienteDisponible() {
        return siguienteDisponible;
    }

    public void setSiguienteDisponible(int siguienteDisponible) {
        this.siguienteDisponible = siguienteDisponible;
    }

    @Override
    public boolean isActivo() {
        return clienteActivo;
    }

    @Override
    public void setActivo(boolean activo) {
        this.clienteActivo = activo;
    }

    @Override
    public void escribir(RandomAccessFile archivo) {
        try {
            escribirCampoFijo(archivo, validarDNI(getDni()), 8);
            archivo.writeByte(validarEdad(getEdad() & 0xFF));
            escribirCampoFijo(archivo, validarNombre(getNombre()), 20);
            escribirCampoFijo(archivo, validarCorreo(getCorreo()), 20);

            LocalDate fecha = getIniSus();
            if (fecha == null) {
                fecha = LocalDate.of(2020, 1, 1); // Fecha por defecto
            }
            archivo.writeLong(fecha.toEpochDay());
            escribirCampoFijo(archivo, validarCelular(getCelular()), 9);

            // En el método escribir, se implementa:
            archivo.writeBoolean(clienteActivo);
            archivo.writeInt(siguienteDisponible);
            // Escribir el siguiente disponible

        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void leer(RandomAccessFile archivo) {
        try {
            this.dni = leerCampo(archivo, 8).trim();
            // Si DNI está vacío, saltar el resto del registro
            if (dni.trim().isEmpty()) {
                archivo.skipBytes(LONGITUD_REGISTRO - 8);
                this.edad = 0;
                this.nombre = "";
                this.correo = "";
                this.iniSus = null;
                this.celular = "";
                this.clienteActivo = false;
                this.siguienteDisponible = -1; // No hay siguiente disponible
                return;
            }
            this.edad = archivo.readByte();
            this.nombre = leerCampo(archivo, 20).trim();
            this.correo = leerCampo(archivo, 20).trim();

            // Solo lee la fecha si el registro es válido
            long epochDay = archivo.readLong();
            if (epochDay < -365243219162L || epochDay > 365241780471L) {
                // Si es inválido, usa una fecha por defecto (por ejemplo, 2020-01-01)
                this.iniSus = LocalDate.of(2020, 1, 1);
            } else {
                this.iniSus = LocalDate.ofEpochDay(epochDay);
            }

            this.celular = leerCampo(archivo, 9).trim();
            this.clienteActivo = archivo.readBoolean();
            this.siguienteDisponible = archivo.readInt(); // Leer el siguiente disponible
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al leer el registro: " +
                    e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
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

    private String leerCampo(RandomAccessFile archivo, int longitud) throws IOException {
        byte[] buffer = new byte[longitud];
        archivo.readFully(buffer);
        return new String(buffer).trim();
    }

    // Método para crear clientes aleatorios y escribirlos en el archivo
    public void crearClientesAleatorios(RandomAccessFile archivo, int cantidad) throws IOException {
        Random rand = new Random();
        String[] nombres = { "Juan", "Maria", "Luis", "Ana", "Carlos", "Sofia", "Pedro", "Laura", "Diego", "Elena",
                "Javier", "Isabel", "Andres", "Carmen", "Raul", "Patricia", "Miguel", "Lucia", "Alberto", "Sara",
                "Fernando", "Claudia", "Victor", "Paula", "Jorge", "Marta", "David", "Cristina", "Antonio", "Veronica",
                "Ricardo", "Silvia", "Eduardo", "Teresa", "Roberto", "Lorena", "Hector", "Beatriz", "Gabriel",
                "Adriana", "Oscar", "Natalia", "Arturo", "Mariana", "Felipe", "Lorena", "Raquel", "Esteban", "Gloria",
                "Rosa", "Victor", "Patricia", "Alejandro", "Ines", "Cecilia", "Julian", "Angela", "Santiago",
                "Carolina", "Gonzalo", "Luciana", "Matias", "Valentina", "Camila", "Emilio", "Florencia",
                "Ignacio", "Victoria", "Bruno", "Julieta", "Dario", "Lina", "Nicolas", "Gabriela",
                "Mauricio", "Ximena", "Estefania", "Belen", "Claudia", "Lourdes", "Patricio", "Marisol", "Guillermo",
                "Cecilia",
                "Rafael", "Margarita", "Alfonso", "Susana", "Cristian", "Elisa", "Hugo", "Miranda", "Lorenzo" };
        String[] apellidos = { "Garcia", "Rodriguez", "Martinez", "Lopez", "Perez", "Gonzalez", "Sanchez", "Romero",
                "Fernandez", "Torres", "Diaz", "Moreno", "Alvarez", "Jimenez", "Ruiz", "Hernandez", "Castro", "Ortiz",
                "Gutierrez", "Molina", "Reyes", "Cruz", "Ramirez", "Flores", "Vasquez", "Guzman", "Ramos", "Mendez",
                "Castillo", "Delgado", "Aguilar", "Navarro", "Paredes", "Soto", "Cabrera", "Salazar", "Campos",
                "Cortez", "Vega", "Rojas", "Morales", "Ponce", "Cano", "Bravo", "Escobar", "Cordero", "Mora", "Lara",
                "Bermudez", "Quintero", "Cardenas", "Acosta", "Palacios", "Montoya", "Ceballos", "Ocampo", "Arroyo",
                "Valencia", "Sierra", "Pineda", "Bustos", "Gaitan", "Hidalgo", "Pizarro",
                "Salinas", "Cifuentes", "Bermudez", "Lozada", "Cano", "Galindo", "Maldonado", "Paz", "Sarmiento",
                "Vargas", "Cifuentes", "Barrera", "Gamboa", "Roldan", "Sarmiento", "Tovar", "Zambrano",
                "Vallejo","Bermudez", "Pineda", "Galindo", "Maldonado", "Paz", "Sarmiento", "Vargas", "Cifuentes",
                "Barrera", "Gamboa", "Roldan", "Sarmiento", "Tovar", "Zambrano", "Vallejo" };
        try {
            for (int i = 0; i < cantidad; i++) {
                String dni = String.format("%08d", rand.nextInt(100_000_000));
                setDni(dni);
                // Generar edad aleatoria entre 20 y 70 años
                byte edad = (byte) (20 + rand.nextInt(51)); // 20 a 70 años
                // String edadStr = String.format("%02d", edad);
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

                LocalDate fechaInicio = LocalDate.of(2020, 1, 1);
                LocalDate fechaFin = LocalDate.now();
                LocalDate fecha = generarFechaAleatoria(fechaInicio, fechaFin);
                // long fechaLong = fecha.toEpochDay();
                setIniSus(fecha);
                String celular = String.format("%09d", 900000000 + rand.nextInt(100_000_000));
                setCelular(celular);

                escribir(archivo);

            }
        } catch (Exception e) {
            System.err.println("Error al crear clientes aleatorios: " + e.getMessage());
        }
    }

    public static LocalDate generarFechaAleatoria(LocalDate inicio, LocalDate fin) {
        long diasEntre = ChronoUnit.DAYS.between(inicio, fin);
        long diaRandom = new Random().nextLong(diasEntre + 1);
        return inicio.plusDays(diaRandom);
    }

    public void mostrarDatos(RandomAccessFile archivo) {
        try {
            archivo.seek(4); // Saltar la cabecera del archivo
            // Leer hasta el final del archivo
            if (archivo.length() <= 4) {
                System.out.println("No hay datos para mostrar.");
                return;
            }
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
        StringBuilder sb = new StringBuilder(valor == null ? "" : valor);
        if (sb.length() > longitud)
            sb.setLength(longitud);
        while (sb.length() < longitud)
            sb.append(' ');
        archivo.writeBytes(sb.toString()); // 1 byte por caracter, SOLO ASCII
    }

    private String validarDNI(String dni) {
        if (!dni.matches("\\d{8}"))
            throw new IllegalArgumentException("DNI inválido");
        return dni;
    }

    private byte validarEdad(int edad) {
        if (edad < 0 || edad > 90)
            throw new IllegalArgumentException("Edad inválida");
        return (byte) edad;
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
