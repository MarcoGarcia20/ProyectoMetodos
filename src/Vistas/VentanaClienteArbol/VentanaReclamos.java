package src.Vistas.VentanaClienteArbol;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import src.Entidades.Reclamo; // Importamos la entidad Reclamo
import src.Estructuras.ArbolB;

import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class VentanaReclamos extends JFrame {

    // Estructura de datos principal
    private ArbolB arbolReclamos;

    // Componentes de la UI
    private JTextField txtCantidad, txtBuscarId;
    private JButton btnGenerar, btnBuscar, btnLimpiarArbol;
    private JTable tablaReclamos;
    private DefaultTableModel modeloTabla;
    private JTextArea areaLog;
    private Random rand = new Random();

    // Datos para la generación aleatoria (extraídos de tu clase Reclamo)
    private String[] descripciones = {
            "Corte de servicio inesperado", "Facturacion erronea", "Problemas de conexion",
            "Maltrato de operador", "Cobro por servicio no solicitado", "Llamadas interrumpidas", "Internet lento"
    };
    private String[] soluciones = {
            "Revisado por tecnico", "Reembolso aplicado", "En investigacion",
            "Se requiere mas informacion", "Caso cerrado", "Derivado a soporte", "Cliente no contactado"
    };

    public VentanaReclamos() {
        // 1. Inicializar el Árbol B
        arbolReclamos = new ArbolB(16); // Mantenemos el mismo orden para comparar

        // 2. Configuración de la ventana
        setTitle("Gestión de Reclamos con Árbol B (Orden 16)");
        setSize(950, 700); // Aumenté un poco el ancho por si acaso
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 3. Crear y añadir paneles
        add(crearPanelControles(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelLog(), BorderLayout.SOUTH);
    }

    // ===== MÉTODO CORREGIDO =====
    private JPanel crearPanelControles() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Operaciones de Reclamos"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 0; // Todos los componentes estarán en la misma fila (y=0)

        // --- Componentes de Generación (Alineados a la Izquierda) ---
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        panel.add(new JLabel("Cantidad a generar:"), gbc);

        gbc.gridx = 1;
        txtCantidad = new JTextField("100", 5);
        panel.add(txtCantidad, gbc);

        gbc.gridx = 2;
        btnGenerar = new JButton("Generar e Insertar Reclamos");
        panel.add(btnGenerar, gbc);

        gbc.gridx = 3;
        btnLimpiarArbol = new JButton("Limpiar Todo");
        panel.add(btnLimpiarArbol, gbc);

        // --- Componente "Pegamento" ---
        // Este es el truco: un componente invisible que ocupa todo el espacio extra.
        gbc.gridx = 4;
        gbc.weightx = 1.0; // Le asignamos todo el "peso" horizontal.
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JLabel(""), gbc); // Un label vacío es suficiente.
        gbc.weightx = 0.0; // Reseteamos el peso para los siguientes componentes.
        gbc.fill = GridBagConstraints.NONE;

        // --- Componentes de Búsqueda (Alineados a la Derecha) ---
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 5;
        panel.add(new JLabel("Buscar por ID Reclamo:"), gbc);

        gbc.gridx = 6;
        txtBuscarId = new JTextField(10);
        panel.add(txtBuscarId, gbc);

        gbc.gridx = 7;
        btnBuscar = new JButton("Buscar Reclamo");
        panel.add(btnBuscar, gbc);

        // --- Action Listeners ---
        btnGenerar.addActionListener(e -> generarEInsertarReclamos());
        btnBuscar.addActionListener(e -> buscarReclamo());
        btnLimpiarArbol.addActionListener(e -> limpiarTodo());

        return panel;
    }
    // ===========================

    private JScrollPane crearPanelTabla() {
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID Reclamo");
        modeloTabla.addColumn("DNI Cliente");
        modeloTabla.addColumn("N° Línea");
        modeloTabla.addColumn("Descripción");
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Estado");
        modeloTabla.addColumn("Solución");
        tablaReclamos = new JTable(modeloTabla);
        return new JScrollPane(tablaReclamos);
    }

    private JScrollPane crearPanelLog() {
        areaLog = new JTextArea(5, 40);
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(areaLog);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log de Operaciones"));
        return scrollPane;
    }

    private void generarEInsertarReclamos() {
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) {
                log("Por favor, ingrese un número positivo.");
                return;
            }
            if (cantidad > 7_000_000) {
                JOptionPane.showMessageDialog(this, "Generar tantos IDs únicos puede tardar mucho tiempo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
            log("Iniciando generación de " + cantidad + " reclamos únicos...");
            long tiempoInicio = System.nanoTime();
            int insertados = 0;
            int intentos = 0;
            while (insertados < cantidad) {
                Reclamo nuevoReclamo = generarReclamoAleatorio();
                if (arbolReclamos.buscar(nuevoReclamo.getIdReclamo()) == null) {
                    arbolReclamos.insertar(nuevoReclamo.getIdReclamo(), nuevoReclamo);
                    insertados++;
                }
                intentos++;
            }
            long tiempoFin = System.nanoTime();
            long duracionMs = TimeUnit.NANOSECONDS.toMillis(tiempoFin - tiempoInicio);
            log("Proceso completado en " + duracionMs + " ms.");
            log("Se insertaron " + insertados + " reclamos únicos en el Árbol B.");
            log("(Se necesitaron " + intentos + " intentos para generar " + insertados + " IDs únicos)");
            actualizarTabla();
        } catch (NumberFormatException ex) {
            log("Error: La cantidad debe ser un número válido.");
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido en la cantidad.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarReclamo() {
        String idBusqueda = txtBuscarId.getText().trim();
        if (idBusqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El ID de reclamo no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        log("Buscando reclamo con ID: " + idBusqueda + "...");
        long tiempoInicio = System.nanoTime();
        Object resultado = arbolReclamos.buscar(idBusqueda);
        long tiempoFin = System.nanoTime();
        long duracionNs = tiempoFin - tiempoInicio;
        if (resultado instanceof Reclamo) {
            Reclamo reclamoEncontrado = (Reclamo) resultado;
            log("¡Reclamo encontrado en " + duracionNs + " ns! Descripción: " + reclamoEncontrado.getDescripcion());
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                if (modeloTabla.getValueAt(i, 0).equals(idBusqueda)) {
                    tablaReclamos.setRowSelectionInterval(i, i);
                    tablaReclamos.scrollRectToVisible(tablaReclamos.getCellRect(i, 0, true));
                    break;
                }
            }
        } else {
            log("Reclamo con ID " + idBusqueda + " no fue encontrado en el árbol.");
        }
    }

    private void limpiarTodo() {
        arbolReclamos = new ArbolB(16);
        modeloTabla.setRowCount(0);
        log("Árbol B de Reclamos y tabla han sido reseteados.");
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<String> listaClaves = arbolReclamos.listarInOrden();
        log("Actualizando tabla con " + listaClaves.size() + " registros del árbol de reclamos...");
        for (String item : listaClaves) {
            String idReclamo = item.split(":")[0];
            Object obj = arbolReclamos.buscar(idReclamo);
            if (obj instanceof Reclamo) {
                Reclamo r = (Reclamo) obj;
                modeloTabla.addRow(new Object[]{
                        r.getIdReclamo(),
                        r.getDniCliente(),
                        r.getNumeroLineaCliente(),
                        r.getDescripcion(),
                        r.getFecha(),
                        (r.getEstado() != null && r.getEstado() ? "Resuelto" : "Pendiente"),
                        r.getSolucion()
                });
            }
        }
    }

    private Reclamo generarReclamoAleatorio() {
        Reclamo r = new Reclamo();
        r.setIdReclamo("R" + String.format("%07d", rand.nextInt(10_000_000)));
        r.setDniCliente(String.format("%08d", rand.nextInt(90_000_000) + 10_000_000));
        r.setNumeroLineaCliente("9" + String.format("%08d", rand.nextInt(100_000_000)));
        r.setDescripcion(descripciones[rand.nextInt(descripciones.length)]);
        LocalDate fechaInicio = LocalDate.of(2022, 1, 1);
        LocalDate fechaFin = LocalDate.now();
        long diasEntre = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        r.setFecha(fechaInicio.plusDays(rand.nextLong(diasEntre + 1)));
        r.setEstado(rand.nextBoolean());
        r.setSolucion(soluciones[rand.nextInt(soluciones.length)]);
        return r;
    }

    private void log(String mensaje) {
        areaLog.append(mensaje + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaReclamos ventana = new VentanaReclamos();
            ventana.setVisible(true);
        });
    }
}