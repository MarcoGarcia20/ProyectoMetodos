package src.Estructuras;

import java.util.ArrayList;
import java.util.List;

public class ArbolB {
    private NodoB raiz;
    private int m; // Orden del árbol

    // Constructor del ArbolB
    public ArbolB(int m) {
        this.m = m;
        this.raiz = new NodoB(m, true); // La raíz empieza siendo una hoja
    }

    public void insertar(String clave, Object dato) {
        NodoB r = raiz;

        // Si la raíz está llena, el árbol debe crecer en altura.
        if (r.n == m - 1) {
            // 1. Crear una nueva raíz.
            NodoB nuevaRaiz = new NodoB(m, false); // La nueva raíz no es una hoja.
            nuevaRaiz.hijos[0] = r; // La antigua raíz se convierte en su primer hijo.

            // 2. Dividir la antigua raíz (que ahora es un hijo).
            nuevaRaiz.dividirHijo(0, r);

            // 3. Decidir a cuál de los dos hijos irá la nueva clave.
            int i = 0;
            if (nuevaRaiz.claves[0].compareTo(clave) < 0) {
                i++;
            }
            nuevaRaiz.hijos[i].insertarNoLleno(clave, dato);

            // 4. Asignar la nueva raíz al árbol.
            this.raiz = nuevaRaiz;
        } else {
            // Si la raíz no está llena, simplemente insertamos.
            r.insertarNoLleno(clave, dato);
        }
    }

    public void insercionEnArbolB(String clave, Object dato) {
        // Se llama al método recursivo comenzando desde la raíz.
        RespuestaSplit resp = insertarRecursivo(raiz, clave, dato);

        // Si la llamada recursiva a la raíz generó una promoción...
        // esto significa que la raíz original se dividió.
        if (resp != null) {
            // "Generar el nodo superior (si es necesario)"
            // Se crea una nueva raíz.
            NodoB nuevaRaiz = new NodoB(m, false);
            nuevaRaiz.claves[0] = resp.clavePromovida;
            nuevaRaiz.datos[0] = resp.datoPromovido;
            nuevaRaiz.hijos[0] = this.raiz; // El hijo izquierdo es la antigua raíz.
            nuevaRaiz.hijos[1] = resp.nuevoNodo; // El hijo derecho es el nuevo nodo.
            nuevaRaiz.n = 1;
            this.raiz = nuevaRaiz;
        }
    }

    private RespuestaSplit insertarRecursivo(NodoB nodoActual, String clave, Object dato) {
        // CASO BASE: Si el nodo actual es una hoja, intentamos insertar aquí.
        if (nodoActual.hoja) {
            // "Todas las inserciones en las Hojas"
            if (nodoActual.n < m - 1) {
                // Si hay espacio, insertamos la clave y el dato ordenadamente.
                nodoActual.insertarEnHoja(clave, dato);
                return null; // No hay división, retornamos null.
            } else {
                // "Si no cabe => División y Promoción"
                // Si la hoja está llena, la dividimos.
                return nodoActual.dividir(clave, dato, null);
            }
        }
        // CASO RECURSIVO: Si no es una hoja, buscamos el hijo correcto para descender.
        else {
            // "mientras i<nodo.contclaves && cl_ins > nodo.clave[i]"
            int i = 0;
            while (i < nodoActual.n && clave.compareTo(nodoActual.claves[i]) > 0) {
                i++;
            }

            // "resp = Ins_B(cl_ins, nodo.pr[i])"
            // Llamada recursiva hacia el hijo apropiado.
            RespuestaSplit resp = insertarRecursivo(nodoActual.hijos[i], clave, dato);

            // "Si (resp.punt != 0) =>" (En nuestro caso, si resp no es null)
            // Si el hijo se dividió, debemos insertar la clave promovida en este nodo.
            if (resp != null) {
                // Si hay espacio en el nodo actual...
                if (nodoActual.n < m - 1) {
                    // "Insertar clave en nodo actual"
                    nodoActual.insertarEnInterno(resp.clavePromovida, resp.datoPromovido, resp.nuevoNodo);
                    return null; // La promoción se manejó, no es necesario seguir subiendo.
                } else {
                    // "Si no cabe => División y Promoción"
                    // Si este nodo también está lleno, lo dividimos.
                    return nodoActual.dividir(resp.clavePromovida, resp.datoPromovido, resp.nuevoNodo);
                }
            }
            return null; // No hubo promoción desde abajo.
        }
    }

    /**
     * Busca una clave en el árbol B.
     * 
     * @param clave La clave a buscar.
     * @return El objeto dato asociado a la clave, o null si no se encuentra.
     */
    public Object buscar(String clave) {
        return buscarRecursivo(raiz, clave);
    }

    private Object buscarRecursivo(NodoB nodoActual, String clave) {
        // 1. Encontrar la posición 'i' en el nodo actual donde podría estar la clave.
        int i = 0;
        while (i < nodoActual.n && clave.compareTo(nodoActual.claves[i]) > 0) {
            i++;
        }

        // 2. Verificar si encontramos la clave en la posición 'i'.
        if (i < nodoActual.n && clave.equals(nodoActual.claves[i])) {
            return nodoActual.datos[i]; // ¡Clave encontrada!
        }

        // 3. Si es una hoja y no la encontramos, la clave no existe.
        if (nodoActual.hoja) {
            return null;
        }

        // 4. Si no es una hoja, continuar la búsqueda en el hijo apropiado.
        return buscarRecursivo(nodoActual.hijos[i], clave);
    }

        public Object busquedaEnArbol(String clave) {
            // 1. "Obtener raíz del árbol B."
            if (this.raiz == null) {
                return null;
            }

            // 2. "Hacer pos = búsqueda en el árbol B."
            // Se delega la búsqueda a la raíz. El objeto que devuelve es nuestro "pos".
            Object datoEncontrado = this.raiz.buscar(clave);

            // 3. "Si lo encontró => ..."
            // En nuestro caso, si 'datoEncontrado' no es null, lo encontramos.
            // Los pasos de "Calcular dirección", "Posicionar en archivo" y "Leer registro"
            // son abstractos en nuestra implementación, ya que tenemos el dato
            // directamente.

            // 4. "Retornar pos."
            return datoEncontrado;
        }

    public List<String> listarInOrden() {
        List<String> resultado = new ArrayList<>();
        if (this.raiz != null) {
            listarInOrdenRecursivo(this.raiz, resultado);
        }
        return resultado;
    }

    private void listarInOrdenRecursivo(NodoB nodo, List<String> resultado) {
        // --- "Si (NRR_nodo != -1)" ---
        // (La comprobación de null se hace antes de la primera llamada)

        // --- "Para (i = 0; i < nodo.contclaves; i = i + 1)" ---
        for (int i = 0; i < nodo.n; i++) {
            // --- "Recorrer en In-orden el subárbol apuntado por nodo.pr[i]." ---
            // Si no somos una hoja, visitamos el hijo que está a la IZQUIERDA de la clave
            // actual.
            if (!nodo.hoja) {
                listarInOrdenRecursivo(nodo.hijos[i], resultado);
            }

            // --- "Procesar nodo.clave[i]." ---
            // "recuperar registro de datos e incorporar al listado"
            // Visitamos/procesamos la clave actual.
            resultado.add(nodo.claves[i] + ": " + nodo.datos[i].toString());
        }

        // --- "Recorrer en In-orden subárbol apuntado por nodo.pr[i]." ---
        // Después del bucle, 'i' es igual a 'nodo.n'.
        // Visitamos el ÚLTIMO hijo (el que está a la DERECHA de la última clave).
        if (!nodo.hoja) {
            listarInOrdenRecursivo(nodo.hijos[nodo.n], resultado);
        }
    }
}
