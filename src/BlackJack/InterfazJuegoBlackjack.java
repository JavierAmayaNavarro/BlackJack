package BlackJack;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Interfaz gráfica para el juego de Blackjack
 * Maneja la visualización del tablero y los controles de juego
 */
public class InterfazJuegoBlackjack extends JFrame {
    private PanelTablero panelTablero;
    private JuegoBlackjack juego;
    private JButton btnHit;
    private JButton btnStand;
    private JButton btnNuevaPartida;
    private JLabel lblEstado;
    private JLabel lblMarcador;
    
    // Variables para sincronismo de acciones
    private volatile String accionJugador = null;
    private final Object lockAccion = new Object();

    public InterfazJuegoBlackjack() {
        setTitle("♠ ♥ BLACKJACK ♦ ♣");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(new Color(20, 100, 40));  // Verde más oscuro
        
        // Crear y establecer icono personalizado
        setIconImage(crearIconoBlackjack());

        // Crear instancia del juego
        juego = new JuegoBlackjack(this);

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBackground(new Color(20, 100, 40));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior: Título y estado
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 5));
        panelSuperior.setBackground(new Color(15, 70, 35));  // Verde oscuro más realista
        panelSuperior.setPreferredSize(new Dimension(980, 150));
        panelSuperior.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 3));

        JLabel lblTitulo = new JLabel("♠ ♥ BLACKJACK ♦ ♣");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblTitulo.setForeground(new Color(180, 255, 220));  // Neon suave
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);

        lblEstado = new JLabel("Inicializando...");
        lblEstado.setFont(new Font("Arial", Font.BOLD, 20));
        lblEstado.setForeground(new Color(120, 255, 120));  // Verde claro brillante
        lblEstado.setHorizontalAlignment(JLabel.CENTER);

        lblMarcador = new JLabel("Marcador: ");
        lblMarcador.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblMarcador.setForeground(new Color(180, 255, 220));  // Neon marcador

        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(new Color(10, 30, 25));
        panelTitulo.setOpaque(true);
        panelTitulo.setPreferredSize(new Dimension(980, 80));
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        // Neon separator below title
        JSeparator sep = new JSeparator();
        sep.setPreferredSize(new Dimension(0, 4));
        sep.setForeground(new Color(100, 240, 200));
        sep.setBackground(new Color(100, 240, 200));
        panelTitulo.add(sep, BorderLayout.SOUTH);

        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBackground(new Color(10, 30, 25));
        panelEstado.setPreferredSize(new Dimension(980, 50));
        lblEstado.setPreferredSize(new Dimension(960, 46));
        lblEstado.setHorizontalAlignment(JLabel.CENTER);
        lblEstado.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        lblEstado.setOpaque(true);
        lblEstado.setBackground(new Color(5, 30, 25));
        panelEstado.add(lblEstado, BorderLayout.CENTER);

        JPanel panelMarcador = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelMarcador.setBackground(new Color(10, 60, 30));
        panelMarcador.add(lblMarcador);

        panelSuperior.add(panelTitulo, BorderLayout.NORTH);
        panelSuperior.add(panelEstado, BorderLayout.CENTER);
        panelSuperior.add(panelMarcador, BorderLayout.SOUTH);

        // Panel central: Tablero de juego
        panelTablero = new PanelTablero(juego);
        
        // Panel inferior: Botones
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelInferior.setBackground(new Color(5, 30, 35, 220));
        panelInferior.setOpaque(true);
        panelInferior.setPreferredSize(new Dimension(980, 80));
        panelInferior.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 3));

        btnHit = crearBoton("PEDIR CARTA (Hit)", new Color(70, 150, 220));
        btnHit.addActionListener(e -> {
            synchronized (lockAccion) {
                accionJugador = "1";
                lockAccion.notifyAll();
            }
        });

        btnStand = crearBoton("PLANTARSE (Stand)", new Color(210, 150, 30));
        btnStand.addActionListener(e -> {
            synchronized (lockAccion) {
                accionJugador = "2";
                lockAccion.notifyAll();
            }
        });

        btnNuevaPartida = crearBoton("NUEVA PARTIDA", new Color(50, 180, 50));
        btnNuevaPartida.addActionListener(e -> reiniciarPartida());

        panelInferior.add(btnHit);
        panelInferior.add(btnStand);
        panelInferior.add(btnNuevaPartida);

        // Agregar paneles al principal
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        // Hacer el tablero desplazable para mostrar correctamente hasta 7 jugadores
        JScrollPane scrollTablero = new JScrollPane(panelTablero, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTablero.setBorder(BorderFactory.createEmptyBorder());
        scrollTablero.getViewport().setBackground(new Color(20, 100, 40));
        scrollTablero.setPreferredSize(new Dimension(980, 520));
        panelPrincipal.add(scrollTablero, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(panelPrincipal);
        setVisible(true);

        // Iniciar el juego en un hilo separado
        iniciarJuego();
    }

    private JButton crearBoton(String texto, Color color) {
        // Botón moderno: fondo con gradiente, bordes redondeados y sombra sutil
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Sombra
                g2.setColor(new Color(0,0,0,80));
                g2.fillRoundRect(4, 4, w-8, h-8, 18, 18);

                // Gradiente de fondo
                GradientPaint gp = new GradientPaint(0, 0, color.brighter(), 0, h, color.darker());
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w-6, h-6, 16, 16);

                // Texto
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                g2.setFont(getFont());
                g2.drawString(getText(), (w - textWidth) / 2 - 3, (h + textHeight) / 2 - 6);

                g2.dispose();
            }

            @Override
            public void setBackground(Color bg) {
                // no-op para evitar que LookAndFeel sobrescriba el gradiente
            }
        };

        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(190, 52));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        return btn;
    }

    /**
     * Crea un icono personalizado de BlackJack
     */
    private Image crearIconoBlackjack() {
        int size = 64;
        BufferedImage imagen = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imagen.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo verde de mesa
        g2d.setColor(new Color(20, 100, 40));
        g2d.fillRect(0, 0, size, size);

        // Borde dorado
        g2d.setColor(new Color(255, 215, 0));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(2, 2, size - 4, size - 4);

        // Símbolo de picas grande en el centro-izquierda
        g2d.setColor(new Color(255, 215, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        g2d.drawString("♠", 5, 45);

        // Símbolo de corazones en el centro-derecha
        g2d.setColor(new Color(255, 100, 100));
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        g2d.drawString("♥", 40, 45);

        g2d.dispose();
        return imagen;
    }

    private void iniciarJuego() {
        mostrarDialogoJugadores();
    }

    private void mostrarDialogoJugadores() {
        // Panel principal con fondo
        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(new Color(34, 139, 34));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel de título
        JPanel panelTituloDialog = new JPanel();
        panelTituloDialog.setBackground(new Color(34, 139, 34));
        JLabel lblTituloDialog = new JLabel("⚙️ CONFIGURAR JUGADORES");
        lblTituloDialog.setFont(new Font("Arial", Font.BOLD, 18));
        lblTituloDialog.setForeground(Color.YELLOW);
        panelTituloDialog.add(lblTituloDialog);

        // Panel para número de jugadores
        JPanel panelNum = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelNum.setBackground(new Color(34, 139, 34));
        
        JLabel lblNum = new JLabel("¿Cuántos jugadores? (4-7):");
        lblNum.setFont(new Font("Arial", Font.BOLD, 13));
        lblNum.setForeground(Color.WHITE);
        
        JSpinner spinnerNum = new JSpinner(new SpinnerNumberModel(4, 1, 10, 1));
        spinnerNum.setPreferredSize(new Dimension(60, 40));
        spinnerNum.setFont(new Font("Arial", Font.BOLD, 14));
        
        panelNum.add(lblNum);
        panelNum.add(spinnerNum);

        // Panel para nombres con campos individuales
        JPanel panelNombres = new JPanel();
        panelNombres.setLayout(new BoxLayout(panelNombres, BoxLayout.Y_AXIS));
        panelNombres.setBackground(new Color(34, 139, 34));
        panelNombres.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel lblNombresTitle = new JLabel("Ingresa los nombres:");
        lblNombresTitle.setFont(new Font("Arial", Font.BOLD, 12));
        lblNombresTitle.setForeground(Color.WHITE);
        panelNombres.add(lblNombresTitle);
        panelNombres.add(Box.createVerticalStrut(8));

        // Campos de nombres dinámicos
        JTextField[] camposNombres = new JTextField[7];
        for (int i = 0; i < 7; i++) {
            JPanel panelCampo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            panelCampo.setBackground(new Color(34, 139, 34));
            
            JLabel lblJugador = new JLabel("Jugador " + (i + 1) + ":");
            lblJugador.setFont(new Font("Arial", Font.PLAIN, 11));
            lblJugador.setForeground(Color.WHITE);
            lblJugador.setPreferredSize(new Dimension(70, 30));
            
            camposNombres[i] = new JTextField(20);
            camposNombres[i].setText("Jugador " + (i + 1));
            camposNombres[i].setFont(new Font("Arial", Font.PLAIN, 11));
            camposNombres[i].setPreferredSize(new Dimension(200, 30));
            camposNombres[i].setEnabled(i < 4);  // Solo primeros 4 habilitados
            
            if (i >= 4) {
                camposNombres[i].setForeground(new Color(100, 100, 100));
            }
            
            panelCampo.add(lblJugador);
            panelCampo.add(camposNombres[i]);
            panelNombres.add(panelCampo);
        }

        // Listener para habilitar/deshabilitar campos según cantidad
        spinnerNum.addChangeListener(e -> {
            int cantidad = (int) spinnerNum.getValue();
            for (int i = 0; i < 7; i++) {
                camposNombres[i].setEnabled(i < cantidad);
                if (i < cantidad) {
                    camposNombres[i].setForeground(Color.BLACK);
                } else {
                    camposNombres[i].setForeground(new Color(100, 100, 100));
                }
            }
        });

        // Scroll para los nombres
        JScrollPane scrollNombres = new JScrollPane(panelNombres);
        scrollNombres.setBackground(new Color(34, 139, 34));
        scrollNombres.setBorder(BorderFactory.createEmptyBorder());

        // Panel de contenido
        JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
        panelContenido.setBackground(new Color(34, 139, 34));
        panelContenido.add(panelNum, BorderLayout.NORTH);
        panelContenido.add(scrollNombres, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBotones.setBackground(new Color(34, 139, 34));

        JButton btnIniciar = new JButton("INICIAR JUEGO");
        btnIniciar.setFont(new Font("Arial", Font.BOLD, 12));
        btnIniciar.setPreferredSize(new Dimension(150, 40));
        btnIniciar.setBackground(new Color(50, 200, 50));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setBorder(BorderFactory.createRaisedBevelBorder());

        JButton btnCancelar = new JButton("SALIR");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCancelar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.setBackground(new Color(200, 50, 50));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(BorderFactory.createRaisedBevelBorder());

        panelBotones.add(btnIniciar);
        panelBotones.add(btnCancelar);

        // Agregar todo
        panelPrincipal.add(panelTituloDialog, BorderLayout.NORTH);
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Crear diálogo
        JDialog dialog = new JDialog(this, "Configuración de Jugadores", true);
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.add(panelPrincipal);

        // Acciones de botones
        btnIniciar.addActionListener(e -> {
            int numJugadores = (int) spinnerNum.getValue();
            
            // Validar rango
            if (numJugadores < 4 || numJugadores > 7) {
                JOptionPane.showMessageDialog(
                    dialog,
                    "❌ ERROR: Debe ingresar entre 4 y 7 jugadores.\nActualmente ingresó: " + numJugadores,
                    "Cantidad inválida",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            
            // Agregar jugadores
            for (int i = 0; i < numJugadores; i++) {
                String nombre = camposNombres[i].getText().trim();
                if (nombre.isEmpty()) {
                    nombre = "Jugador " + (i + 1);
                }
                juego.agregarJugador(nombre);
            }
            
            dialog.dispose();
            iniciarPartida();
        });

        btnCancelar.addActionListener(e -> {
            dialog.dispose();
            System.exit(0);
        });

        dialog.setVisible(true);
    }

    private void iniciarPartida() {
        Thread hiloJuego = new Thread(() -> {
            try {
                juego.bienvenida_juego();
                actualizarEstado("Inicializando partida...");
                Thread.sleep(1000);

                juego.inicializarMazo();

                while (!juego.hayGanadorFinal()) {
                    juego.jugarRonda();
                    actualizarMarcador();
                    actualizarTablero();

                    if (!juego.hayGanadorFinal()) {
                        Thread.sleep(1500);
                    }
                }

                juego.define_ganador_final();
                actualizarTablero();
                Thread.sleep(1000);
                
                // Mostrar resumen final
                mostrarResumenFinal();
                
                deshabilitarControles();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        hiloJuego.setDaemon(true);
        hiloJuego.start();
    }

    public void mostrarTurnoJugador(String nombreJugador) {
        SwingUtilities.invokeLater(() -> {
            lblEstado.setText("TURNO DE: " + nombreJugador.toUpperCase());
            lblEstado.setForeground(new Color(120, 255, 220));
            lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 20));
            lblEstado.setOpaque(true);
            lblEstado.setBackground(new Color(5, 30, 25));
            lblEstado.revalidate();
            lblEstado.repaint();
            // ensure parent repaint so it's visible immediately
            if (lblEstado.getParent() != null) lblEstado.getParent().repaint();
        });
    }

    public String obtenerAccion() {
        synchronized (lockAccion) {
            accionJugador = null;
            try {
                lockAccion.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return accionJugador;
        }
    }

    public void actualizarTablero() {
        SwingUtilities.invokeLater(() -> panelTablero.repaint());
    }

    public void actualizarEstado(String mensaje) {
        SwingUtilities.invokeLater(() -> lblEstado.setText(mensaje));
    }

    public void actualizarMarcador() {
        SwingUtilities.invokeLater(() -> {
            StringBuilder sb = new StringBuilder();
            sb.append("🏆 RONDAS GANADAS: ");
            sb.append("Crupier ").append(juego.getCrupier().getPuntos()).append(" | ");
            for (Jugador j : juego.getJugadores()) {
                sb.append(j.getNombre()).append(" ").append(j.getPuntos()).append(" | ");
            }
            lblMarcador.setText(sb.toString());
        });
    }

    public void actualizarTableroDesdeJuego() {
        SwingUtilities.invokeLater(() -> panelTablero.repaint());
    }

    public void mostrarResultadoRonda(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            JDialog dialog = new JDialog(this, "Resultado de la Ronda", true);
            dialog.setSize(500, 200);
            dialog.setLocationRelativeTo(this);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(new Color(34, 139, 34));
            panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            JTextArea textArea = new JTextArea(mensaje);
            textArea.setFont(new Font("Arial", Font.PLAIN, 13));
            textArea.setForeground(Color.WHITE);
            textArea.setBackground(new Color(0, 0, 0, 100));
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JScrollPane scroll = new JScrollPane(textArea);
            scroll.setBackground(new Color(34, 139, 34));

            JButton btnCerrar = new JButton("Continuar");
            btnCerrar.setFont(new Font("Arial", Font.BOLD, 12));
            btnCerrar.setBackground(new Color(70, 130, 180));
            btnCerrar.setForeground(Color.WHITE);
            btnCerrar.setFocusPainted(false);
            btnCerrar.addActionListener(e -> dialog.dispose());

            JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panelBoton.setBackground(new Color(34, 139, 34));
            panelBoton.add(btnCerrar);

            panel.add(scroll, BorderLayout.CENTER);
            panel.add(panelBoton, BorderLayout.SOUTH);
            dialog.add(panel);
            dialog.setVisible(true);
        });
    }

    private void deshabilitarControles() {
        btnHit.setEnabled(false);
        btnStand.setEnabled(false);
    }

    /**
     * Muestra un resumen detallado de los resultados finales de la partida
     */
    private void mostrarResumenFinal() {
        SwingUtilities.invokeLater(() -> {
            // Obtener ganador final
            Jugador ganador = juego.getGanadorFinal();
            
            // Crear diálogo
            JDialog dialogo = new JDialog(this, "🏆 PARTIDA FINALIZADA 🏆", true);
            dialogo.setSize(600, 350);
            dialogo.setLocationRelativeTo(this);
            dialogo.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            
            JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
            panelPrincipal.setBackground(new Color(15, 70, 35));
            panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            // Panel título
            JPanel panelTitulo = new JPanel();
            panelTitulo.setBackground(new Color(15, 70, 35));
            JLabel lblTitulo = new JLabel("🏆 RESULTADOS FINALES 🏆");
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
            lblTitulo.setForeground(new Color(255, 215, 0));
            panelTitulo.add(lblTitulo);
            
            // Área de texto con resultados
            JTextArea areaResultados = new JTextArea();
            areaResultados.setFont(new Font("Courier New", Font.PLAIN, 12));
            areaResultados.setForeground(new Color(255, 240, 100));
            areaResultados.setBackground(new Color(10, 40, 20));
            areaResultados.setEditable(false);
            areaResultados.setLineWrap(true);
            areaResultados.setWrapStyleWord(true);
            areaResultados.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Construir texto de resultados
            StringBuilder resultados = new StringBuilder();
            resultados.append("\n");
            
            if (ganador != null) {
                resultados.append("🎉 ¡GANADOR FINAL! 🎉\n");
                resultados.append("━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
                resultados.append("👑 ").append(ganador.getNombre().toUpperCase());
                resultados.append(" - ").append(ganador.getPuntos()).append(" VICTORIAS\n\n");
            }
            
            resultados.append("━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            resultados.append("RANKING FINAL:\n");
            resultados.append("━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");
            
            // Ordenar jugadores por puntos
            List<Jugador> jugadoresOrdenados = new java.util.ArrayList<>(juego.getJugadores());
            jugadoresOrdenados.sort((a, b) -> Integer.compare(b.getPuntos(), a.getPuntos()));
            
            int posicion = 1;
            for (Jugador j : jugadoresOrdenados) {
                String emoji = posicion == 1 ? "🥇" : posicion == 2 ? "🥈" : posicion == 3 ? "🥉" : "  ";
                resultados.append(emoji).append(" #").append(posicion).append(" - ");
                resultados.append(String.format("%-20s%2d victorias\n", j.getNombre(), j.getPuntos()));
                posicion++;
            }
            
            resultados.append("\n━━━━━━━━━━━━━━━━━━━━━━━━━\n");
            resultados.append("Puntuación del Crupier: ").append(juego.getCrupier().getPuntos()).append("\n");
            
            areaResultados.setText(resultados.toString());
            
            JScrollPane scroll = new JScrollPane(areaResultados);
            scroll.setBackground(new Color(15, 70, 35));
            scroll.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
            
            // Panel de botones
            JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            panelBotones.setBackground(new Color(15, 70, 35));
            
            JButton btnContinuar = new JButton("NUEVA PARTIDA");
            btnContinuar.setFont(new Font("Arial", Font.BOLD, 13));
            btnContinuar.setPreferredSize(new Dimension(200, 45));
            btnContinuar.setBackground(new Color(50, 180, 50));
            btnContinuar.setForeground(Color.WHITE);
            btnContinuar.setFocusPainted(false);
            btnContinuar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
            ));
            btnContinuar.addActionListener(e -> {
                dialogo.dispose();
                reiniciarPartida();
            });
            
            panelBotones.add(btnContinuar);
            
            // Agregar todo
            panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
            panelPrincipal.add(scroll, BorderLayout.CENTER);
            panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
            
            dialogo.add(panelPrincipal);
            dialogo.setVisible(true);
        });
    }

    private void reiniciarPartida() {
        // Limpiar jugadores anteriores
        juego.getJugadores().clear();
        
        // Reiniciar el marcador
        actualizarMarcador();
        
        // Mostrar nuevamente el diálogo de configuración
        mostrarDialogoJugadores();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InterfazJuegoBlackjack::new);
    }
}
