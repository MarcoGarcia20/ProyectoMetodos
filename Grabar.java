import java.io.RandomAccessFile;

public interface Grabar {
    void escribir(RandomAccessFile archivo);
    void leer(RandomAccessFile archivo);
}

