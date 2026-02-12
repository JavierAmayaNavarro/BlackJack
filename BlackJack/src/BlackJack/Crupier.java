package BlackJack.src.BlackJack;
public class Crupier extends Jugador {

    /* Constantes */
    public static final int MINIMO_CRUPIER = 17;  // El crupier para de pedir al llegar a 17

    /* Atributos */
    private int puntos;          // Puntos acumulados (gana a 5)
    private boolean turnoActivo;  // true mientras el crupier está jugando su turno

    /*Constructores */
    public Crupier(String nombre) {
        super(nombre);  // Llama al constructor de la clase Jugador con el nombre del crupier
        this.puntos = 0;
        this.turnoActivo = false;
    }

    /* Metodos  */

    /** Reinicia la mano para una nueva ronda. */
    public void nuevaRonda() {
        getMano().limpiar();  // Llama al método limpiar() de la clase Mano para vaciar las cartas
        turnoActivo = false;
    }

    /** Recibe una carta. La segunda carta del reparto inicial se coloca boca abajo. */
    public void recibirCarta(Carta carta, boolean bocaAbajo) {
        carta.setBocaAbajo(bocaAbajo);
        getMano().agregarCarta(carta);
    }

    /** Revela la carta oculta (al inicio del turno del crupier). */
    public void revelarCartaOculta() {
        for (Carta c : getMano().getCartas()) {
            if (c.isBocaAbajo()) {
                c.setBocaAbajo(false);
                break;
            }
        }
    }

    /*Ejecuta el turno automático del crupier según las reglas:
     * pide cartas hasta que su total sea >= 17.
     * Devuelve las cartas robadas para mostrarlas en pantalla*/
    public void jugarTurno(Mazo mazo) {
        turnoActivo = true;
        revelarCartaOculta();
        // El crupier pide cartas hasta llegar a 17 o pasarse
        while (getMano().obtenerValorMano() < MINIMO_CRUPIER) {
            Carta nueva = mazo.repartirCarta();
            nueva.setBocaAbajo(false);
            getMano().agregarCarta(nueva);
        }
        turnoActivo = false;
    }

    /** El crupier suma un punto al marcador. */
    public void sumarPunto() {
        puntos++;
    }

    /** Puntuación total de la mano del crupier (todas las cartas, incluyendo ocultas). */
    public int getPuntuacionTotal() {
        return getMano().obtenerValorMano();
    }

    /** Puntuación visible (solo cartas boca arriba). */
    public int getPuntuacionVisible() {
        return getMano().obtenerValorMano();
    }

    /** Devuelve true si el crupier se ha pasado de 21. */
    public boolean estaPasado() {
        return getMano().obtenerValorMano() > 21;
    }

    /** Devuelve true si el crupier tiene Blackjack natural. */
    public boolean tieneBlackjack() {
        return getMano().tieneBlackjack();
    }

    // ─── Getters / Setters ─────────────────────────────────────────────────────
    public int getPuntos() {
        return puntos;
    }

    public boolean isTurnoActivo() {
        return turnoActivo;
    }
}