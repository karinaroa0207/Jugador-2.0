package edu.co.udistrital.model;

/**
 * Representa un nodo en la lista circular.
 * Cada nodo contiene el ID del jugador y una referencia al siguiente nodo.
 */
public class Nodo {
    private int idJugador;
    private Nodo next;

    /**
     * Constructor del nodo.
     * @param idJugador Identificador único del jugador.
     */
    public Nodo(int idJugador) {
        this.idJugador = idJugador;
        this.next = null;
    }

    public int getIdJugador() { 
        return idJugador; }

    public Nodo getNext() { 
        return next; }

    public void setNext(Nodo next) {
        this.next = next; }
}