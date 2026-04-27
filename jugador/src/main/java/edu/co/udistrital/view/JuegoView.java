package edu.co.udistrital.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Vista principal del juego. Maneja toda la interfaz gráfica con Swing.
 * Cumple con el principio de responsabilidad única: solo dibuja y actualiza la UI.
 */
public class JuegoView {

    private JFrame frame;
    private JPanel panelJugadores;
    private JTextArea areaLog;
    private Map<Integer, JLabel> etiquetasJugadores; // id -> label en pantalla
    private JScrollPane scrollJugadores;

    /**
     * Pregunta al usuario cuántos jugadores participan usando un diálogo estilizado.
     *
     * @return El número de jugadores ingresado (mínimo 2).
     */
    public int pedirNumeroJugadores() {
        // Personalizamos un poco el panel de entrada para que se vea más lindo
        JPanel panelEntrada = new JPanel(new BorderLayout(5, 5));
        JLabel mensaje = new JLabel("🎮 ¿Cuántos jugadores van a participar?");
        mensaje.setFont(new Font("SansSerif", Font.BOLD, 14));
        JTextField campoTexto = new JTextField(10);
        
        panelEntrada.add(mensaje, BorderLayout.NORTH);
        panelEntrada.add(campoTexto, BorderLayout.CENTER);

        while (true) {
            int resultado = JOptionPane.showConfirmDialog(
                    null,
                    panelEntrada,
                    "Configuración del Juego",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            // Si el usuario presiona la "X" o Cancelar, salimos del programa
            if (resultado != JOptionPane.OK_OPTION) {
                System.exit(0);
            }

            try {
                int n = Integer.parseInt(campoTexto.getText().trim());
                if (n >= 2) {
                    return n;
                }
                JOptionPane.showMessageDialog(null, "El juego requiere al menos 2 jugadores.", "Atención", JOptionPane.WARNING_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Por favor, ingresa un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Construye y muestra la ventana principal del juego.
     * Posiciona el Log a la izquierda (con efecto luces) y los jugadores a la derecha.
     *
     * @param numJugadores cantidad de jugadores para generar sus fichas.
     */
    public void iniciarVentana(int numJugadores) {

        etiquetasJugadores = new LinkedHashMap<>();

        frame = new JFrame("Juego de Eliminación - Ciencias de la Computación");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 500); // Ventana más ancha para formato Izquierda/Derecha
        frame.setLocationRelativeTo(null);
        
        // Agregamos un margen (gap) de 15 píxeles entre la izquierda y la derecha
        frame.setLayout(new BorderLayout(15, 15)); 
        frame.getContentPane().setBackground(new Color(30, 30, 30)); // Fondo oscuro general

        // --- 1. CONFIGURACIÓN DEL PANEL IZQUIERDO (EL CUADRITO DEL LOG CON LUCES) ---
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setBackground(new Color(15, 15, 15)); // Negro casi puro
        areaLog.setForeground(new Color(0, 255, 200)); // Letra color cyan/neón
        areaLog.setFont(new Font("Monospaced", Font.BOLD, 13));
        areaLog.setMargin(new Insets(10, 10, 10, 10));
        //Hacer que el texto no se salga horizontalmente y cree un scrool horizontal
        //Mostrar texto en la siguiente linea
        areaLog.setLineWrap(true);
        //Evita cortar palabras a la mitad
        areaLog.setWrapStyleWord(true);

        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setPreferredSize(new Dimension(300, 0)); // Ancho fijo a la izquierda
        
        // Creando el efecto "Luces" usando bordes compuestos
        Border bordeNeon = BorderFactory.createLineBorder(new Color(0, 255, 255), 3); // Línea gruesa Cyan
        Border bordeSombra = BorderFactory.createLineBorder(new Color(0, 150, 150), 2); // Sombra exterior
        Border bordeVacio = BorderFactory.createEmptyBorder(10, 10, 10, 10); // Margen para que respire
        
        // Juntamos los bordes para simular el brillo
        scrollLog.setBorder(BorderFactory.createCompoundBorder(
                bordeVacio, 
                BorderFactory.createCompoundBorder(bordeSombra, bordeNeon)
        ));
        scrollLog.getViewport().setBackground(new Color(15, 15, 15));

        // --- 2. CONFIGURACIÓN DEL PANEL DERECHO (LOS JUGADORES) ---
        panelJugadores = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15)) {
            @Override
            public Dimension getPreferredSize() {
                int width = getParent() != null ? getParent().getWidth() : 500;
                int x = 0, y = 0, rowHeight = 0;

                for (Component c : getComponents()) {
                    Dimension d = c.getPreferredSize();
                    if (x + d.width > width) {
                        x = 0;                 
                        y += rowHeight + 15;   
                        rowHeight = 0;         
                    }
                    x += d.width + 15;
                    rowHeight = Math.max(rowHeight, d.height);
                }
                y += rowHeight + 20;
                return new Dimension(width, y);
            }
        };
        panelJugadores.setBackground(new Color(40, 45, 50)); // Fondo gris oscuro para contraste

        // Crear las "fichas" de los jugadores
        for (int i = 1; i <= numJugadores; i++) {
            JLabel lbl = new JLabel("J" + i, SwingConstants.CENTER);
            lbl.setPreferredSize(new Dimension(80, 80));
            lbl.setOpaque(true);
            lbl.setBackground(new Color(70, 130, 180)); // Azul acero
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
            
            // Borde suave redondeado para los jugadores
            lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));

            panelJugadores.add(lbl);
            etiquetasJugadores.put(i, lbl);
        }

        scrollJugadores = new JScrollPane(panelJugadores);
        scrollJugadores.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollJugadores.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollJugadores.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20)); // Margen derecho
        scrollJugadores.getViewport().setBackground(new Color(40, 45, 50));

        // Ensamblar la ventana
        frame.add(scrollLog, BorderLayout.WEST); // Izquierda
        frame.add(scrollJugadores, BorderLayout.CENTER); // Derecha

        frame.setVisible(true);
    }

    /**
     * Muestra en el log que un jugador lanzó el dado y resalta su ficha temporalmente.
     *
     * @param idJugador ID del jugador.
     * @param valorDado Valor obtenido.
     */
    public void mostrarJugadorLanza(int idJugador, int valorDado) {
        JLabel lbl = etiquetasJugadores.get(idJugador);
        irAlJugador(lbl);
        if (lbl != null) {
            // Brillo amarillo al lanzar
            lbl.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
        }
        agregarLog("🎲 Jugador " + idJugador + " lanzó: " + valorDado);
        pausar(500); // Pausa un poquito más larga para apreciar el efecto
        if (lbl != null) {
            lbl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        }
    }

    /**
     * Actualiza la interfaz gráfica según si el jugador fue eliminado o se salvó.
     *
     * @param idJugador ID del jugador.
     * @param eliminado true si fue eliminado, false si se salvó.
     * @param valorDado Valor del dado para el mensaje.
     */
    public void mostrarEstadoJugador(int idJugador, boolean eliminado, int valorDado) {
        JLabel lbl = etiquetasJugadores.get(idJugador);
        irAlJugador(lbl);
        
        if (eliminado) {
            agregarLog("   ❌ Jugador " + idJugador + " ELIMINADO (Impar).");
            if (lbl != null) {
                lbl.setBackground(new Color(180, 50, 50)); // Cambia a rojo oscuro
                lbl.setBorder(BorderFactory.createLineBorder(Color.RED, 4));
                pausar(400);
                panelJugadores.remove(lbl);
                panelJugadores.revalidate();
                panelJugadores.repaint();
            }
        } else {
            agregarLog("   ✅ Jugador " + idJugador + " SE SALVA (Par).");
            if (lbl != null) {
                lbl.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
                pausar(300);
                lbl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
            }
        }
        agregarLog("-------------------------");
        pausar(200);
    }

    /**
     * Muestra el ganador al terminar el juego y luego cierra la aplicación.
     *
     * @param idGanador ID del jugador ganador.
     */
    public void mostrarGanador(int idGanador) {
        agregarLog("\n🏆 ¡EL GANADOR ES JUGADOR " + idGanador + "!");
        
        // Resaltar al ganador en la interfaz
        JLabel lbl = etiquetasJugadores.get(idGanador);
        if (lbl != null) {
            lbl.setBackground(new Color(255, 215, 0)); // Dorado
            lbl.setForeground(Color.BLACK);
            lbl.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
        }

        // Muestra el mensaje. El programa se "pausa" aquí hasta que el usuario da clic en OK
        JOptionPane.showMessageDialog(
                frame,
                "🏆 ¡El ganador es el Jugador " + idGanador + "!\n\nEl juego se cerrará ahora.",
                "Fin del juego",
                JOptionPane.INFORMATION_MESSAGE
        );

        
        frame.dispose(); // Libera los recursos de la ventana gráfica (la cierra visualmente)
        System.exit(0);  // Termina completamente el proceso de Java en la memoria
    } 

    /**
     * Agrega texto al log de manera segura en el hilo de la interfaz.
     */
    private void agregarLog(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            areaLog.append(mensaje + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }

    /**
     * Detiene la ejecución temporalmente para crear el efecto de animación por turnos.
     */
    private void pausar(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Mueve el scroll automáticamente hacia donde está el jugador actual.
     *
     * @param lbl Etiqueta del jugador a enfocar.
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