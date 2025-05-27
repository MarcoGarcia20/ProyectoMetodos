
import java.io.IOException;

import Ventanas.VentanaCrear;
import Ventanas.VentanaPrincipal;
class Principal {
    public static void main(String[] args) throws IOException{
        
    try{
        // Crear la ventana de la aplicación
        VentanaPrincipal principal = new VentanaPrincipal();

        principal.setVisible(true);
        //Aplicacion app = new Aplicacion();
        
    }catch (Exception e) {
        System.out.println("Error : " + e.getMessage());
    }   
    }
}

