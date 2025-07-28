package src.Vistas.VentanaClienteArbol;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import src.Entidades.Cliente;
import src.Estructuras.ArbolB;

import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class VentanaClientes extends JFrame {

    // Estructura de datos principal
    private ArbolB arbolClientes;

    // Componentes de la UI
    private JTextField txtCantidad, txtBuscarDni;
    private JButton btnGenerar, btnBuscar, btnLimpiarArbol;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JTextArea areaLog;
    private Random rand = new Random();

    // Datos para la generación aleatoria (extraídos de tu clase Cliente)
    private String[] nombres = { "Juan", "Maria", "Luis", "Ana", "Carlos", "Sofia", "Pedro", "Laura", "Diego", "Elena", "Javier", "Isabel", "Andres", "Carmen", "Raul", "Patricia", "Miguel", "Lucia", "Alberto", "Sara" };
    private String[] apellidos = { "Garcia", "Rodriguez", "Martinez", "Lopez", "Perez", "Gonzalez", "Sanchez", "Romero", "Fernandez", "Torres", "Diaz", "Moreno", "Alvarez", "Jimenez", "Ruiz", "Hernandez", "Castro", "Ortiz" };

    public VentanaClientes() {
        // 1. Inicializar el Árbol B
        arbolClientes = new ArbolB(16);

        // 2. Configuración de la ventana
        setTitle("Gestión de Clientes con Árbol B (Orden 16)");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 3. Crear y añadir paneles
        add(crearPanelControles(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelLog(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelControles() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Operaciones"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Panel de Generación ---
        JPanel panelGenerar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelGenerar.add(new JLabel("Cantidad a generar:"));
        txtCantidad = new JTextField("100", 5);
        panelGenerar.add(txtCantidad);
        btnGenerar = new JButton("Generar e Insertar Clientes");
        panelGenerar.add(btnGenerar);
        btnLimpiarArbol = new JButton("Limpiar Todo");
        panelGenerar.add(btnLimpiarArbol);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0;
        panel.add(panelGenerar, gbc);

        // --- Panel de Búsqueda ---
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBuscar.add(new JLabel("Buscar por DNI:"));
        txtBuscarDni = new JTextField(10);
        panelBuscar.add(txtBuscarDni);
        btnBuscar = new JButton("Buscar Cliente");
        panelBuscar.add(btnBuscar);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(panelBuscar, gbc);

        // --- Action Listeners ---
        btnGenerar.addActionListener(e -> generarEInsertarClientes());
        btnBuscar.addActionListener(e -> buscarCliente());
        btnLimpiarArbol.addActionListener(e -> limpiarTodo());

        return panel;
    }

    private JScrollPane crearPanelTabla() {
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("DNI");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Edad");
        modeloTabla.addColumn("Correo");
        modeloTabla.addColumn("Suscripción");
        modeloTabla.addColumn("Celular");
        tablaClientes = new JTable(modeloTabla);
        return new JScrollPane(tablaClientes);
    }

    private JScrollPane crearPanelLog() {
        areaLog = new JTextArea(5, 40);
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(areaLog);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log de Operaciones"));
        return scrollPane;
    }

    // --- LÓGICA DE LAS OPERACIONES ---

    private void generarEInsertarClientes() {
        try {
            int cantidad = Integer.parseInt(txtCantidad.getText());
            if (cantidad <= 0) {
                log("Por favor, ingrese un número positivo.");
                return;
            }

            log("Iniciando generación de " + cantidad + " clientes aleatorios...");
            long tiempoInicio = System.nanoTime();
            int insertados = 0;

            for (int i = 0; i < cantidad; i++) {
                Cliente nuevoCliente = generarClienteAleatorio();
                // El Árbol B no permite claves duplicadas. Si buscar() devuelve null, la clave no existe.
                if (arbolClientes.buscar(nuevoCliente.getDni()) == null) {
                    arbolClientes.insertar(nuevoCliente.getDni(), nuevoCliente);
                    insertados++;
                }
            }
            
            long tiempoFin = System.nanoTime();
            long duracionMs = TimeUnit.NANOSECONDS.toMillis(tiempoFin - tiempoInicio);

            log("Proceso completado en " + duracionMs + " ms.");
            log("Se insertaron " + insertados + " clientes únicos en el Árbol B.");
            
            actualizarTabla();

        } catch (NumberFormatException ex) {
            log("Error: La cantidad debe ser un número válido.");
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número válido en la cantidad.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarCliente() {
        String dniBusqueda = txtBuscarDni.getText().trim();
        if (dniBusqueda.isEmpty() || !dniBusqueda.matches("\\d{8}")) {
            log("Error: DNI inválido. Debe contener 8 dígitos.");
            JOptionPane.showMessageDialog(this, "El DNI debe tener 8 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        log("Buscando cliente con DNI: " + dniBusqueda + "...");
        long tiempoInicio = System.nanoTime();
        Object resultado = arbolClientes.buscar(dniBusqueda);
        long tiempoFin = System.nanoTime();

        if (resultado instanceof Cliente) {
            Cliente clienteEncontrado = (Cliente) resultado;
            log("¡Cliente encontrado en " + (tiempoFin - tiempoInicio) + " ns! Nombre: " + clienteEncontrado.getNombre());
            // Seleccionar la fila en la tabla
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                if (modeloTabla.getValueAt(i, 0).equals(dniBusqueda)) {
                    tablaClientes.setRowSelectionInterval(i, i);
                    tablaClientes.scrollRectToVisible(tablaClientes.getCellRect(i, 0, true));
                    break;
                }
            }
        } else {
            log("Cliente con DNI " + dniBusqueda + " no fue encontrado en el árbol.");
        }
    }

    private void limpiarTodo() {
        arbolClientes = new ArbolB(16); // Crea una nueva instancia vacía
        modeloTabla.setRowCount(0); // Limpia la tabla visual
        log("Árbol B y tabla han sido reseteados.");
    }
    
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        List<String> listaClaves = arbolClientes.listarInOrden();
        log("Actualizando tabla con " + listaClaves.size() + " registros del árbol...");
        for (String item : listaClaves) {
            String dni = item.split(":")[0]; // Extraemos la clave (DNI)
            Object obj = arbolClientes.buscar(dni);
            if (obj instanceof Cliente) {
                Cliente c = (Cliente) obj;
                modeloTabla.addRow(new Object[]{
                        c.getDni(),
                        c.getNombre(),
                        c.getEdad(),
                        c.getCorreo(),
                        c.getIniSus().toString(),
                        c.getCelular()
                });
            }
        }
    }

    private Cliente generarClienteAleatorio() {
        Cliente c = new Cliente();
        c.setDni(String.format("%08d", rand.nextInt(90_000_000) + 10_000_000)); // DNI entre 10,000,000 y 99,999,999
        c.setEdad((byte) (18 + rand.nextInt(53))); // 18 a 70 años
        
        String nombre = nombres[rand.nextInt(nombres.length)];
        String apellido = apellidos[rand.nextInt(apellidos.length)];
        c.setNombre((nombre + " " + apellido));

        String correoBase = (nombre.toLowerCase() + "." + apellido.toLowerCase()).replaceAll("\\s+", "");
        c.setCorreo(correoBase + rand.nextInt(100) + "@example.com");

        LocalDate fechaInicio = LocalDate.of(2018, 1, 1);
        LocalDate fechaFin = LocalDate.now();
        long diasEntre = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        c.setIniSus(fechaInicio.plusDays(rand.nextLong(diasEntre + 1)));

        c.setCelular(String.format("9%08d", rand.nextInt(100_000_000)));
        
        return c;
    }

    private void log(String mensaje) {
        areaLog.append(mensaje + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaClientes ventana = new VentanaClientes();
            ventana.setVisible(true);
        });
    }
}