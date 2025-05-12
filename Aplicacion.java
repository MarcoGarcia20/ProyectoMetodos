import java.io.RandomAccessFile;
import java.time.LocalDate;


public class Aplicacion {
    
    RandomAccessFile archivo = null;
    String ruta = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\Sistema.txt";
    
    public Aplicacion() {
        try {
            archivo = new RandomAccessFile(ruta, "rw");
            for(int i = 0; i<10; i++){
                Cliente clientes[] = new Cliente[10];
                clientes[i] = new Cliente();
                clientes[i].setDni("dni"+i);
                clientes[i].setEdad(i*10);
                clientes[i].setNombre("nombre"+i);
                clientes[i].setCorreo("nombre"+i+"@gmail.com");
                clientes[i].setIniSus(LocalDate.now());
                clientes[i].setCelular("98765432"+i);
                clientes[i].grabar(archivo);
                System.out.println("Cliente " + i + " grabado en el archivo.");
            }
            archivo.close();
   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }  
}