package Ventanas;

import java.time.LocalDate;

import Entidades.Cliente;

public class ValidarCliente implements Validador<Cliente> {

    @Override
    public void validar(Cliente entidad) throws Exception {
        if (entidad == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        validarDNI(entidad.getDni());
        validarNombre(entidad.getNombre());
        validarEdad(String.valueOf(entidad.getEdad()));
        validarCorreo(entidad.getCorreo());
        validarCelular(entidad.getCelular());
        validarFechaInicio(entidad.getIniSus().toString());
    }
    
    private String validarDNI(String dni) throws Exception {
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new IllegalArgumentException("DNI debe tener 8 dígitos");
        }
        return dni;
    }

    private int validarEdad(String edadStr) throws Exception {
        if (edadStr == null) {
            throw new IllegalArgumentException("Edad no puede ser nula");
        }
        try {
            int edad = Integer.parseInt(edadStr.trim());
            if (edad < 18 || edad > 100)
                throw new IllegalArgumentException("Edad debe ser un número entre 18-100");
            return edad;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Edad debe ser un número entre 18-100");
        }
    }

    private String validarNombre(String nombre) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre no puede estar vacío");
        }
        if (nombre.trim().length() > 20) {
            throw new IllegalArgumentException("Nombre máximo 20 caracteres");
        }
        return nombre.trim();
    }

    private String validarCorreo(String correo) throws Exception {
        if (correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("Correo no puede estar vacío");
        }
        if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Correo no válido");
        }
        return correo.trim();
    }

    private String validarCelular(String celular) throws Exception {
        if (celular == null || !celular.matches("\\d{9}")) {
            throw new IllegalArgumentException("Celular debe tener 9 dígitos");
        }
        return celular;
    }

    private LocalDate validarFechaInicio(String fechaInicio) throws Exception {
        if (fechaInicio == null || fechaInicio.trim().isEmpty()) {
            throw new IllegalArgumentException("Fecha de inicio no puede estar vacía");
        }
        try {
            return LocalDate.parse(fechaInicio.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Fecha de inicio debe tener formato yyyy-MM-dd");
        }
    }
}
