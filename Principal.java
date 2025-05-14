
import java.io.IOException;
class Principal {
    public static void main(String[] args) throws IOException{
        
    try{
        Aplicacion app = new Aplicacion();
    }catch (Exception e) {
        System.out.println("Error al abrir el archivo: " + e.getMessage());
    }   
    }
}

