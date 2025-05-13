
import java.io.RandomAccessFile;
import java.time.LocalDate;
public class Solucion implements Acceso{
    private String descripcion;
    private LocalDate fechaSolucion;
    private String responsable;

    public Solucion() {
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
}
