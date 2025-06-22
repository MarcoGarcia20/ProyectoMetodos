package Vistas;

import java.util.List;

import Entidades.Cliente;

public interface IVistaCliente {
    void mostrarMensaje(String mensaje);
    void listarClientes(List<Cliente> clientes);
}
