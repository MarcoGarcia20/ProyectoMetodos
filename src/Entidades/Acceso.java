package src.Entidades;
import java.io.IOException;
import java.io.RandomAccessFile;

public interface Acceso {
    void escribir(RandomAccessFile archivo);
    void leer(RandomAccessFile archivo);
    void posicionar(RandomAccessFile archivo, int posicion) throws IOException;

    boolean isActivo();
    void setActivo(boolean activo);
}

