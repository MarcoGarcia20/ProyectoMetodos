package src.Entidades.Indices;

import java.io.*;

public class IndiceCliente extends Indice {
    public static final int DNI_LENGTH = 8;

    public IndiceCliente(String dni, long referencia) {
        super(dni, referencia);
    }

    public String getDni() { return clave; }

    @Override
    public void escribir(RandomAccessFile raf) throws IOException {
        byte[] dniBytes = new byte[DNI_LENGTH];
        byte[] src = clave.getBytes();
        System.arraycopy(src, 0, dniBytes, 0, Math.min(src.length, DNI_LENGTH));
        raf.write(dniBytes);
        raf.writeLong(referencia);
    }
    
    public static IndiceCliente leer(RandomAccessFile raf) throws IOException {
        byte[] dniBytes = new byte[DNI_LENGTH];
        raf.readFully(dniBytes);
        String dni = new String(dniBytes).trim();
        long ref = raf.readLong();
        return new IndiceCliente(dni, ref);
    }
}