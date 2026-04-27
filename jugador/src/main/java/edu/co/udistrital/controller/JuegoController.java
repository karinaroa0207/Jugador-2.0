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
    private ControlVista CVista;

    /**
     * Constructor que inicializa los componentes del juego.
     */
    public JuegoController() {
        this.CVista = new ControlVista(this);
        this.lista = new ListaCircular();
        this.dado = new Random();
        CVista.pedirNumeroJugadores();
    }

    /**
     * Ejecuta la lógica principal del juego, iterando hasta que solo quede un
     * ganador.
     *
     * @param n numero de jugadores
     */
    public void ejecutarJuego(int n) {
        for (int i = 1; i <= n; i++) {
            lista.insertar(i);
        }

        Nodo actual = lista.getHead();
        //Obtenemos nodo anterior para eliminar más facil actual
        Nodo anterior = lista.obtenerAnterior(actual);

        while (lista.getSize() > 1) {
            int valorDado = dado.nextInt(6) + 1;

            CVista.mostrarLanzamiento(lista.getIdNodo(actual), valorDado);

            if (valorDado % 2 != 0) { // Regla: Impar = Elimina
                CVista.mostrarEstadoJugador(lista.getIdNodo(actual), true, valorDado);

                // Mantenemos el nodo actual para avanzar después de eliminar
                actual = lista.getNext(actual);
                lista.eliminarSiguiente(anterior);

            } else { // Regla: Par = Se salva
                CVista.mostrarEstadoJugador(lista.getIdNodo(actual), false, valorDado);
                anterior = actual;
                actual = lista.getNext(actual);
            }
        }
        CVista.mostrarGanador(lista.getIdNodo(lista.getHead()));
    }
    
    public void reiniciar() {        
        lista.eliminarSiguiente(lista.getHead());
        CVista.pedirNumeroJugadores();
    }
}
