package Controladores;

import java.util.List;

import Entidades.Cliente;
import Persistencia.ClienteRepositorio;
import Vistas.IVistaCliente;
import Vistas.VentanasCliente.VentanaFormularioCliente;
import Vistas.VentanasCliente.VentanaListarCliente;

public class ClienteControlador {
    private ClienteRepositorio repositorio;
    private VentanaFormularioCliente vista;
    private IVistaCliente ivistaCliente;

    public ClienteControlador(ClienteRepositorio repositorio, VentanaFormularioCliente vista) {
        this.repositorio = repositorio;
        this.vista = vista;
        this.vista.setControlador(this);
    }
    public ClienteControlador(ClienteRepositorio repositorio, IVistaCliente vistalistar) {
        this.repositorio = repositorio;
        this.ivistaCliente = vistalistar;
    }

    //Método invocado cuando se presiona el botón de Agregar Cliente
    public void ingresarCliente() {
        try {
            Cliente cliente = vista.obtenerClienteDesdeCampos();
            //cliente.validar();
            repositorio.ingresarCliente(cliente);
        } catch (Exception e) {
            vista.mostrarMensaje("Error al guardar el cliente: " + e.getMessage());
        }
    }
    //Método invocado cuando se presiona el botón de Consultar Cliente
    public Cliente consultarCliente(String dni) {
        try {
            Cliente cliente = repositorio.busquedaSecuencial(dni);
            return cliente;
        } catch (Exception e) {
            vista.mostrarMensaje("Error al buscar el cliente: " + e.getMessage());
            return null;
        }
    }
    //Método invocado cuando se presiona el botón de Modificar Cliente
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
    //Método invocado cuando se presiona el botón de Eliminar Cliente
    public boolean eliminarCliente() {
        try {
            Cliente cliente = vista.obtenerClienteDesdeCampos();
            if (cliente == null) {
                vista.mostrarMensaje("Cliente no encontrado.");
                vista.deshabilitarCampos();
                return false; // Si no se encuentra el cliente, no se puede eliminar
            } else{
                boolean eliminado = repositorio.eliminarCliente(cliente.getDni());
                if (eliminado) {
                vista.limpiarCampos();
                vista.deshabilitarCampos();
                }
                return eliminado; // true si se eliminó, false si no (ya estaba inactivo o no existía)
            }
        } catch (Exception e) {
            vista.mostrarMensaje("Error al eliminar el cliente: " + e.getMessage());
            return false; // Si ocurre un error, se retorna false
        }
    }
    public void mostrarClientes() {
        try {
            List<Cliente> clientes = repositorio.listarClientes();
            ivistaCliente.listarClientes(clientes);
            
        } catch (Exception e) {
            vista.mostrarMensaje("Error al listar clientes: " + e.getMessage());
        }
    }
}
