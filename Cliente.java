
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
                System.out.println("DNI: " + archivo.readUTF());
                System.out.println("Edad: " + archivo.readInt());
                System.out.println("Nombre: " + archivo.readUTF());

                System.out.println("Correo: " + archivo.readUTF());
                System.out.println("Inicio de Suscripción: " + LocalDate.ofEpochDay(archivo.readLong()));
                System.out.println("Celular: " + archivo.readUTF());
            }
        } catch (Exception e) {
            System.err.println("Error displaying data: " + e.getMessage());
        }
    }

}
