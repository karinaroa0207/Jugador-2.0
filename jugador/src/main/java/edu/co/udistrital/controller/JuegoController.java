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
    }

    /**
     * Ejecuta la lógica principal del juego, iterando hasta que solo quede un
     * ganador.
     */
    public void ejecutarJuego() {
        int n = leerNumeroInt("Escriba número de jugadores: ");
        for (int i = 1; i <= n; i++) {
            lista.insertar(i);
        }
        
        Nodo actual = lista.getHead();
        Nodo anterior = lista.obtenerAnterior(actual);

        while (lista.getSize() > 1) {
            vista.mostrarMensaje(lista.toString());
            int valorDado = dado.nextInt(6) + 1;
            
            vista.mostrarJugadorLanza(lista.getIdNodo(actual), valorDado);

            if (valorDado % 2 != 0) { // Regla: Impar = Elimina
                vista.mostrarEstadoJugador(lista.getIdNodo(actual), true, valorDado);

                // Mantenemos el nodo actual para avanzar después de eliminar
                Nodo temp = actual;
                actual = lista.getNext(actual);
                lista.eliminar(anterior);

            } else { // Regla: Par = Se salva
                vista.mostrarEstadoJugador(lista.getIdNodo(actual), false, valorDado);
                anterior = actual;
                actual = lista.getNext(actual);
            }            
        }
        vista.mostrarGanador(lista.getIdNodo(lista.getHead()));
    }

    /**
     * Método auxilizar para aceptar solo numeros enteros mayores a cero
     *
     * @param mensaje texto a mostrar en interfaz para el usuario
     * @return numero entero valido capturado
     */
    private int leerNumeroInt(String mensaje) {
        int x;
        while (true) {
            try {
                x = Integer.parseInt(vista.leerDato(mensaje));
                if (x > 0) {
                    break;
                }
            } catch (NumberFormatException ex) {
            }
        }
        return x;
    }

}
