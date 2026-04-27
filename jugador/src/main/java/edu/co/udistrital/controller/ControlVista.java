package edu.co.udistrital.controller;

import edu.co.udistrital.view.JuegoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author mauri
 */
public class ControlVista implements ActionListener{
    private JuegoView vista;
    private JuegoController juego;

    public ControlVista(JuegoController juego) {
        vista = new JuegoView(this);
        this.juego = juego;
    }   
    
    public void pedirNumeroJugadores() {        
        int n = vista.pedirNumeroJugadores();
        vista.iniciarVentana(n);
        juego.ejecutarJuego(n);       
    }
       
    public void mostrarLanzamiento(int idJugador, int valorDado) {
        vista.mostrarJugadorLanza(idJugador, valorDado);
    }
    
    public void mostrarEstadoJugador(int idJugador, boolean eliminado, int valorDado) {
        vista.mostrarEstadoJugador(idJugador, eliminado, valorDado);
    }
    
    public void mostrarGanador(int idJugador) {
        vista.mostrarGanador(idJugador);
    }

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
