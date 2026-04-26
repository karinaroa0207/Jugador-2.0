package edu.co.udistrital.model;

/**
 * Gestiona la estructura de datos de una Lista Circular Simplemente Enlazada.
 */
public class ListaCircular {

    private Nodo head;
    private Nodo tail;
    private int size;

    public ListaCircular() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Inserta un nuevo jugador al final de la lista. Mantiene la propiedad
     * circular haciendo que el tail apunte siempre al head.
     *
     * @param id Identificador del jugador a insertar.
     */
    public void insertar(int id) {
        Nodo nuevo = new Nodo(id);
        if (head == null) {
            head = nuevo;
            tail = nuevo;
            tail.setNext(head);
        } else {
            tail.setNext(nuevo);
            tail = nuevo;
            tail.setNext(head);
        }
        size++;
    }

    /**
     * Elimina el nodo que sigue al nodo proporcionado (el anterior).
     *
     * @param anterior El nodo previo al que se desea eliminar.
     */
    public void eliminar(Nodo anterior) {
        if (size == 0) {
            return;
        }

        if (size == 1) {
            head = null;
            tail = null;
        } else {
            Nodo aEliminar = anterior.getNext();
            anterior.setNext(aEliminar.getNext());

            // Si eliminamos la cabeza, debemos actualizar el head y el tail
            if (aEliminar == head) {
                head = anterior.getNext();
            }
            // Si eliminamos la cola, debemos actualizar el tail
            if (aEliminar == tail) {
                tail = anterior;
            }
        }
        size--;
    }

    public Nodo getHead() {
        return head;
    }

    public int getSize() {
        return size;
    }
}
