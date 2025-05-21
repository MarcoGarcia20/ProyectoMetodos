
import java.io.RandomAccessFile;
import java.time.LocalDate;

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
            archivo.writeUTF(getDni());
            archivo.writeInt(edad);
            archivo.writeUTF(getNombre());
            archivo.writeUTF(getCorreo());
            archivo.writeLong(getIniSus().toEpochDay());
            archivo.writeUTF(getCelular());
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @Override
    public void mostrarDatos(RandomAccessFile archivo) {
    try {
        while (archivo.getFilePointer() < archivo.length()) {
            System.out.println("_________________________________");
            
            // DNI: 8 bytes (String)
            byte[] dniBytes = new byte[8];
            archivo.readFully(dniBytes);
            System.out.println("DNI: " + new String(dniBytes, "ISO-8859-1").trim());

            // Edad: 2 bytes (String numérico)
            byte[] edadBytes = new byte[2];
            archivo.readFully(edadBytes);
            System.out.println("Edad: " + new String(edadBytes, "ISO-8859-1").trim());

            // Nombre: 10 bytes (String)
            byte[] nombreBytes = new byte[10];
            archivo.readFully(nombreBytes);
            System.out.println("Nombre: " + new String(nombreBytes, "ISO-8859-1").trim());

            // Apellido: 10 bytes (String)
            byte[] apellidoBytes = new byte[10];
            archivo.readFully(apellidoBytes);
            System.out.println("Apellido: " + new String(apellidoBytes, "ISO-8859-1").trim());

            // Correo: 20 bytes (String)
            byte[] correoBytes = new byte[20];
            archivo.readFully(correoBytes);
            System.out.println("Correo: " + new String(correoBytes, "ISO-8859-1").trim());

            // Fecha: 8 bytes (long)
            System.out.println("Inicio de Suscripción: " + LocalDate.ofEpochDay(archivo.readLong()));

            // Celular: 9 bytes (String numérico)
            byte[] celularBytes = new byte[9];
            archivo.readFully(celularBytes);
            System.out.println("Celular: " + new String(celularBytes, "ISO-8859-1").trim());
        }
    } catch (Exception e) {
        System.err.println("Error displaying data: " + e.getMessage());
    }
}

}
