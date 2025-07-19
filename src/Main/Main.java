package src.Main;

import javax.swing.SwingUtilities;
import src.Vistas.VentanaPrincipal;
import src.Vistas.VentanasCliente.VentanaIngresarCliente;
class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                VentanaPrincipal principal = new VentanaPrincipal();
                principal.setVisible(true);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        });
        // Aplicacion aplicacion = new Aplicacion();

    }
}

