package src.Controladores;

import java.util.List;

import src.Entidades.Reclamo;
import src.Persistencia.ReclamoRepositorio;
import src.Vistas.IVista;
import src.Vistas.VentanasReclamo.VentanaFormularioReclamo;

public class ReclamoControlador {
    private ReclamoRepositorio repositorio;
    private VentanaFormularioReclamo vista;
    private IVista<Reclamo> ivistaReclamo;

    public ReclamoControlador(ReclamoRepositorio repositorio, VentanaFormularioReclamo vista) {
        this.repositorio = repositorio;
        this.vista = vista;
        this.vista.setControlador(this);
    }
    public ReclamoControlador(ReclamoRepositorio repositorio, IVista<Reclamo> ivistaReclamo) {
        this.repositorio = repositorio;
        this.ivistaReclamo = ivistaReclamo;
    }

    // Método invocado cuando se presiona el botón de Agregar Reclamo
    public void ingresarReclamo() {
        try {
            Reclamo reclamo = vista.obtenerReclamosDesdeCampos();
            // reclamo.validar(); // Si tienes validaciones, descomentar
            // repositorio.ingresarReclamo(reclamo);
            vista.limpiarCampos();
        } catch (Exception e) {
            vista.mostrarMensaje("Error al guardar el reclamo: " + e.getMessage());
        }
    }

    // Método invocado cuando se presiona el botón de Consultar Reclamo
    public Reclamo consultarReclamo(String idReclamo) {
        try {
            Reclamo reclamo = repositorio.busquedaSecuencial(idReclamo);
            if (reclamo != null) {
                return reclamo;
            } else {
                vista.mostrarMensaje("Reclamo no encontrado.");
            }
            return reclamo;
        } catch (Exception e) {
            vista.mostrarMensaje("Error al buscar el reclamo: " + e.getMessage());
            return null;
        }
    }
    // Método invocado cuando se presiona el botón de Modificar Reclamo
    public void modificarReclamo() {
        // try {
        //     Reclamo reclamo = vista.obtenerReclamoDesdeCampos();
        //     if (reclamo == null) {
        //         vista.deshabilitarCampos();
        //     } else {
        //         repositorio.modificarReclamo(reclamo);
        //         vista.limpiarCampos();
        //     }
        // } catch (Exception e) {
        //     vista.mostrarMensaje("Error al modificar el reclamo: " + e.getMessage());
        // }
    }
    // Método invocado cuando se presiona el botón de Eliminar Reclamo
    // public boolean eliminarReclamo() {
    //     try {
    //         Reclamo reclamo = vista.obtenerReclamoDesdeCampos();
    //         if (reclamo == null) {
    //             vista.mostrarMensaje("Reclamo no encontrado.");
    //             vista.deshabilitarCampos();
    //             return false;
    //         } else {
    //             repositorio.eliminarReclamo(reclamo);
    //             vista.limpiarCampos();
    //             return true;
    //         }
    //     } catch (Exception e) {
    //         vista.mostrarMensaje("Error al eliminar el reclamo: " + e.getMessage());
    //         return false;
    //     }
    // }
    public void mostrarReclamos() {
        try {
            List<Reclamo> reclamos = repositorio.listarReclamos();
            ivistaReclamo.listar(reclamos);
        } catch (Exception e) {
            ivistaReclamo.mostrarMensaje("Error al listar los reclamos: " + e.getMessage());
        }
    }

}
