package edu.co.udistrital.view;

/**
 * Clase encargada exclusivamente de mostrar la información al usuario en
 * consola. No contiene lógica de negocio, solo métodos de presentación.
 */
import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * Vista principal del juego. Maneja toda la interfaz gráfica con Swing.
 */
public class JuegoView {

    private JFrame frame;
    private JPanel panelJugadores;
    private JTextArea areaLog;
    private Map<Integer, JLabel> etiquetasJugadores; // id -> label en pantalla
    private JScrollPane scrollJugadores;

    /**
     * Pregunta al usuario cuántos jugadores quiere al iniciar.
     *
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
            if (input == null) {
                System.exit(0);
            }

            try {
                int n = Integer.parseInt(input.trim());
                if (n >= 2) {
                    return n;
                }
                JOptionPane.showMessageDialog(null, "Ingresa al menos 2 jugadores.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Construye y muestra la ventana principal del juego.
     *
     * @param numJugadores cantidad de jugadores para mostrar sus fichas.
     */
    public void iniciarVentana(int numJugadores) {

        etiquetasJugadores = new LinkedHashMap<>();

        frame = new JFrame("Juego de Eliminación");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        //Panel con wrap(filas)
        panelJugadores = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)) {
            // Crear filas manualmente para que el scrool vertical funcione
            @Override
            public Dimension getPreferredSize() {

                // Ancho disponible del contenedor padre (JScrollPane viewport)
                // Se usa para calcular cuándo hacer salto de fila (wrap manual)
                int width = getParent() != null ? getParent().getWidth() : 500;

                int x = 0;
                int y = 0;
                int rowHeight = 0;

                // Recorre todos los componentes (jugadores)
                for (Component c : getComponents()) {

                    // Tamaño preferido de cada componente
                    Dimension d = c.getPreferredSize();

                    // Si el siguiente componente no cabe en la fila actual,
                    // se hace salto de línea (nueva fila)
                    if (x + d.width > width) {
                        x = 0;                 // reinicia posición horizontal
                        y += rowHeight + 10;   // baja una fila completa (+ espacio)
                        rowHeight = 0;         // reinicia altura de fila
                    }

                    // suma ancho del componente + separación horizontal                    
                    x += d.width + 10;

                    // Mantiene la mayor altura de la fila actual
                    rowHeight = Math.max(rowHeight, d.height);
                }

                // Agrega la última fila al total de altura
                // Para que ultimos elmentos no se corten agregamos 20 pixeles como padding
                y += rowHeight + 20;

                // Retorna el tamaño total calculado del panel
                return new Dimension(width, y);
            }
        };
        panelJugadores.setBackground(new Color(230, 245, 245));

        for (int i = 1; i <= numJugadores; i++) {

            JLabel lbl = new JLabel("J" + i, SwingConstants.CENTER);

            lbl.setPreferredSize(new Dimension(70, 70));
            lbl.setOpaque(true);
            lbl.setBackground(new Color(70, 130, 180));
            lbl.setForeground(Color.WHITE);
            lbl.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

            panelJugadores.add(lbl);
            etiquetasJugadores.put(i, lbl);
        }

        scrollJugadores = new JScrollPane(panelJugadores);
        scrollJugadores.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollJugadores.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollJugadores.setPreferredSize(new Dimension(500, 180));

        areaLog = new JTextArea();
        JScrollPane scrollLog = new JScrollPane(areaLog);

        frame.add(scrollJugadores, BorderLayout.NORTH);
        frame.add(scrollLog, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    /**
     * Muestra en el log que un jugador lanzó el dado.
     *
     * @param idJugador ID del jugador.
     * @param valorDado Valor obtenido.
     */
    public void mostrarJugadorLanza(int idJugador, int valorDado) {
        JLabel lbl = etiquetasJugadores.get(idJugador);
        irAlJugador(lbl);
        if (lbl != null) {
            lbl.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        }
        agregarLog("🎲 Jugador " + idJugador + " lanzó: " + valorDado);
        pausar(400);
        if (lbl != null) {
            lbl.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        }
    }

    /**
     * Actualiza la interfaz según si el jugador fue eliminado o se salvó.
     *
     * @param idJugador ID del jugador.
     * @param eliminado true si fue eliminado, false si se salvó.
     * @param valorDado Valor del dado para el mensaje.
     */
    public void mostrarEstadoJugador(int idJugador, boolean eliminado, int valorDado) {
        JLabel lbl = etiquetasJugadores.get(idJugador);
        irAlJugador(lbl);
        if (eliminado) {
            agregarLog("   ❌ Jugador " + idJugador + " eliminado (impar).");
            if (lbl != null) {
                lbl.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                pausar(200);
                panelJugadores.remove(lbl);
                panelJugadores.revalidate();
                panelJugadores.repaint();
            }
        } else {
            agregarLog("   ✅ Jugador " + idJugador + " se salva (par).");
            if (lbl != null) {
                lbl.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
            }
        }
        pausar(100);

    }

    /**
     * Muestra el ganador al terminar el juego.
     *
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
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Mover scroll view a jugador
     *
     * @param lbl jugador
     */
    private void irAlJugador(JLabel lbl) {
        if (lbl == null) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            panelJugadores.scrollRectToVisible(lbl.getBounds());
        });
    }
}
