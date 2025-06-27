package src.Vistas;

import java.util.List;

import src.Entidades.Cliente;

public interface IVista<T> {
    void mostrarMensaje(String mensaje);
    void listar(List<T> entidades);
}
