package edu.co.udistrital.controller;

import edu.co.udistrital.view.JuegoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controla la comunicación entre la vista y la lógica del juego.
 */
public class ControlVista implements ActionListener {

    private JuegoView vista;
    private JuegoController juego;

    /**
     * Inicializa la vista y el controlador del juego.
     */
    public ControlVista(JuegoController juego) {
        this.vista = new JuegoView(this);
        this.juego = juego;
    }

    /**
     * Solicita el número de jugadores, inicia la ventana
     * y ejecuta el juego con ese valor.
     */
    public void pedirNumeroJugadores() {
        int n = vista.pedirNumeroJugadores();
        vista.iniciarVentana(n);
        juego.ejecutarJuego(n);
    }

    /**
     * Muestra en la vista el resultado del lanzamiento de un jugador.
     * 
     * @param idJugador identificador del jugador
     * @param valorDado valor obtenido en el dado
     */
    public void mostrarLanzamiento(int idJugador, int valorDado) {
        vista.mostrarJugadorLanza(idJugador, valorDado);
    }

    /**
     * Muestra si un jugador sigue activo o fue eliminado,
     * junto con el valor obtenido en el dado.
     * 
     * @param idJugador identificador del jugador
     * @param eliminado estado del jugador
     * @param valorDado valor del dado
     */
    public void mostrarEstadoJugador(int idJugador, boolean eliminado, int valorDado) {
        vista.mostrarEstadoJugador(idJugador, eliminado, valorDado);
    }

    /**
     * Muestra el jugador ganador en la vista.
     * 
     * @param idJugador identificador del ganador
     */
    public void mostrarGanador(int idJugador) {
        vista.mostrarGanador(idJugador);
    }

    /**
     * Maneja acciones de la interfaz como reiniciar o salir.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equalsIgnoreCase("reiniciar")) {
            vista.reiniciar();
            juego.reiniciar();
        } else if (cmd.equalsIgnoreCase("salir")) {
            System.exit(0);
        }
    }
}
