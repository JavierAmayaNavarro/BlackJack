public class Jugador {

    /* Atributos de la clase Jugador */
    private String nombre; // Nombre del jugador
    private Mano mano; // Mano del jugador que es un objeto de la clase Mano
    private int puntos; // Puntos del jugador
    private boolean plantado; // Indica si el jugador se ha plantado o no
    private boolean eliminado; // Indica si el jugador ha sido eliminado del juego o no 

    /* Constructores */
    public Jugador(String jnombre) {
        this.nombre = jnombre; // Nos referimos al nombre del jugador que se le pasa por parametro
        this.mano = new Mano(); // Creamos una nueva mano para el jugador
        this.puntos = 0; // Iniciamos los puntos a 0 del jugador
        this.plantado = false; // Reiniciamos la variable plantado
        this.eliminado = false; // Reiniciamos la variable eliminado
    }

    /* Getters y Setters */
    public String getNombre() {
        return this.nombre; // Devuelve el nombre del jugador
    }

    public Mano getMano() {
        return this.mano; // Devuelve la mano del jugador
    }

    public int getPuntos() {
        return this.puntos; // Devuelve los puntos del jugador
    }

    public boolean isPlantado() {
        return this.plantado; // Devuelve si el jugador se ha plantado o no
    }

    public boolean isEliminado() {
        return this.eliminado; // Devuelve si el jugador ha sido eliminado o no
    }

    /* Métodos */
    public int obtenerPuntos() {
        return puntos; // Devuelve los puntos del jugador
    }

    /* Actualiza el estado de eliminado según si la mano se ha pasado. */
    public void actualizarEstado() {
        if (mano.estaPasado()) {
            eliminado = true;
        }
    }

    // Pedir carta
    public void pedirCarta(Carta carta) {
        mano.agregarCarta(carta); // Añadimos la carta a la mano del jugador
    }

    // Reiniciar para nueva ronda
    public void reiniciarRonda() {
        mano.limpiar(); // Limpiamos la mano del jugador
        plantado = false; // Reinicializamos la variable plantado al comenzar la ronda
        eliminado = false; // Reiniciamos la variable eliminado al comenzar la ronda
    }

    // Plantarse
    public void plantarse() {
        plantado = true;
    }

    // Sumar punto cuando gana ronda
    public void sumarPunto() {
        puntos++;
    }

    // Comprobar si puede seguir jugando
    public boolean esValido() {
        if (mano.estaPasado() || mano.tieneBlackjack()) {
            eliminado = true; // Si la mano del jugador pasa de 21 o tiene blackjack, el jugador es eliminado del juego
            return false; // Si la mano del jugador pasa de 21 o tiene blackjack, el jugador no puede seguir jugando
        } else {
            return true; // Si la mano del jugador no pasa de 21 y no tiene blackjack, el jugador puede seguir jugando
        }
    }

    // Devuelve true si el turno del jugador ha terminado (plantado, blackjack o eliminado)
    public boolean turnoTerminado() {
        return plantado || mano.estaPasado() || mano.tieneBlackjack() || eliminado;
    }

    /* Método toString para mostrar el nombre del jugador al imprimir el objeto Jugador */
    @Override
    public String toString() {
        return nombre + "[Puntos: " + puntos + "]"; // Devolvemos el nombre del jugador al imprimir el objeto Jugador
    }
}