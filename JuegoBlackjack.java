import java.util.ArrayList;
import java.util.Scanner;

public class JuegoBlackjack {

    /* Atributos del juego */

    private ArrayList<Jugador> jugadores; //Creamos un ArrayList de jugadores para representar a los jugadores en el juego
    private Crupier crupier; //Creamos una variable de tipo Crupier para representar al crupier en el juego
    private Mazo mazo;
    private Scanner scanner; //Creamos un objeto Scanner para leer la entrada del usuario

    /* Constructores del juego */
    public JuegoBlackjack() {
        jugadores = new ArrayList<>(); //Inicializamos el ArrayList de jugadores
        crupier = new Crupier(); //Creamos un nuevo crupier
        mazo = new Mazo(); //Creamos un nuevo mazo de cartas
        scanner = new Scanner(System.in); //Inicializamos el Scanner para leer la entrada del usuario
    }

    /* Metodos del juego */

    public void iniciarJuego() {

        System.out.println("Bienvenido al juego de Blackjack!"); //Mensaje de bienvenida al juego
        System.out.print("Ingrese el numero de jugadores (4-7): "); //Pedimos al usuario que ingrese el numero de jugadores
        int numJugadores = scanner.nextInt(); //Leemos el numero de jugadores ingresado por el usuario
        while (numJugadores < 4 || numJugadores > 7) { //Validamos que el numero de jugadores este entre 4 y 7
            System.out.print("Numero de jugadores invalido. Ingrese un numero entre 4 y 7: "); //Si el numero de jugadores es invalido, pedimos al usuario que ingrese un numero valido
            numJugadores = scanner.nextInt(); //Leemos el nuevo numero de jugadores ingresado por el usuario
        }
        scanner.nextLine(); //Limpiamos el buffer del Scanner para evitar problemas al leer el nombre de los jugadores
        for (int i = 1; i<numJugadores; i++) { 
            System.out.print("Ingrese el nombre del jugador " + i + ": "); //Pedimos al usuario que ingrese el nombre de cada jugador
            String nombreJugador = scanner.nextLine(); //Leemos el nombre del jugador ingresado por el usuario
            jugadores.add(new Jugador(nombreJugador)); //Agregamos el jugador al ArrayList de jugadores
        }

        // Bucle hasta que alguien llegue a 5 puntos 
        boolean finjuego = false; //Variable para controlar el fin del juego

        while(!finjuego){// Mientras el juego no haya llegado a 5 puntos, se sigue jugando
            mazo = new Mazo(); //Creamos un nuevo mazo de cartas al comenzar cada ronda
            mazo.barajar(); //Barajamos el mazo de cartas al comenzar cada ronda

            // Reiniciamos las manos de cada jugador y del crupier cada vez que la ronda comienza de nuevo 
            for (Jugador jugador : jugadores) {
                jugador.reiniciar(); //Reiniciamos la mano y el estado de plantado de cada jugador al comenzar cada ronda
            }
            crupier.reiniciar(); //Reiniciamos la mano del crupier al comenzar cada ronda

            // Repartimos dos cartas a cada jugador y al crupier al comenzar cada ronda , ( El juego se empieza con 2 cartas para cada jugador y el crupier) ##PRUEBA##
            for (Jugador jugador : jugadores) {
                jugador.pedirCarta(mazo); //Repartimos una carta al jugador utilizando el metodo pedirCarta
                jugador.pedirCarta(mazo); //Repartimos una segunda carta al jugador utilizando el metodo pedirCarta
            }
            crupier.pedirCarta(mazo); //Repartimos una carta al crupier utilizando el metodo pedirCarta
            crupier.pedirCarta(mazo); //Repartimos una segunda carta al crupier utilizando el metodo pedirCarta

            crupier.jugarTurno(mazo, null); //El crupier juega su turno utilizando el metodo jugarTurno, que hace que el crupier siga pidiendo cartas hasta que su mano tenga un valor de al menos 17

            // Determinamos el jugador

            ganador();

            //Mostramos las puntuaciones de cada jugador al finalizar la ronda

            for (Jugador jugador : jugadores) {
                if (jugador.obtenerPuntos() == 5) {
                    System.out.println("El ganador es: " + jugador.getNombre() + " con " + jugador.obtenerPuntos() + " puntos!"); //Si un jugador llega a 5 puntos, se declara como ganador y se muestra su nombre y puntos
                    finjuego = true; //Se establece finjuego como true para terminar el bucle del juego
                }
            }

            if (crupier.obtenerPuntos() == 5) {
                System.out.println("El crupier ha ganado con " + crupier.obtenerPuntos() + " puntos!"); //Si el crupier llega a 5 puntos, se declara como ganador y se muestra su puntuacion
                finjuego = true; //Se establece finjuego como true para terminar el bucle del juego
            }
        }
    }

    private void ganador() {

    int valorCrupier = crupier.getMano().obtenerValorMano();

    for (Jugador j : jugadores) {

        int valorJugador = j.getMano().obtenerValorMano();

        if (j.getMano().estaPasado()) {
            continue;
        }

        if (crupier.getMano().estaPasado() || valorJugador > valorCrupier) {
            j.sumarPunto();
            System.out.println(j.getNombre() + " gana la ronda!");
        }
        else if (valorJugador == valorCrupier) {
            System.out.println("Empate entre " + j.getNombre() + " y el crupier.");
        }
        else {
            crupier.sumarPunto();
            System.out.println("El crupier gana contra " + j.getNombre());
        }
    }
    }

    public static void main(String[] args) {
        JuegoBlackjack juego = new JuegoBlackjack(); //Creamos una instancia del juego de Blackjack
        juego.iniciarJuego(); //Iniciamos el juego llamando al metodo iniciarJuego
    }
}
