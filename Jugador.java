public class Jugador {

    /* Atributos de la clase Jugador */

    private String nombre; //Nombre del jugador
    private Mano mano; //Mano del jugador que es un objeto de la clase Mano
    private int puntos; //Puntos del jugador
    private boolean plantado; //Indica si el jugador se ha plantado o no
    private boolean eliminado; //Indica si el jugador ha sido eliminado del juego o no 

    /* Constructores */

    public Jugador (String jnombre) {
        this.nombre = jnombre; //Nos referimos al nombre del jugador que se le pasa por parametro
        this.mano = new Mano(); //Creamos una nueva mano para el jugador
        this.puntos = 0; //Iniciamos los puntos a 0 del jugador
        this.plantado = false; //Reniciamos la variable plantado
        this.eliminado = false; //Reiniciamos la variable eliminado

    }

    /* Getters y Setters */

    public String getNombre() {
        return this.nombre; //Devuelve el nombre del jugador
    }

    public Mano getMano() {
        return this.mano; //Devolvemos la mano del jugador
    }
    
    public int getPuntos() {
        return this.puntos; //Devolvemos los puntos del jugador
    }

    public boolean isPlantado() {
        return this.plantado; //Devolvemos si el jugador se ha plantado o no
    }

    public boolean isEliminado() {
         return this.eliminado; //Devolvemos si el jugador ha sido eliminado o no
    }


    /* Metodos  */

    public int obtenerPuntos() {
        return puntos; //Devolvemos los puntos del jugador
    }

    // Pedir carta
    public void pedirCarta(Mazo mazo) {
        Carta carta = mazo.repartirCarta(); // Obtenemos una carta del mazo y la añadimos a la mano del jugador
        mano.agregarCarta(carta); // Añadimos la carta a la mano del jugador
    }

    // Reiniciar para nueva ronda
    public void reiniciarRonda() {
        mano.limpiar(); //Limpiamos la mano del jugador
        plantado = false; //Reinicializamos la variable plantado al comenzar la ronda
        eliminado = false; //Reinicializamos la variable eliminado al comenzar la ronda
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
            eliminado = true; //Si la mano del jugador pasa de 21 o tiene blackjack, el jugador es eliminado del juego
            return false; //Si la mano del jugador pasa de 21 o tiene blackjack, el jugador no puede seguir jugando
        } else {
            return true; //Si la mano del jugador no pasa de 21 y no tiene blackjack, el jugador puede seguir jugando
        }
    }

    // Devuelve true si el turno del jugador ha terminado (platando,blackjack o eliminado)

    public boolean turnoTerminado() {
        return plantado || mano.estaPasado() || mano.tieneBlackjack() || eliminado;  //El turno del jugador termina si se ha plantado, si su mano pasa de 21, si tiene blackjack o si ha sido eliminado
    
    }
    /* Metodo toString para mostrar el nombre del jugador al imprimir el objeto Jugador */

    @Override public String toString() {
        return nombre + "[Puntos: " + puntos + "]"; //Devolvemos el nombre del jugador al imprimir el objeto Jugador
    }

}