package src.Entidades.Indices;

// Índice específico para Cliente (clave: DNI)
public class IndiceCliente extends Indice {
    public IndiceCliente(String dni, long referencia) {
        super(dni, referencia);
    }

    public String getDni() {
        return clave;
    }

    public void setDni(String dni) {
        this.clave = dni;
    }
}