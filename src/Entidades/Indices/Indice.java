package src.Entidades.Indices;

import java.io.Serializable;

// Clase abstracta para definir el índice básico
public abstract class Indice implements Serializable {
    protected String clave;      
    protected long referencia;   

    public Indice(String clave, long referencia) {
        this.clave = clave;
        this.referencia = referencia;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public long getReferencia() {
        return referencia;
    }

    public void setReferencia(long referencia) {
        this.referencia = referencia;
    }

    // Método útil para comparar índices por clave
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Indice other = (Indice) obj;
        return clave.equals(other.clave);
    }

    @Override
    public int hashCode() {
        return clave.hashCode();
    }
}