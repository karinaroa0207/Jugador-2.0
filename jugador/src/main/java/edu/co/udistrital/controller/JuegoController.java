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
     */
    public JuegoController() {
        this.vista = new JuegoView();
        this.lista = new ListaCircular();
        this.dado = new Random();

        int n = vista.pedirNumeroJugadores();
        for (int i = 1; i <= n; i++) {
            lista.insertar(i);
        }
        vista.iniciarVentana(n); // abre la ventana con los jugadores
        ejecutarJuego();
    }

    /**
     * Ejecuta la lógica principal del juego, iterando hasta que solo quede un
     * ganador.
     */
    public void ejecutarJuego() {
        Nodo actual = lista.getHead();
        //Obtenemos nodo anterior para eliminar más facil actual
        Nodo anterior = lista.obtenerAnterior(actual);

        while (lista.getSize() > 1) {
            int valorDado = dado.nextInt(6) + 1;
            
            vista.mostrarJugadorLanza(lista.getIdNodo(actual), valorDado);

            if (valorDado % 2 != 0) { // Regla: Impar = Elimina
                vista.mostrarEstadoJugador(lista.getIdNodo(actual), true, valorDado);

                // Mantenemos el nodo actual para avanzar después de eliminar
                actual = lista.getNext(actual);
                lista.eliminarSiguiente(anterior);

            } else { // Regla: Par = Se salva
                vista.mostrarEstadoJugador(lista.getIdNodo(actual), false, valorDado);
                anterior = actual;
                actual = lista.getNext(actual);
            }            
        }
        vista.mostrarGanador(lista.getIdNodo(lista.getHead()));
    }
}
