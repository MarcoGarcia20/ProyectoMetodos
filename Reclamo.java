import java.io.RandomAccessFile;
import java.time.LocalDate;
public class Reclamo implements Grabar {
    private LocalDate fecha;
    private String numeroLineaReclamo;
    private Boolean estado;
    private String solucion;

    public Reclamo() {
    }
    
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public String getNumeroLineaReclamo() {
        return numeroLineaReclamo;
    }
    public void setNumeroLineaReclamo(String numeroLineaReclamo) {
        this.numeroLineaReclamo = numeroLineaReclamo;
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
    public void grabar(RandomAccessFile archivo) {
        try {
            archivo.writeLong(getFecha().toEpochDay());
            archivo.writeUTF(getNumeroLineaReclamo());
            archivo.writeBoolean(getEstado());
            archivo.writeUTF(getSolucion());
        } catch (Exception e) {
            e.getMessage();
        }
    }

    
}
