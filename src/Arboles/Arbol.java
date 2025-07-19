package src.Arboles;

public class Arbol {
    Nodo raiz;

    public Arbol() {
        this.raiz = null;
    }
    public Nodo getRaiz() {
        return raiz;
    }
    public void setRaiz(Nodo raiz) {
        this.raiz = raiz;
    }
    public boolean estaVacio() {
        return raiz == null;
    }
    public void insertar(Nodo nuevoNodo) {
        if (estaVacio()) {
            raiz = nuevoNodo;
        } else {
            insertarRecursivo(raiz, nuevoNodo);
        }
    }
    private void insertarRecursivo(Nodo nodoActual, Nodo nuevoNodo) {
        if (nuevoNodo.getClave().compareTo(nodoActual.getClave()) < 0) {
            if (nodoActual.getIzquierdo() == null) {
                nodoActual.setIzquierdo(nuevoNodo);
            } else {
                insertarRecursivo(nodoActual.getIzquierdo(), nuevoNodo);
            }
        } else {
            if (nodoActual.getDerecho() == null) {
                nodoActual.setDerecho(nuevoNodo);
            } else {
                insertarRecursivo(nodoActual.getDerecho(), nuevoNodo);
            }
        }
    }
    public Nodo buscar(String clave) {
        return buscarRecursivo(raiz, clave);
    }
    private Nodo buscarRecursivo(Nodo nodoActual, String clave) {
        if (nodoActual == null || nodoActual.getClave().equals(clave)) {
            return nodoActual;
        }
        if (clave.compareTo(nodoActual.getClave()) < 0) {
            return buscarRecursivo(nodoActual.getIzquierdo(), clave);
        } else {
            return buscarRecursivo(nodoActual.getDerecho(), clave);
        }
    }
    public void eliminar(String clave) {
        raiz = eliminarRecursivo(raiz, clave);
    }
    private Nodo eliminarRecursivo(Nodo nodoActual, String clave) {
        if (nodoActual == null) {
            return null;
        }
        if (clave.compareTo(nodoActual.getClave()) < 0) {
            nodoActual.setIzquierdo(eliminarRecursivo(nodoActual.getIzquierdo(), clave));
        } else if (clave.compareTo(nodoActual.getClave()) > 0) {
            nodoActual.setDerecho(eliminarRecursivo(nodoActual.getDerecho(), clave));
        } else {
            // Nodo encontrado
            if (nodoActual.getIzquierdo() == null) {
                return nodoActual.getDerecho();
            } else if (nodoActual.getDerecho() == null) {
                return nodoActual.getIzquierdo();
            }
            // Nodo con dos hijos
            Nodo sucesor = encontrarMinimo(nodoActual.getDerecho());
            nodoActual.setClave(sucesor.getClave());
            nodoActual.setDato(sucesor.getDato());
            nodoActual.setDerecho(eliminarRecursivo(nodoActual.getDerecho(), sucesor.getClave()));
        }
        return nodoActual;
    }
    private Nodo encontrarMinimo(Nodo nodo) {
        while (nodo.getIzquierdo() != null) {
            nodo = nodo.getIzquierdo();
        }
        return nodo;
    }
    public void recorrerInOrden() {
        recorrerInOrdenRecursivo(raiz);
    }
    private void recorrerInOrdenRecursivo(Nodo nodo) {
        if (nodo != null) {
            recorrerInOrdenRecursivo(nodo.getIzquierdo());
            System.out.println(nodo.getClave() + ": " + nodo.getDato());
            recorrerInOrdenRecursivo(nodo.getDerecho());
        }
    }
    public void recorrerPreOrden() {
        recorrerPreOrdenRecursivo(raiz);
    }
    private void recorrerPreOrdenRecursivo(Nodo nodo) {
        if (nodo != null) {
            System.out.println(nodo.getClave() + ": " + nodo.getDato());
            recorrerPreOrdenRecursivo(nodo.getIzquierdo());
            recorrerPreOrdenRecursivo(nodo.getDerecho());
        }
    }
    public void recorrerPostOrden() {
        recorrerPostOrdenRecursivo(raiz);
    }
    private void recorrerPostOrdenRecursivo(Nodo nodo) {
        if (nodo != null) {
            recorrerPostOrdenRecursivo(nodo.getIzquierdo());
            recorrerPostOrdenRecursivo(nodo.getDerecho());
            System.out.println(nodo.getClave() + ": " + nodo.getDato());
        }
    }

    public int contarNodos() {
        return contarNodosRecursivo(raiz);
    }
    private int contarNodosRecursivo(Nodo nodo) {
        if (nodo == null) {
            return 0;
        }
        return 1 + contarNodosRecursivo(nodo.getIzquierdo()) + contarNodosRecursivo(nodo.getDerecho());
    }
    public int altura() {
        return alturaRecursivo(raiz);
    }
    private int alturaRecursivo(Nodo nodo) {
        if (nodo == null) {
            return -1; // Altura de un árbol vacío es -1
        }
        int alturaIzquierda = alturaRecursivo(nodo.getIzquierdo());
        int alturaDerecha = alturaRecursivo(nodo.getDerecho());
        return 1 + Math.max(alturaIzquierda, alturaDerecha);
    }
}
