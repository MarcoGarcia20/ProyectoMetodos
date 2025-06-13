package Controladores;

import java.util.List;

import Entidades.Cliente;
import Persistencia.ClienteRepositorio;
import Vistas.VentanaFormularioCliente;

public class ClienteControlador {
    private ClienteRepositorio repositorio;
    private VentanaFormularioCliente vista;

    public ClienteControlador(ClienteRepositorio repositorio, VentanaFormularioCliente vista) {
        this.repositorio = repositorio;
        this.vista = vista;
        this.vista.setControlador(this);
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
            Cliente cliente = repositorio.buscarClientePorDNI(dni);
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
    public void eliminarCliente() {
        try {
            Cliente cliente = vista.obtenerClienteDesdeCampos();
            if (cliente == null) {
                vista.deshabilitarCampos();
            } else{
                repositorio.eliminarCliente(cliente.getDni());
                vista.limpiarCampos();
            } 
        } catch (Exception e) {
            vista.mostrarMensaje("Error al eliminar el cliente: " + e.getMessage());
        }
    }
    public void mostrarClientes() {
        try {
            List<Cliente> clientes = repositorio.listarClientes();
            
        } catch (Exception e) {
            vista.mostrarMensaje("Error al listar clientes: " + e.getMessage());
        }
}
}
