
import java.io.RandomAccessFile;
import java.time.LocalDate;
public class Solucion implements Grabar{
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
    public void grabar(RandomAccessFile archivo) {
        try {
            archivo.writeUTF(getDescripcion());
            archivo.writeUTF(getResponsable());
            archivo.writeLong(getFechaSolucion().toEpochDay());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
