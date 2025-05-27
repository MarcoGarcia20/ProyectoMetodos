import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class ArchivoCliente {
    private static final String RUTA = "clientes.dat";
    
    public void agregar(Cliente c) {
        try (RandomAccessFile raf = new RandomAccessFile(RUTA, "rw")) {
            raf.seek(raf.length());
            c.escribir(raf);
        } catch (Exception e) {
            System.out.println("Error al agregar cliente: " + e.getMessage());
        }
    }

    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(RUTA, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                Cliente c = new Cliente();
                c.leer(raf);
                lista.add(c);
            }
        } catch (Exception e) {
            System.out.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }

    public void mostrar() {
        listar().forEach(System.out::println);
    }

    public Cliente buscarPorDni(String dni) {
        try (RandomAccessFile raf = new RandomAccessFile(RUTA, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                Cliente c = new Cliente();
                long pos = raf.getFilePointer();
                c.leer(raf);
                if (c.getDni().equals(dni)) return c;
            }
        } catch (Exception e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }
        return null;
    }

    public boolean modificar(Cliente nuevo) {
        try (RandomAccessFile raf = new RandomAccessFile(RUTA, "rw")) {
            while (raf.getFilePointer() < raf.length()) {
                long pos = raf.getFilePointer();
                Cliente c = new Cliente();
                c.leer(raf);
                if (c.getDni().equals(nuevo.getDni())) {
                    raf.seek(pos);
                    nuevo.escribir(raf);
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al modificar: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminar(String dni) {
        List<Cliente> lista = listar();
        boolean eliminado = lista.removeIf(c -> c.getDni().equals(dni));
        if (eliminado) {
            try (RandomAccessFile raf = new RandomAccessFile(RUTA, "rw")) {
                raf.setLength(0);
                for (Cliente c : lista) {
                    c.escribir(raf);
                }
                return true;
            } catch (Exception e) {
                System.out.println("Error al reescribir: " + e.getMessage());
            }
        }
        return false;
    }
}
