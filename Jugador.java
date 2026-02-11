public class Jugador {

    /* Atributos de la clase Jugador */

    private String nombre; //Nombre del jugador
    private Mano mano; //Mano del jugador que es un objeto de la clase Mano
    private int puntos; //Puntos del jugador
    private boolean plantado; //Indica si el jugador se ha plantado o no

    /* Constructores */

    public Jugador (String jnombre) {
        this.nombre = jnombre; //Nos referimos al nombre del jugador que se le pasa por parametro
        this.mano = new Mano(); //Creamos una nueva mano para el jugador
        this.puntos = 0; //Iniciamos los puntos a 0 del jugador
        this.plantado = false; //Reniciamos la variable plantado

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

        /* Metodos  */

    // Sumar punto cuando gana ronda
    public void sumarPunto() {
        puntos++;
    }

    //Devolvemos los puntos del jugador

    public int obtenerPuntos() {
        return puntos; //Devolvemos los puntos del jugador
    }

    // Pedir carta
    public void pedirCarta(Mazo mazo) {
        Carta carta = mazo.repartirCarta(); // Obtenemos una carta del mazo y la añadimos a la mano del jugador
        mano.agregarCarta(carta); // Añadimos la carta a la mano del jugador
    }

    // Plantarse
    public void plantarse() {
        plantado = true;
    }

    // Reiniciar para nueva ronda
    public void reiniciar() {
        mano = new Mano(); //Creamos una nueva mano para el jugador
        plantado = false; //Reinicializamos la variable plantado al comenzar la ronda
    }


    // Comprobar si puede seguir jugando
    public boolean esValido() {
        return !mano.estaPasado() && !mano.tieneBlackjack(); //Si la mano del jugador no pasa de 21 y no tiene blackjack, el jugador puede seguir jugando
    }

}