
import java.io.RandomAccessFile;
import java.time.LocalDate;

public class Cliente implements Grabar {
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
    public void grabar(RandomAccessFile archivo) {
        
        try {
            archivo.writeUTF(getDni());
            archivo.writeUTF(getNombre());
            archivo.writeInt(edad);
            archivo.writeUTF(getCorreo());
            archivo.writeLong(getIniSus().toEpochDay());
            archivo.writeUTF(getCelular());
        } catch (Exception e) {
            e.getMessage();
        }
    }
    
    
}
