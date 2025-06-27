package src.Vistas.VentanasCliente;
import src.Controladores.ClienteControlador;
import src.Entidades.Cliente;
import src.Persistencia.ClienteRepositorio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.*;
import java.util.HashSet;

public class VentanaConsultarCliente extends VentanaFormularioCliente{

    public VentanaConsultarCliente() {
        super("Consultar Cliente");
    }

    @Override
    protected void configurar() {
        
    }
}


