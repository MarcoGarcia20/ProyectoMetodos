
import java.io.IOException;
class Principal {
    public static void main(String[] args) throws IOException{
        
    try{
        Aplicacion app = new Aplicacion();
        //throw new IOException("Simulated IOException");
    }catch (Exception e) {
        System.out.println("Error al abrir el archivo: " + e.getMessage());
    }   
    }
}

