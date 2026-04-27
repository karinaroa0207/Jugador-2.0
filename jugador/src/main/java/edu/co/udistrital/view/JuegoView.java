package edu.co.udistrital.view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Vista principal del juego. Maneja toda la interfaz gráfica con Swing. Cumple
 * con el principio de responsabilidad única: solo dibuja y actualiza la UI.
 */
public class JuegoView {

    private JFrame frame;
    private JPanel panelJugadores;
    private JTextArea areaLog;
    private Map<Integer, JLabel> etiquetasJugadores; // id -> label en pantalla
    private JScrollPane scrollJugadores;
    private JScrollPane scrollLog;
    private JLayeredPane panelCapas;
    private JPanel panelJugadoresCircular;
    private JLabel[] dado;
    private ActionListener listener;
    private JPanel panelLog;

    public JuegoView(ActionListener listener) {
        this.listener = listener;
        etiquetasJugadores = new LinkedHashMap<>();
        dado = new JLabel[6];

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
        
        scrollLog = new JScrollPane(areaLog);
        scrollLog.setPreferredSize(new Dimension(300, 0)); // Ancho fijo a la izquierda
        /*JPanel panelDado = new JPanel(new BorderLayout());
        panelDado.setPreferredSize(new Dimension(0, 120));
        
        panelLog = new JPanel(new BorderLayout());

        panelLog.add(panelDado, BorderLayout.NORTH);
        panelLog.add(scrollLog, BorderLayout.CENTER);
        */
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

        scrollJugadores = new JScrollPane(panelJugadores);
        scrollJugadores.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollJugadores.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollJugadores.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 20)); // Margen derecho
        scrollJugadores.getViewport().setBackground(new Color(40, 45, 50));

        panelJugadoresCircular = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return getParent() != null ? getParent().getSize() : new Dimension(400, 400);
            }

            @Override
            public void doLayout() {
                int n = getComponentCount();
                if (n == 0) {
                    return;
                }

                int w = getWidth();
                int h = getHeight();

                int cx = w / 2;
                int cy = h / 2;

                int r = Math.min(w, h) / 3;

                for (int i = 0; i < n; i++) {
                    Component c = getComponent(i);

                    Dimension d = c.getPreferredSize();

                    double angle = 2 * Math.PI * i / n - Math.PI / 2;

                    int x = (int) (cx + r * Math.cos(angle) - d.width / 2);
                    int y = (int) (cy + r * Math.sin(angle) - d.height / 2);

                    c.setBounds(x, y, d.width, d.height);
                }
            }
        };
        panelJugadoresCircular.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                panelJugadoresCircular.revalidate();
                panelJugadoresCircular.repaint();
            }
        });

        panelJugadoresCircular.setBounds(0, 0, 600, 600);
        panelJugadoresCircular.setOpaque(false);

        panelCapas = new JLayeredPane();
        panelCapas.setBounds(0, 0, 600, 600);
        panelCapas.setPreferredSize(new Dimension(600, 600));
        panelCapas.setLayout(null);
        panelCapas.setOpaque(true);
        panelCapas.setBackground(new Color(40, 45, 50));

        panelJugadoresCircular.setOpaque(false);
        panelCapas.add(panelJugadoresCircular, Integer.valueOf(0));

        for (int i = 0; i < 6; i++) {

            JLabel face = new JLabel("Cara " + (i + 1), SwingConstants.CENTER);
            face.setFont(new Font("Arial", Font.BOLD, 30));
            face.setOpaque(true);
            face.setBackground(Color.WHITE);
            face.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            face.setBounds(250, 250, 100, 100);

            dado[i] = face;
            panelCapas.add(face, Integer.valueOf(i + 1));
        }
        frame.setVisible(false);

    }

    /**
     * Pregunta al usuario cuántos jugadores participan usando un diálogo
     * estilizado.
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
            JOptionPane optionPane = new JOptionPane(
                    panelEntrada,
                    JOptionPane.PLAIN_MESSAGE,
                    JOptionPane.OK_CANCEL_OPTION
            );

            JDialog dialog = optionPane.createDialog("Configuración del Juego");
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowActivated(java.awt.event.WindowEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        campoTexto.requestFocusInWindow();
                        campoTexto.selectAll();
                    });
                }
            });

            dialog.setVisible(true);

            Object value = optionPane.getValue();
            int resultado = (value instanceof Integer) ? (Integer) value : -1;

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
     * Construye y muestra la ventana principal del juego. Posiciona el Log a la
     * izquierda (con efecto luces) y los jugadores a la derecha.
     *
     * @param numJugadores cantidad de jugadores para generar sus fichas.
     */
    public void iniciarVentana(int numJugadores) {
        // Crear las "fichas" de los jugadores
        JPanel pJugadores;
        if (numJugadores > 10) {
            pJugadores = panelJugadores;
        } else {
            pJugadores = panelJugadoresCircular;
        }
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

            pJugadores.add(lbl);
            etiquetasJugadores.put(i, lbl);
        }
        if (numJugadores > 10) {
            frame.setSize(850, 500); // Ventana más ancha para formato Izquierda/Derecha
            frame.add(panelLog, BorderLayout.WEST); // Izquierda
            frame.add(scrollJugadores, BorderLayout.CENTER); // Derecha
        } else {
            frame.setSize(600, 600); // Ventana más ancha para formato circulo  
            panelCapas.revalidate();
            panelCapas.repaint();
            frame.add(panelCapas, BorderLayout.CENTER);
        }
        frame.setVisible(true);
    }

    /**
     * Muestra en el log que un jugador lanzó el dado y resalta su ficha
     * temporalmente.
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
            mostrarCaraDado(valorDado - 1);
        }
        agregarLog("🎲 Jugador " + idJugador + " lanzó: " + valorDado);
        pausar(500); // Pausa un poquito más larga para apreciar el efecto
        if (lbl != null) {
            lbl.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        }
    }

    /**
     * Actualiza la interfaz gráfica según si el jugador fue eliminado o se
     * salvó.
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

                panelJugadoresCircular.remove(lbl);
                panelJugadoresCircular.revalidate();
                panelJugadoresCircular.repaint();
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

        // Resaltar al ganador
        JLabel lbl = etiquetasJugadores.get(idGanador);
        if (lbl != null) {
            lbl.setBackground(new Color(255, 215, 0)); // dorado
            lbl.setForeground(Color.BLACK);
            lbl.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));
        }

        // 🔥 Opciones personalizadas
        String[] opciones = {"Reiniciar", "Salir"};

        int opcion = JOptionPane.showOptionDialog(
                frame,
                "🏆 ¡El ganador es el Jugador " + idGanador + "!\n\n¿Qué deseas hacer?",
                "Fin del juego",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (opcion == 0) {
            ActionEvent ev = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "reiniciar");
            listener.actionPerformed(ev);
        } else {
            frame.dispose();
            System.exit(0);
        }
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
     * Detiene la ejecución temporalmente para crear el efecto de animación por
     * turnos.
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

    public void reiniciar() {
        etiquetasJugadores.clear();
        mostrarCaraDado(0);
        areaLog.setText("");
        frame.getContentPane().removeAll();
        panelJugadores.removeAll();
        panelJugadoresCircular.removeAll();
    }

    private void mostrarCaraDado(int index) {
        for (JLabel d : dado) {
            d.setVisible(false);
        }
        dado[index].setVisible(true);
    }
}
