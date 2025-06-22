package Vistas.VentanasCliente;
import Entidades.Cliente;
import Persistencia.ClienteRepositorio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import Controladores.ClienteControlador;

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


