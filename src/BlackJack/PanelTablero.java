package BlackJack;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Panel donde se dibujan todas las cartas y la información del juego
 */
public class PanelTablero extends JPanel {
    private JuegoBlackjack juego;
    
    private static final int ANCHO_CARTA = 50;
    private static final int ALTO_CARTA = 80;
    private static final int SEPARACION = 6;
    private static final int MARGEN = 20;

    public PanelTablero(JuegoBlackjack juego) {
        this.juego = juego;
        setBackground(new Color(20, 100, 40));  // Verde más oscuro y bonito
    }

    @Override
    public Dimension getPreferredSize() {
        // Calcular altura dinámica según número de jugadores (3 por fila)
        int n = juego.getJugadores().size();
        int filas = Math.max(1, (n + 2) / 3); // ceil(n/3)
        int alturaBase = 160; // espacio para crupier y margen superior
        int alturaPorFila = 170;
        int altura = alturaBase + filas * alturaPorFila + MARGEN;
        altura = Math.max(550, altura);
        return new Dimension(980, altura);
    }

    /**
     * Dibuja el fondo con patrón de mesa de blackjack
     */
    private void dibujarFondoMesa(Graphics2D g2d) {
        // Gradiente de fondo verde más realista
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(12, 60, 40),
            getWidth(), getHeight(), new Color(8, 40, 30)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Spot de luz suave cerca del crupier
        float cx = getWidth() * 0.2f;
        float cy = 110f;
        float r = Math.max(getWidth(), getHeight()) * 0.35f;
        float[] dist = {0f, 1f};
        Color[] colors = {new Color(255, 255, 220, 80), new Color(255, 255, 220, 0)};
        RadialGradientPaint rgp = new RadialGradientPaint(cx, cy, r, dist, colors);
        g2d.setPaint(rgp);
        g2d.fillOval((int)(cx - r), (int)(cy - r), (int)(r*2), (int)(r*2));

        // Spot central tenue
        float cx2 = getWidth() / 2f;
        float cy2 = getHeight() / 2f;
        float r2 = Math.max(getWidth(), getHeight()) * 0.5f;
        float[] dist2 = {0f, 1f};
        Color[] colors2 = {new Color(120, 255, 220, 30), new Color(120, 255, 220, 0)};
        RadialGradientPaint rgp2 = new RadialGradientPaint(cx2, cy2, r2, dist2, colors2);
        g2d.setPaint(rgp2);
        g2d.fillOval((int)(cx2 - r2), (int)(cy2 - r2), (int)(r2*2), (int)(r2*2));

        // Patrón diagonal sutil para acabado futurista
        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.setStroke(new BasicStroke(1));
        for (int i = -getHeight(); i < getWidth(); i += 40) {
            g2d.drawLine(i, 0, i + getHeight(), getHeight());
        }

        // Borde decorativo exterior e interior con brillo neón
        g2d.setColor(new Color(100, 240, 200, 60));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(10, 10, getWidth() - 20, getHeight() - 20);
        g2d.setColor(new Color(100, 240, 200, 30));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(12, 12, getWidth() - 24, getHeight() - 24);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar fondo con patrón de mesa de blackjack
        dibujarFondoMesa(g2d);

        // Dibujar línea divisoria
        g2d.setColor(new Color(255, 215, 0, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(20, 145, getWidth() - 20, 145);

        // Dibujar CRUPIER en la parte superior
        int yActual = MARGEN;
        g2d.setColor(new Color(255, 215, 0, 80));
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.drawString("DEALER (CRUPIER)", MARGEN, yActual - 5);
        
        dibujarCrupier(g2d, MARGEN, yActual + 5);
        
        // Dibujar JUGADORES en filas de 3 jugadores
        yActual = 160;
        List<Jugador> jugadores = juego.getJugadores();
        
        // Primera fila de jugadores (hasta 3)
        int xPos = MARGEN;
        for (int i = 0; i < Math.min(3, jugadores.size()); i++) {
            dibujarJugador(g2d, jugadores.get(i), xPos, yActual);
            xPos += 310;
        }
        
        // Segunda fila de jugadores (si hay más de 3)
        if (jugadores.size() > 3) {
            yActual += 170;
            xPos = MARGEN;
            for (int i = 3; i < jugadores.size(); i++) {
                dibujarJugador(g2d, jugadores.get(i), xPos, yActual);
                xPos += 310;
            }
        }
    }

    private void dibujarCrupier(Graphics2D g2d, int x, int y) {
        // Título "CRUPIER"
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("CRUPIER", x, y);
        
        // Caja de información
        dibujarCajaCrupier(g2d, x, y + 15);
    }

    private void dibujarCajaCrupier(Graphics2D g2d, int x, int y) {
        // Rectángulo de fondo oscuro semi-transparente
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillRect(x - 5, y - 5, 310, 130);
        
        // Borde dorado más grueso
        g2d.setColor(new Color(255, 200, 0));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(x - 5, y - 5, 310, 130);
        
        // Cartas del crupier
        dibujarMano(g2d, juego.getCrupier().getMano(), x, y);
        
        // CAJA CON PUNTUACIÓN VISIBLE
        int puntosVisibles = calcularPuntosVisibles(juego.getCrupier().getMano());
        
        // Caja grande para los puntos
        int cajaX = x + 155;
        int cajaY = y + 10;
        int cajaAncho = 140;
        int cajaAlto = 50;
        
        // Fondo oscuro de la caja de puntos
        g2d.setColor(new Color(10, 10, 10, 200));
        g2d.fillRect(cajaX, cajaY, cajaAncho, cajaAlto);
        
        // Borde dorado grueso alrededor
        g2d.setColor(new Color(255, 215, 0));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(cajaX, cajaY, cajaAncho, cajaAlto);
        
        // Texto de puntos en GRANDE y DESTACADO
        String textoPuntos = String.valueOf(puntosVisibles);
        g2d.setColor(new Color(255, 215, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        FontMetrics fm = g2d.getFontMetrics();
        int anchoTexto = fm.stringWidth(textoPuntos);
        g2d.drawString(textoPuntos, cajaX + (cajaAncho - anchoTexto) / 2, cajaY + 38);
        
        // Indicador si hay cartas ocultas
        if (tieneCartaBocaAbajo(juego.getCrupier().getMano())) {
            g2d.setColor(new Color(255, 150, 0));
            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            g2d.drawString("(parcial)", cajaX + 5, cajaY + cajaAlto + 15);
        }
    }
    
    private int calcularPuntosVisibles(Mano mano) {
        int puntos = 0;
        int ases = 0;
        
        for (Carta carta : mano.getCartas()) {
            if (!carta.isBocaAbajo()) {
                int valor = carta.getPuntos();
                if (carta.getValor().equals("A")) {
                    ases++;
                    puntos += 11;
                } else if (carta.getValor().equals("J") || carta.getValor().equals("Q") || carta.getValor().equals("K")) {
                    puntos += 10;
                } else {
                    puntos += Integer.parseInt(carta.getValor());
                }
            }
        }
        
        while (puntos > 21 && ases > 0) {
            puntos -= 10;
            ases--;
        }
        
        return puntos;
    }
    
    private boolean tieneCartaBocaAbajo(Mano mano) {
        for (Carta carta : mano.getCartas()) {
            if (carta.isBocaAbajo()) {
                return true;
            }
        }
        return false;
    }

    private void dibujarJugador(Graphics2D g2d, Jugador jugador, int x, int y) {
        // Rectángulo de fondo oscuro semi-transparente
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.fillRect(x - 5, y - 5, 290, 160);
        
        // Borde grueso con color según estado
        g2d.setStroke(new BasicStroke(3.5f));
        if (jugador.getMano().tieneBlackjack()) {
            g2d.setColor(new Color(255, 215, 0));  // Oro para blackjack
        } else if (jugador.isEliminado()) {
            g2d.setColor(new Color(255, 100, 100));  // Rojo claro para pasarse
        } else if (jugador.isPlantado()) {
            g2d.setColor(new Color(100, 255, 100));  // Verde claro para plantado
        } else {
            g2d.setColor(new Color(150, 200, 255));  // Azul claro para jugando
        }
        g2d.drawRect(x - 5, y - 5, 290, 160);
        
        // Nombre del jugador (más grande)
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 15));
        g2d.drawString(jugador.getNombre(), x + 5, y + 5);
        
        // Cartas
        dibujarMano(g2d, jugador.getMano(), x, y + 20);
        
        // PUNTUACIÓN - CAJA DESTACADA MÁS GRANDE
        int puntos = jugador.getMano().obtenerValorMano();
        
        // Caja de fondo oscuro para los puntos
        int cajaX = x + 145;
        int cajaY = y + 60;
        int cajaAncho = 130;
        int cajaAlto = 50;
        
        g2d.setColor(new Color(20, 20, 20));
        g2d.fillRect(cajaX, cajaY, cajaAncho, cajaAlto);
        
        // Borde dorado grueso alrededor de los puntos
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(255, 215, 0));
        g2d.drawRect(cajaX, cajaY, cajaAncho, cajaAlto);
        
        // Número de puntos en GRANDE y MÁS LLAMATIVO
        g2d.setColor(new Color(255, 215, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        String ptsTxt = String.valueOf(puntos);
        FontMetrics fm = g2d.getFontMetrics();
        int anchoTexto = fm.stringWidth(ptsTxt);
        g2d.drawString(ptsTxt, cajaX + (cajaAncho - anchoTexto) / 2, cajaY + 38);
        
        // Estado del jugador
        String estado = "";
        Color colorEstado = Color.WHITE;
        
        if (jugador.getMano().tieneBlackjack()) {
            estado = "⭐ BLACKJACK";
            colorEstado = new Color(255, 215, 0);
        } else if (jugador.isEliminado()) {
            estado = "✗ PASADO (>21)";
            colorEstado = new Color(255, 100, 100);
        } else if (jugador.isPlantado()) {
            estado = "✔ PLANTADO";
            colorEstado = new Color(100, 255, 100);
        } else {
            estado = "▸ JUGANDO";
            colorEstado = new Color(150, 200, 255);
        }
        
        g2d.setColor(colorEstado);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString(estado, x + 5, y + 135);
    }

    private void dibujarMano(Graphics2D g2d, Mano mano, int x, int y) {
        List<Carta> cartas = mano.getCartas();
        int xActual = x;
        
        for (Carta carta : cartas) {
            dibujarCarta(g2d, xActual, y, carta);
            xActual += ANCHO_CARTA + SEPARACION;
        }
    }

    private void dibujarCarta(Graphics2D g2d, int x, int y, Carta carta) {
        int arc = 12;
        if (carta.isBocaAbajo()) {
            // Carta boca abajo: fondo marrón con borde redondeado
            g2d.setColor(new Color(90, 50, 20));
            g2d.fillRoundRect(x, y, ANCHO_CARTA, ALTO_CARTA, arc, arc);

            // Borde claro
            g2d.setColor(new Color(200, 160, 120));
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(x, y, ANCHO_CARTA, ALTO_CARTA, arc, arc);

            // Patrón sutil de puntos
            g2d.setColor(new Color(130, 80, 40));
            for (int i = 6; i < ANCHO_CARTA - 6; i += 8) {
                for (int j = 6; j < ALTO_CARTA - 6; j += 8) {
                    g2d.fillOval(x + i, y + j, 2, 2);
                }
            }
        } else {
            // Carta face-up: fondo blanco redondeado con sombra
            g2d.setColor(new Color(0,0,0,40));
            g2d.fillRoundRect(x+2, y+3, ANCHO_CARTA, ALTO_CARTA, arc, arc);

            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(x, y, ANCHO_CARTA, ALTO_CARTA, arc, arc);

            // Borde elegante
            g2d.setColor(new Color(40, 40, 40));
            g2d.setStroke(new BasicStroke(1.2f));
            g2d.drawRoundRect(x, y, ANCHO_CARTA, ALTO_CARTA, arc, arc);

            // Color del palo
            Color colorPalo = getColorPalo(carta.getPalo());
            g2d.setColor(colorPalo);

            // Valor grande (arriba-izquierda)
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            String valor = carta.getValor();
            g2d.drawString(valor, x + 8, y + 18);

            // Dibujo estilizado del palo (central)
            g2d.setFont(new Font("Serif", Font.PLAIN, 20));
            g2d.drawString(carta.getPalo(), x + ANCHO_CARTA/2 - 6, y + ALTO_CARTA/2 + 8);

            // Palo pequeño abajo
            g2d.setFont(new Font("Arial", Font.PLAIN, 11));
            String palo = carta.getPalo();
            g2d.drawString(palo, x + 8, y + ALTO_CARTA - 6);
        }
    }

    private Color getColorPalo(String palo) {
        if (palo.equals("♥") || palo.equals("♦")) {
            return new Color(220, 20, 60);  // Rojo para corazones y diamantes
        } else {
            return Color.BLACK;  // Negro para picas y tréboles
        }
    }
}
