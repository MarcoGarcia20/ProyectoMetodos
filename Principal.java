
import java.io.IOException;

import Ventanas.VentanaCrear;
import Ventanas.VentanaPrincipal;
class Principal {
    public static void main(String[] args) throws IOException{
        
    try{
        VentanaPrincipal principal = new VentanaPrincipal();
        // Crear la ventana de la aplicación
        VentanaCrear ventanaCrear = new VentanaCrear();
        // Configurar la ventana de la aplicación
        ventanaCrear.setVisible(true);

        //principal.setVisible(true);
        //Aplicacion app = new Aplicacion();
    }catch (Exception e) {
        System.out.println("Error al abrir el archivo: " + e.getMessage());
    }   
    }
}

