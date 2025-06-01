package Ventanas;

import java.io.RandomAccessFile;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

public interface Datos {
    void cargarDesdeArchivo(RandomAccessFile archivo);
    void configurarTabla(JTable tabla);
}
