package src.Estructuras;

/**
 * Representa la respuesta de una operación de inserción que puede resultar en una división (split).
 * Equivale al objeto "resp" de la diapositiva.
 */
public class RespuestaSplit {
    String clavePromovida;   // "Clave a promocionar"
    Object datoPromovido;    // "Referencia" asociada a la clave
    NodoB nuevoNodo;         // "Puntero al nuevo nodo"

    /**
     * Constructor para crear una respuesta cuando ocurre una división.
     * @param clavePromovida La clave que sube al padre.
     * @param datoPromovido El dato asociado a la clave promovida.
     * @param nuevoNodo El nuevo nodo hermano creado durante la división.
     */
    public RespuestaSplit(String clavePromovida, Object datoPromovido, NodoB nuevoNodo) {
        this.clavePromovida = clavePromovida;
        this.datoPromovido = datoPromovido;
        this.nuevoNodo = nuevoNodo;
    }
}