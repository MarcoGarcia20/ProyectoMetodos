import java.io.RandomAccessFile;
import java.time.LocalDate;

public class Servicio implements Acceso {
    private String nombre;
    private double montoPlan;
    private LocalDate fechaVencimiento;
    private double montoRecarga;

    public Servicio() {
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public double getMontoPlan() {
        return montoPlan;
    }
    public void setMontoPlan(double montoPlan) {
        this.montoPlan = montoPlan;
    }
    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    public double getMontoRecarga() {
        return montoRecarga;
    }
    public void setMontoRecarga(double montoRecarga) {
        this.montoRecarga = montoRecarga;
    }
    @Override
    public void escribir(RandomAccessFile archivo) {
        try {
            archivo.writeUTF(getNombre());
            archivo.writeDouble(getMontoPlan());
            archivo.writeLong(getFechaVencimiento().toEpochDay());
            archivo.writeDouble(getMontoRecarga());
        } catch (Exception e) {
            e.getMessage();
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
