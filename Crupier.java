public class Crupier extends Jugador {

    public Crupier() {
        super("Crupier"); //Llamamos al constructor de la clase Jugador para inicializar el nombre del crupier
    }

    public void jugarTurno(Mazo mazo, Mano mano ) {
        while (mano.obtenerValorMano() < 17) { //El crupier debe seguir pidiendo cartas hasta que su mano tenga un valor de al menos 17
            pedirCarta(mazo); //El crupier pide una carta del mazo
        }
        plantarse(); //Una vez que el crupier tiene un valor de mano de al menos 17, se planta
    }
    
}
