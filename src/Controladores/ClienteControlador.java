package src.Controladores;

import java.util.List;

import src.Entidades.Cliente;
import src.Persistencia.ClienteRepositorio;
import src.Vistas.IVista;
import src.Vistas.VentanasCliente.VentanaCompactarCliente;
import src.Vistas.VentanasCliente.VentanaCrearArchivoCliente;
import src.Vistas.VentanasCliente.VentanaFormularioCliente;
import src.Vistas.VentanasCliente.VentanaListarCliente;

public class ClienteControlador {
    private ClienteRepositorio repositorio;
    private VentanaFormularioCliente vista;
    private IVista<Cliente> ivistaCliente;
    private VentanaCompactarCliente ventanaCompactarCliente;
    private VentanaCrearArchivoCliente ventanaCrearArchivoCliente;

    public ClienteControlador(ClienteRepositorio repositorio, VentanaFormularioCliente vista) {
        this.repositorio = repositorio;
        this.vista = vista;
        this.vista.setControlador(this);
    }

    public ClienteControlador(ClienteRepositorio repositorio, IVista<Cliente> vistalistar) {
        this.repositorio = repositorio;
        this.ivistaCliente = vistalistar;
    }

    public ClienteControlador(ClienteRepositorio repositorio, VentanaCompactarCliente ventanaCompactarCliente) {
        this.repositorio = repositorio;
        this.ventanaCompactarCliente = ventanaCompactarCliente;
    }

    public ClienteControlador(ClienteRepositorio repositorio, VentanaCrearArchivoCliente ventanaCrearArchivoCliente) {
        this.repositorio = repositorio;
        this.ventanaCrearArchivoCliente = ventanaCrearArchivoCliente;
    }

    /*
     * 1era Entrega Métodos de Acceso
     */
    // Método invocado cuando se presiona el botón de Crear Archivo Cliente
    public void crearArchivoClientes(int cantidad) {
        try {
            repositorio.crearArchivo(cantidad);
            ventanaCrearArchivoCliente.mostrarMensaje("Archivo creado correctamente.");
        } catch (Exception e) {
            ventanaCrearArchivoCliente.mostrarMensaje("Error: " + e.getMessage());
        }
    }

    // Método invocado cuando se presiona el botón de Agregar Cliente
    public void ingresarCliente() {
        try {
            Cliente cliente = vista.obtenerClienteDesdeCampos();
            // cliente.validar();
            repositorio.ingresarCliente(cliente);
        } catch (Exception e) {
            vista.mostrarMensaje("Error al guardar el cliente: " + e.getMessage());
        }
    }

    // Método invocado cuando se presiona el botón de Consultar Cliente
    public Cliente consultarCliente(String dni) {
        try {
            Cliente cliente = repositorio.busquedaSecuencial(dni);
            return cliente;
        } catch (Exception e) {
            vista.mostrarMensaje("Error al buscar el cliente: " + e.getMessage());
            return null;
        }
    }

    // Método invocado cuando se presiona el botón de Modificar Cliente
    public void modificarCliente() {
        try {
            Cliente cliente = vista.obtenerClienteDesdeCampos();
            if (cliente == null) {
                vista.deshabilitarCampos();
            } else {
                repositorio.modificarCliente(cliente);
                vista.limpiarCampos();
            }
        } catch (Exception e) {
            vista.mostrarMensaje("Error al modificar el cliente: " + e.getMessage());
        }
    }

    // Método invocado cuando se presiona el botón de Eliminar Cliente
    public boolean eliminarCliente() {
        try {
            String dni = vista.getCampoDni();
            if (dni == null || dni.trim().isEmpty()) {
                vista.mostrarMensaje("Ingrese un DNI para eliminar.");
                return false;
            }
            dni = dni.trim();

            boolean eliminado = repositorio.eliminarCliente(dni.trim());
            if (eliminado) {
                vista.mostrarMensaje("✔ Registro eliminado.");
                vista.limpiarCampos();
                vista.deshabilitarCampos();
            } else {
                vista.mostrarMensaje("No se pudo eliminar el registro (puede que no exista o ya fue eliminado).");
            }
            return eliminado; // Retorna true si se eliminó correctamente, false en caso contrario
        } catch (Exception e) {
            vista.mostrarMensaje("Error al eliminar el cliente: " + e.getMessage());
            return false; // Si ocurre un error, se retorna false
        }
    }

    public void mostrarClientes() {
        try {
            List<Cliente> clientes = repositorio.listarClientes();
            ivistaCliente.listar(clientes);
        } catch (Exception e) {
            vista.mostrarMensaje("Error al listar clientes: " + e.getMessage());
        }
    }

    /*
     * 2da Entrega Métodos de Acceso
     */
    // Mantenimiento del archivo de clientes
    public void compactarArchivo() {
        try {
            repositorio.compactarPorCopia();
            System.out.println("Archivo compactado exitosamente.");
            ventanaCompactarCliente.mostrarMensaje("¡Compactación exitosa!");
        } catch (Exception e) {
            ventanaCompactarCliente.mostrarMensaje("Error: " + e.getMessage());
        }
    }

    /*
     * 3era Entrega Métodos de Acceso
     */
}
