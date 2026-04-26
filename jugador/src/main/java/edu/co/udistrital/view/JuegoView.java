package edu.co.udistrital.view;

/**
 * Clase encargada exclusivamente de mostrar la información al usuario en consola.
 * No contiene lógica de negocio, solo métodos de presentación.
 */

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import javax.swing.JOptionPane;

/**
 * Vista principal del juego. Maneja toda la interfaz gráfica con Swing.
 */
public class JuegoView {

    private JFrame frame;
    private JPanel panelJugadores;
    private JTextArea areaLog;
    private Map<Integer, JLabel> etiquetasJugadores; // id -> label en pantalla

    /**
     * Pregunta al usuario cuántos jugadores quiere al iniciar.
     * @return número de jugadores ingresado.
     */
    public int pedirNumeroJugadores() {
        while (true) {
            String input = JOptionPane.showInputDialog(
                null,
                "¿Cuántos jugadores participan?",
                "Configuración del Juego",
                JOptionPane.QUESTION_MESSAGE
            );

            // Si cierra el diálogo, termina el programa
            if (input == null) System.exit(0);

            try {
                int n = Integer.parseInt(input.trim());
                if (n >= 2) return n;
                JOptionPane.showMessageDialog(null, "Ingresa al menos 2 jugadores.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Construye y muestra la ventana principal del juego.
     * @param numJugadores cantidad de jugadores para mostrar sus fichas.
     */
    public void iniciarVentana(int numJugadores) {
        etiquetasJugadores = new LinkedHashMap<>();

        frame = new JFrame("Juego de Eliminación");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        // --- Panel superior: jugadores activos ---
        panelJugadores = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelJugadores.setBorder(BorderFactory.createTitledBorder("Jugadores activos"));
        panelJugadores.setBackground(new Color(230, 245, 255));

        for (int i = 1; i <= numJugadores; i++) {
            JLabel lbl = new JLabel("J" + i, SwingConstants.CENTER);
            lbl.setPreferredSize(new Dimension(50, 50));
            lbl.setOpaque(true);
            lbl.setBackground(new Color(70, 130, 180));
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.BOLD, 16));
            lbl.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
            panelJugadores.add(lbl);
            etiquetasJugadores.put(i, lbl);
        }

        // --- Panel inferior: log de eventos ---
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaLog.setBackground(new Color(245, 245, 245));
        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setBorder(BorderFactory.createTitledBorder("Registro del juego"));
        scroll.setPreferredSize(new Dimension(580, 280));

        frame.add(panelJugadores, BorderLayout.NORTH);
        frame.add(scroll, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     * Muestra en el log que un jugador lanzó el dado.
     * @param idJugador ID del jugador.
     * @param valorDado Valor obtenido.
     */
    public void mostrarJugadorLanza(int idJugador, int valorDado) {
        agregarLog("🎲 Jugador " + idJugador + " lanzó: " + valorDado);
        pausar(400);
    }

    /**
     * Actualiza la interfaz según si el jugador fue eliminado o se salvó.
     * @param idJugador ID del jugador.
     * @param eliminado true si fue eliminado, false si se salvó.
     * @param valorDado Valor del dado para el mensaje.
     */
    public void mostrarEstadoJugador(int idJugador, boolean eliminado, int valorDado) {
        if (eliminado) {
            agregarLog("   ❌ Jugador " + idJugador + " eliminado (impar).");
            JLabel lbl = etiquetasJugadores.get(idJugador);
            if (lbl != null) {
                panelJugadores.remove(lbl);
                panelJugadores.revalidate();
                panelJugadores.repaint();
            }
        } else {
            agregarLog("   ✅ Jugador " + idJugador + " se salva (par).");
        }
        pausar(300);
    }

    /**
     * Muestra el ganador al terminar el juego.
     * @param idGanador ID del jugador ganador.
     */
    public void mostrarGanador(int idGanador) {
        agregarLog("\n🏆 ¡Jugador " + idGanador + " es el GANADOR!");
        JOptionPane.showMessageDialog(
            frame,
            "🏆 ¡El ganador es el Jugador " + idGanador + "!",
            "Fin del juego",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // --- Métodos internos ---

    private void agregarLog(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            areaLog.append(mensaje + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }

    private void pausar(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}