package edu.co.udistrital.controller;

import edu.co.udistrital.model.ListaCircular;
import edu.co.udistrital.model.Nodo;
import edu.co.udistrital.view.JuegoView;
import java.util.Random;

/**
 * Controlador del juego. Coordina la interacción entre el Modelo (Datos) y la
 * Vista (Presentación).
 */
public class JuegoController {

    private ListaCircular lista;
    private Random dado;
    private JuegoView vista;

    /**
     * Constructor que inicializa los componentes del juego.
     *
     * @param n Número de jugadores iniciales.
     */
    public JuegoController(int n) {
        this.lista = new ListaCircular();
        this.dado = new Random();
    }

    public JuegoController(int n, JuegoView vista) {
        this.lista = new ListaCircular();
        this.dado = new Random();
        this.vista = vista;

        for (int i = 1; i <= n; i++) {
            lista.insertar(i);
        }

        vista.iniciarVentana(n); // abre la ventana con los jugadores
    }

    /**
     * Ejecuta la lógica principal del juego, iterando hasta que solo quede un
     * ganador.
     */
    public void ejecutarJuego() {
        Nodo actual = lista.getHead();
        Nodo anterior = obtenerAnterior(actual);

        while (lista.getSize() > 1) {
            int valorDado = dado.nextInt(6) + 1;

            vista.mostrarJugadorLanza(actual.getIdJugador(), valorDado);

            if (valorDado % 2 != 0) { // Regla: Impar = Elimina
                vista.mostrarEstadoJugador(actual.getIdJugador(), true, valorDado);

                // Mantenemos el nodo actual para avanzar después de eliminar
                Nodo temp = actual;
                actual = actual.getNext();
                lista.eliminar(anterior);

            } else { // Regla: Par = Se salva
                vista.mostrarEstadoJugador(actual.getIdJugador(), false, valorDado);
                anterior = actual;
                actual = actual.getNext();
            }
        }
        vista.mostrarGanador(lista.getHead().getIdJugador());
    }

    /**
     * Método auxiliar para encontrar el nodo anterior a uno dado. Se hace
     * porque la lista es simplemente enlazada.
     *
     * @param actual El nodo de referencia.
     * @return El nodo que apunta a 'actual'.
     */
    private Nodo obtenerAnterior(Nodo actual) {
        Nodo temp = actual;
        // Recorremos hasta que el siguiente sea el actual
        while (temp.getNext() != actual) {
            temp = temp.getNext();
        }
        return temp;
    }

}
