import java.io.RandomAccessFile;

public interface Acceso {
    void escribir(RandomAccessFile archivo);
    //void leer(RandomAccessFile archivo);
    void mostrarDatos(RandomAccessFile archivo);
}

