import java.io.RandomAccessFile;
import java.time.LocalDate;


public class Aplicacion {
    
    RandomAccessFile archivo = null;
    String ruta = "C:\\Users\\MARCO\\Metodos\\Sistema\\Archivos\\Sistema.dat";
    
    public Aplicacion() {
        Cliente clientes[] = new Cliente[10];
        try {
            archivo = new RandomAccessFile(ruta, "rw");
            
            // for(int i = 0; i<10; i++){
            //     clientes[i] = new Cliente();
            //     clientes[i].setDni("dni"+i);
            //     clientes[i].setEdad(20+i);
            //     clientes[i].setNombre("nombre"+i);
            //     clientes[i].setCorreo("nombre"+i+"@gmail.com");
            //     clientes[i].setIniSus(LocalDate.now());
            //     clientes[i].setCelular("98765432"+i);
            //     clientes[i].escribir(archivo);
            //     System.out.println("Cliente " + i + " grabado en el archivo.");
            // }

            for(int i = 0; i<10; i++){
                clientes[i] = new Cliente();
                // clientes[i].leer(archivo);
                if (archivo.getFilePointer() < archivo.length()) {
                    System.out.println("Registro " + (i + 1) + ":");
                    System.out.println("DNI: " + archivo.readUTF());
                    System.out.println("Edad: " + archivo.readInt());
                    System.out.println("Nombre: " + archivo.readUTF());
                    System.out.println("Correo: " + archivo.readUTF());
                    System.out.println("Inicio de Suscripción: " + LocalDate.ofEpochDay(archivo.readLong()));
                    System.out.println("Celular: " + archivo.readUTF());
                    System.out.println("-------------------------");
                } else {
                    System.out.println("No hay más registros disponibles.");
                    break;
                }
            }
            archivo.close();
   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }  
}