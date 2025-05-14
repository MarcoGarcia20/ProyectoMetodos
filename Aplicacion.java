import java.io.RandomAccessFile;
import java.time.LocalDate;


public class Aplicacion {
    RandomAccessFile archivo = null;
    String ruta = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\Prueba.txt";
    
    public Aplicacion() {
        Cliente clientes[] = new Cliente[10];
        try {
            archivo = new RandomAccessFile(ruta, "rw");
            //crearClientesAleatorios(archivo, clientes);
            for(int i = 0; i<10; i++){
                clientes[i] = new Cliente();
                clientes[i].mostrarDatos(archivo);
                //System.out.println("Cliente " + i + " leído del archivo.");
            }
            archivo.close();
   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }  


public void crearClientesAleatorios(RandomAccessFile archivo, Cliente[] clientes) {
    //Crear el archivo con 10 clientes
    try {
        for (int i = 0; i < clientes.length; i++) {
                clientes[i] = new Cliente();
                clientes[i].setDni("dni"+i);
                clientes[i].setEdad(20+i);
                clientes[i].setNombre("nombre"+i);
                clientes[i].setCorreo("nombre"+i+"@gmail.com");
                clientes[i].setIniSus(LocalDate.now());
                clientes[i].setCelular("98765432"+i);
                clientes[i].escribir(archivo);
                System.out.println("Cliente " + i + " grabado en el archivo.");
        }
        archivo.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}