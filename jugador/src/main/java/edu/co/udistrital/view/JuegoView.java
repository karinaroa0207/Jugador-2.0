package edu.co.udistrital.view;

import java.util.Scanner;

/**
 * Clase encargada exclusivamente de mostrar la información al usuario en
 * consola. No contiene lógica de negocio, solo métodos de presentación y
 * captura de datos del usuario.
 *
 */
public class JuegoView {

    private Scanner sc;

    /**
     * Contructor incializa el escaner para la entrada por consola
     */
    public JuegoView() {
        sc = new Scanner(System.in);
    }

    /**
     * Muestra el lanzamiento de un dado por un jugador.
     *
     * @param id ID del jugador.
     * @param dado Valor obtenido en el dado.
     */
    public void mostrarJugadorLanza(int id, int dado) {
        System.out.println("Jugador " + id + " lanza el dado -> " + dado);
    }

    /**
     * Informa si el jugador fue eliminado o se salvó.
     *
     * @param id ID del jugador.
     * @param eliminado True si fue eliminado, false si se salvó.
     * @param dado Valor del dado que generó la acción.
     */
    public void mostrarEstadoJugador(int id, boolean eliminado, int dado) {
        String resultado = (dado % 2 != 0) ? "Impar" : "Par";
        String estado = eliminado ? "ELIMINADO :(" : "se SALVA :)";
        System.out.println("Resultado: " + resultado + " (" + dado + "). El jugador " + id + " " + estado + ".");
    }

    /**
     * Muestra el mensaje final del juego.
     *
     * @param id ID del ganador.
     */
    public void mostrarGanador(int id) {
        System.out.println("----------------------------------------");
        System.out.println("¡Ya termino! El ganador es el " + id);
    }

    /**
     * Permite obtener entrada texto del usuario imprimiendo mensaje
     *
     * @param mensaje texto a mostrar en consola
     * @return texto ingresado por el usuario
     */
    public String leerDato(String mensaje) {
        System.out.println(mensaje);
        String dato = sc.nextLine();
        return dato;
    }

    /**
     * Permite obtener entrada texto del usuario
     *
     * @return texto ingresado por el usuario
     */
    public String leerDato() {
        String dato = sc.nextLine();
        return dato;
    }

    /**
     * Muestra mensaje en consola
     *
     * @param mensaje texto a mostrar
     */
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

}
