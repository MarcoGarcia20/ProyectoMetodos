package src.Estructuras;

public class NodoB {
    int m; // Orden del árbol (cantidad máxima de hijos)
    int n; // Número actual de claves en el nodo
    String[] claves; // Arreglo para almacenar las claves (m-1)
    Object[] datos; // Arreglo para almacenar los datos asociados a cada clave
    NodoB[] hijos; // Arreglo para los punteros a los nodos hijos (m)
    boolean hoja; // Es true si el nodo es una hoja

    public NodoB(int m, boolean hoja) {
        this.m = m;
        this.hoja = hoja;
        this.n = 0;
        this.claves = new String[m - 1];
        this.datos = new Object[m - 1];
        this.hijos = new NodoB[m];
    }

    // --- MÉTODOS PARA LA INSERCIÓN ---

    /**
     * Inserta una nueva clave en un nodo que NO está lleno.
     * Este método cumple con: "Al llegar las claves se colocan ordenadamente".
     */
    public void insertarNoLleno(String clave, Object dato) {
        int i = n - 1; // Empezamos desde la última clave

        if (hoja) {
            // Si es una hoja, encontramos la posición para la nueva clave
            // y movemos las claves mayores un lugar a la derecha.
            while (i >= 0 && claves[i].compareTo(clave) > 0) {
                claves[i + 1] = claves[i];
                datos[i + 1] = datos[i];
                i--;
            }
            // Insertamos la nueva clave en la posición encontrada.
            claves[i + 1] = clave;
            datos[i + 1] = dato;
            n = n + 1; // Incrementamos el número de claves
        } else {
            // Si no es una hoja, encontramos el hijo que va a recibir la nueva clave.
            while (i >= 0 && claves[i].compareTo(clave) > 0) {
                i--;
            }
            i++; // El hijo correcto es el que está a la derecha de la clave encontrada.

            // Si el hijo está lleno, lo dividimos ANTES de bajar.
            if (hijos[i].n == m - 1) {
                dividirHijo(i, hijos[i]); // "Se desdobla"

                // Después de dividir, la clave del medio sube a este nodo.
                // Decidimos a cuál de los dos nuevos hijos bajaremos.
                if (claves[i].compareTo(clave) < 0) {
                    i++;
                }
            }
            // Llamada recursiva para insertar en el hijo apropiado.
            hijos[i].insertarNoLleno(clave, dato);
        }
    }

    /**
     * Divide un hijo 'y' que está lleno.
     * 'i' es el índice de 'y' en el arreglo de hijos.
     * Este método cumple con: "Se desdobla", "Se reparten las claves" y "Se
     * promociona una clave".
     */
    public void dividirHijo(int i, NodoB y) {
        // 1. Crear un nuevo nodo 'z' que almacenará la mitad derecha de las claves de
        // 'y'.
        NodoB z = new NodoB(y.m, y.hoja);
        int t = m / 2; // Punto medio para la división
        z.n = t - 1;

        // 2. Copiar la segunda mitad de las claves y datos de 'y' a 'z'.
        for (int j = 0; j < t - 1; j++) {
            z.claves[j] = y.claves[j + t];
            z.datos[j] = y.datos[j + t];
        }

        // 3. Si 'y' no es una hoja, copiar también la segunda mitad de sus hijos.
        if (!y.hoja) {
            for (int j = 0; j < t; j++) {
                z.hijos[j] = y.hijos[j + t];
            }
        }

        // 4. Reducir el número de claves en 'y'.
        y.n = t - 1;

        // 5. Hacer espacio en este nodo para el nuevo hijo 'z'.
        for (int j = n; j >= i + 1; j--) {
            hijos[j + 1] = hijos[j];
        }
        hijos[i + 1] = z;

        // 6. Hacer espacio para la clave promovida.
        for (int j = n - 1; j >= i; j--) {
            claves[j + 1] = claves[j];
            datos[j + 1] = datos[j];
        }

        // 7. "Promocionar" la clave del medio de 'y' a este nodo.
        claves[i] = y.claves[t - 1];
        datos[i] = y.datos[t - 1];
        n = n + 1;
    }

    public void insertarEnHoja(String clave, Object dato) {
        int i = n - 1;
        while (i >= 0 && claves[i].compareTo(clave) > 0) {
            claves[i + 1] = claves[i];
            datos[i + 1] = datos[i];
            i--;
        }
        claves[i + 1] = clave;
        datos[i + 1] = dato;
        n++;
    }

    public void insertarEnInterno(String clave, Object dato, NodoB hijoDerecho) {
        int i = n - 1;
        while (i >= 0 && claves[i].compareTo(clave) > 0) {
            claves[i + 1] = claves[i];
            datos[i + 1] = datos[i];
            hijos[i + 2] = hijos[i + 1];
            i--;
        }
        claves[i + 1] = clave;
        datos[i + 1] = dato;
        hijos[i + 2] = hijoDerecho;
        n++;
    }

    /**
     * Divide este nodo (que está lleno) porque necesita insertar una nueva clave.
     * 
     * @param clave       La nueva clave a insertar.
     * @param dato        El dato de la nueva clave.
     * @param hijoDerecho El hijo derecho de la nueva clave (solo para nodos
     *                    internos).
     * @return Un objeto RespuestaSplit con la información de la promoción.
     */
    public RespuestaSplit dividir(String clave, Object dato, NodoB hijoDerecho) {
        // Crear arreglos temporales que contendrán todas las claves y hijos (los
        // actuales + el nuevo).
        String clavePromovida;
        Object datoPromovido;
        String[] tempClaves = new String[m];
        Object[] tempDatos = new Object[m];
        NodoB[] tempHijos = new NodoB[m + 1];

        // Copiar los elementos existentes y el nuevo elemento a los arreglos temporales
        // en orden.
        // (Esta lógica de fusión ordenada es compleja, la simplificaremos para
        // claridad)
        // ... Lógica para fusionar ordenadamente ...
        // Para simplificar, asumimos que ya tenemos una lista ordenada de 'm' claves.
        // En una implementación real, se insertaría la nueva clave en su posición
        // correcta.
        System.arraycopy(this.claves, 0, tempClaves, 0, n);
        System.arraycopy(this.datos, 0, tempDatos, 0, n);
        System.arraycopy(this.hijos, 0, tempHijos, 0, n + 1);

        // --- Lógica de División y Promoción ---
        int t = m / 2;

        int i = 0;
        while (i < n && claves[i].compareTo(clave) < 0) {
            i++;
        }

        System.arraycopy(this.claves, 0, tempClaves, 0, i);
        System.arraycopy(this.datos, 0, tempDatos, 0, i);
        System.arraycopy(this.hijos, 0, tempHijos, 0, i);

        tempClaves[i] = clave;
        tempDatos[i] = dato;
        tempHijos[i + 1] = hijoDerecho;

        System.arraycopy(this.claves, i, tempClaves, i + 1, n - i);
        System.arraycopy(this.datos, i, tempDatos, i + 1, n - i);
        System.arraycopy(this.hijos, i, tempHijos, i + 1, n - i + 1);

        clavePromovida = tempClaves[t];
        datoPromovido = tempDatos[t];

        NodoB nuevoNodo = new NodoB(m, this.hoja);
        nuevoNodo.n = m - 1 - t;

        this.n = t;
        for (int j = 0; j < m - 1; j++) {
            this.claves[j] = null;
            this.datos[j] = null;
            this.hijos[j] = null;
        }
        this.hijos[m - 1] = null;

        System.arraycopy(tempClaves, 0, this.claves, 0, t);
        System.arraycopy(tempDatos, 0, this.datos, 0, t);
        System.arraycopy(tempHijos, 0, this.hijos, 0, t + 1);

        System.arraycopy(tempClaves, t + 1, nuevoNodo.claves, 0, nuevoNodo.n);
        System.arraycopy(tempDatos, t + 1, nuevoNodo.datos, 0, nuevoNodo.n);
        System.arraycopy(tempHijos, t + 1, nuevoNodo.hijos, 0, nuevoNodo.n + 1);

        // La llamada al constructor ahora usa tu clase externa.
        return new RespuestaSplit(clavePromovida, datoPromovido, nuevoNodo);
    }

    public Object buscar(String clave) {
        // 1. Encontrar la posición 'i' donde podría estar la clave en este nodo.
        int i = 0;
        while (i < this.n && clave.compareTo(this.claves[i]) > 0) {
            i++;
        }

        // 2. Comprobar si encontramos la clave exacta en la posición 'i'.
        if (i < this.n && clave.equals(this.claves[i])) {
            // ¡Encontrado! Devolvemos el dato asociado.
            return this.datos[i];
        }

        // 3. Si hemos llegado a una hoja y no la encontramos, la clave no existe.
        if (this.hoja) {
            return null;
        }

        // 4. Si no es una hoja, continuar la búsqueda en el hijo apropiado.
        return this.hijos[i].buscar(clave);
    }
}
